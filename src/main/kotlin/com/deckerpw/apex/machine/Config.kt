package com.deckerpw.apex.machine

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import java.io.File
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class Config {

    protected val config = LinkedTreeMap<String, Any>()

    open fun <T> get(path: String): T {
        try {
            val parts = path.split(".")
            var current: LinkedTreeMap<String,Any> = config
            for ((index, part) in parts.withIndex()) {
                if (index == parts.size - 1) {
                    return current[part] as T
                }
                if (current.contains(part)) {
                    current = current[part] as LinkedTreeMap<String,Any>
                } else {
                    current[part] = LinkedTreeMap<String,Any>()
                    current = current[part] as LinkedTreeMap<String,Any>
                }
            }
        }catch (e: Exception){
            println("Error getting config value at path $path")
            e.printStackTrace()
        }
        return null as T
    }

    open fun <T> set(path: String, value: T) {
        try {
            val parts = path.split(".")
            var current: LinkedTreeMap<String,Any> = config
            for ((index, part) in parts.withIndex()) {
                if (index == parts.size - 1) {
                    current[part] = value as Any
                    return
                }
                if (current.contains(part)) {
                    current = current[part] as LinkedTreeMap<String,Any>
                } else {
                    current[part] = LinkedTreeMap<String,Any>()
                    current = current[part] as LinkedTreeMap<String,Any>
                }
            }
        }catch (e: Exception){
            println("Error setting config value at path $path")
            e.printStackTrace()
        }
    }

}

class FileConfig(private val file: File) : Config() {

    private fun save() {
        // Save to file
        val jsonString = Gson().toJson(config).let { GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(it)) }
        file.writeText(jsonString)
    }

    private fun load() {
        config.clear()
        // Load from file
        if (!file.exists()) {
            save()
            return
        }
        val jsonString = file.readText()
        val json = Gson().fromJson(jsonString, Map::class.java)
        json.forEach { (key, value) -> config[key as String] = value as Any }
    }

    init {
        load()
    }

    override fun <T> set(path: String, value: T) {
        super.set(path, value)
        save()
    }

}

class ConfigValue<T>(private val path: String, private val config: Config, private val default: T) :
    ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value: T = config.get(path)
        return if (value == null) {
            config.set(path, default)
            default
        } else {
            value
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        config.set(path, value)
    }

}

class ReadOnlyConfigValue<T>(private val path: String, private val config: Config, private val default: T) :
    ReadOnlyProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return config.get<T>(path) ?: default
    }

}
