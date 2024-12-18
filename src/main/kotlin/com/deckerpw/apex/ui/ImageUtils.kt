package com.deckerpw.apex.ui

import com.deckerpw.apex.machine.Machine
import com.deckerpw.apex.ui.graphics.theme
import java.awt.image.BufferedImage

fun getImage(path:String): BufferedImage? {
    val url = Machine.instance?.filesystem?.getURL(path)
    return if (url != null) {
        javax.imageio.ImageIO.read(url)
    } else {
        null
    }
}

fun getThemedImage(suffix: String): BufferedImage? {
    return getImage("${theme.assetDir}/$suffix")
}