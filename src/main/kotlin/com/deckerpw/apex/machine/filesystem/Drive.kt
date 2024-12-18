package com.deckerpw.apex.machine.filesystem

import java.io.File
import java.net.URL

interface Drive {

//    "A:/home/deckerpw/apex"

    val name: String

    fun getFile(path: String): File
    fun list(path: String): Array<String?>?
    fun isDirectory(path: String): Boolean
    fun getURL(path: String): URL?
    fun createDirectory(path: String): Boolean
    fun delete(path: String): Boolean
    fun isReadOnly(): Boolean

}