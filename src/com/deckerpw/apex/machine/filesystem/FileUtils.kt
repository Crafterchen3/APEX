package com.deckerpw.apex.machine.filesystem

import java.io.File

fun File.readFully(): String {
    return this.readText(Charsets.UTF_8)
}