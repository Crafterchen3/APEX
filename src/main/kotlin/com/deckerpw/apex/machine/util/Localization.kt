package com.deckerpw.apex.machine.util

import com.deckerpw.apex.machine.machine
import com.google.gson.Gson
import java.net.URL

class Localization {
    private val localizationMap: MutableMap<String,MutableMap<String,String>> = mutableMapOf()

    fun loadLanguage(locale: String,url: URL) {
        val json = url.readText()
        val map = Gson().fromJson(json,MutableMap::class.java) as MutableMap<String,String>
        localizationMap[locale] = map
    }

    fun translate(key: String): String {
        return localizationMap[machine.locale]?.get(key) ?: localizationMap["en"]?.get(key) ?: key
    }

    fun translate(key: String,locale: String): String {
        return localizationMap[locale]?.get(key) ?: localizationMap["en"]?.get(key) ?: key
    }

}

fun translatedString(key: String): String {
    return machine.localization.translate(key)
}