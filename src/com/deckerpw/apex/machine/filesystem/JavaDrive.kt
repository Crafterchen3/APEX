package com.deckerpw.apex.machine.filesystem

import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.ReadOnlyFileSystemException

class JavaDrive : Drive {
    private var fileSystem: FileSystem? = null

    override val name: String
        get() = "JAVA"


    init {
        val classLoader = Thread.currentThread().contextClassLoader
        val uri = classLoader.getResource("assets")?.toURI()
        if (uri != null) {
            if (uri.scheme == "jar") {
                try {
                    fileSystem = FileSystems.newFileSystem(uri, emptyMap<String, Any>())
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    override fun getFile(path: String): File {
        val url = getURL(path) ?: throw RuntimeException("File not found: $path")
        val file = File(url.file)
        return file
    }

    override fun list(path: String): Array<String?>? {
        if (fileSystem != null) {
            val path1 = fileSystem!!.getPath(path)
            try {
                val list: MutableList<Path> = ArrayList(Files.walk(path1, 1).toList())
                list.removeAt(0)
                val files = arrayOfNulls<String>(list.size)
                for (i in list.indices) {
                    files[i] = list[i].fileName.toString()
                }
                return files
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        val folder = getFile(path)
        return folder!!.list()
    }

    override fun isDirectory(path: String): Boolean {
        if (fileSystem != null) {
            val path1 = fileSystem!!.getPath(path)
            return Files.isDirectory(path1)
        }
        val file = getFile(path)
        return file!!.isDirectory
    }

    override fun getURL(path: String): URL? {
        return JavaDrive::class.java.getResource(path)
    }

    override fun createDirectory(path: String): Boolean {
        throw ReadOnlyFileSystemException()
    }

    override fun delete(path: String): Boolean {
        throw ReadOnlyFileSystemException()
    }

    override fun isReadOnly(): Boolean {
        return true
    }

    companion object {
        private var instance: JavaDrive? = null
        val javaDrive: JavaDrive
            get() {
                if (instance == null) {
                    try {
                        instance = JavaDrive()
                    } catch (e: URISyntaxException) {
                        throw RuntimeException(e)
                    }
                }
                return instance!!
            }
    }
}
