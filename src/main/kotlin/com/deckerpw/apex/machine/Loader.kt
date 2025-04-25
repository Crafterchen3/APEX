package com.deckerpw.apex.machine

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.io.IOException
import java.net.URLClassLoader

val loader = Loader()

class Loader {

    private val programs: MutableMap<String, Executable> = mutableMapOf()
    private val classLoaders = mutableListOf<ClassLoader>()

    init {
        classLoaders.add(javaClass.classLoader)
    }

    fun loadJar(path: String) {
        println("Loading jar: $path")
        try {
            classLoaders.add(
                URLClassLoader(
                    arrayOf(machine.filesystem.getURL(path) ?: return), javaClass.getClassLoader()
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun findPrograms() {
        // Add every CommandEventListener annotated with @Program
        val configuration: ConfigurationBuilder =
            ConfigurationBuilder().forPackages("") // Empty string means scan the entire classpath
                .addScanners(Scanners.TypesAnnotated)
        for (classLoader in classLoaders) {
            configuration.addClassLoaders(classLoader)
            configuration.addUrls(ClasspathHelper.forClassLoader(classLoader))
        }
        val reflections = Reflections(configuration)
        val annotatedClasses: Set<Class<*>> = reflections.getTypesAnnotatedWith(Program::class.java)
        for (clazz in annotatedClasses) {
            if (Executable::class.java.isAssignableFrom(clazz)) {
                try {
                    val listener: Executable = clazz.getDeclaredConstructor().newInstance() as Executable
                    programs[clazz.name] = listener
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getProgram(name: String): Executable? {
        return programs[name]
    }
}

annotation class Program

interface Executable {

    fun onStart()

}