package com.deckerpw.apex.apps.filemanager

import java.awt.image.BufferedImage
import java.io.File

class FileType(val icon: BufferedImage, val action: (file: File) -> Unit) {

    fun invoke(file: File){
        action(file)
    }

}

class FileTypeRegistry {

    val fileTypes: MutableMap<String, FileType> = mutableMapOf()

    fun registerFileType(extension: String, fileType: FileType){
        fileTypes.put(extension,fileType)
    }

    fun registerFileType(extensions: List<String>, fileType: FileType){
        for (extension in extensions) {
            registerFileType(extension, fileType)
        }
    }

    fun getFileType(extension: String) : FileType? {
        return fileTypes[extension]
    }

}