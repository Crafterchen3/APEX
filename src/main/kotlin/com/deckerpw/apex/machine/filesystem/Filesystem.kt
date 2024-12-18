package com.deckerpw.apex.machine.filesystem

import java.io.File

enum class DriveLetter {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
}

class Filesystem {

    private val drives = mutableMapOf<DriveLetter,Drive>()

    fun mount(letter: DriveLetter,drive: Drive) {
        drives[letter] = drive
    }

    fun unmount(letter: DriveLetter) {
        drives.remove(letter)
    }

    fun getDrive(letter: DriveLetter): Drive?{
        return drives[letter]
    }

    fun list(path: String): Array<String?>? {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.list(parts.subList(1,parts.size).joinToString("/"))
    }

    fun isDirectory(path: String): Boolean {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.isDirectory(parts.subList(1,parts.size).joinToString("/")) ?: false
    }

    fun createDirectory(path: String): Boolean {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.createDirectory(parts.subList(1,parts.size).joinToString("/")) ?: false
    }

    fun delete(path: String): Boolean {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.delete(parts.subList(1,parts.size).joinToString("/")) ?: false
    }

    fun isReadOnly(path: String): Boolean {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.isReadOnly() ?: false
    }

    fun getURL(path: String): String? {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.getURL(parts.subList(1,parts.size).joinToString("/"))?.toString()
    }

    fun getFile(path: String): File {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive!!.getFile(parts.subList(1,parts.size).joinToString("/"))
    }

    fun exists(path: String): Boolean {
        val parts = path.split(":",limit = 2)
        val drive = getDrive(DriveLetter.valueOf(parts[0]))
        return drive?.getFile(parts.subList(1,parts.size).joinToString("/"))?.exists() ?: false
    }

}