package com.deckerpw.apex.apps.filemanager.ui

import com.deckerpw.apex.ui.getThemedImage
import com.deckerpw.apex.ui.graphics.text
import com.deckerpw.apex.ui.graphics.theme
import com.deckerpw.apex.ui.widgets.AbstractSelectionList
import com.deckerpw.apex.ui.widgets.Widget
import java.awt.Graphics2D
import java.awt.image.BufferedImage

private val ICON_FOLDER = getThemedImage("textures/apps/filemanager/icon/folder.png")
private val ICON_UNKNOWN = getThemedImage("textures/apps/filemanager/icon/unknown.png")

class FileManagerList(parent: Widget?, x: Int, y: Int, width: Int, height: Int) : AbstractSelectionList(
    parent, x, y, width, height, 15
) {

    fun addEntry(fileEntry: FileEntry){
        fileEntry.odd = entries.size % 2 == 0
        entries.add(fileEntry)
        update()
    }

    class FileEntry(parent: Widget?, width: Int, val name: String, val date : String, val folder: Boolean) : Entry(parent, width, 15) {

        var odd: Boolean? = null
        private val backgroundColors = arrayOf(
            theme.listEntryColorA,
            theme.listEntryColorB,
            theme.listEntrySelectedColor
        )

        override fun paint(graphics2D: Graphics2D) {
            graphics2D.color = backgroundColors[if (selected) 2 else (if (odd == true) 1 else 0)]
            graphics2D.fillRect(0,0, width, height)
            val icon: BufferedImage? = if (folder) ICON_FOLDER else ICON_UNKNOWN
            graphics2D.drawImage(icon, 7, 3, 9, 9, null)
            graphics2D.color = theme.listEntryTextColor
            graphics2D.text(name, 28, 1)
            graphics2D.text(date, 249, 1)
        }

    }

}