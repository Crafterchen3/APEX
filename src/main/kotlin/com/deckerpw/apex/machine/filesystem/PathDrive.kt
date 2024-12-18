package com.deckerpw.apex.machine.filesystem

import java.io.File
import java.net.URL

class PathDrive(private val root: String, override val name:String) : Drive {

    override fun getFile(path: String): File {
        return File("$root/$path")
    }

    override fun list(path: String): Array<String?>? {
        val file = getFile(path)
        return file.list()
    }

    override fun isDirectory(path: String): Boolean {
        val file = getFile(path)
        return file.isDirectory
    }

    override fun getURL(path: String): URL? {
        return getFile(path).toURI().toURL()
    }

    override fun createDirectory(path: String): Boolean {
        val file = getFile(path)
        return file.mkdirs()
    }

    override fun delete(path: String): Boolean {
        val file = getFile(path)
        return file.delete()
    }

    override fun isReadOnly(): Boolean {
        return false
    }

}