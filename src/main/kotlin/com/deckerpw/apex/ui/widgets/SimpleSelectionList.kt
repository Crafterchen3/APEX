package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.graphics.text
import com.deckerpw.apex.ui.graphics.theme
import java.awt.Color
import java.awt.Graphics2D

class SimpleSelectionList(parent: Widget?, x: Int, y: Int, width: Int, height: Int, names: List<String>) : AbstractSelectionList(parent, x, y, width,
    height, 15
) {

    init {
        names.forEachIndexed { index, name ->
            entries.add(StringEntry(this, width, name, index % 2 == 0))
        }
    }

    class StringEntry(parent: Widget?, width: Int, val string: String, val odd: Boolean) : Entry(parent, width, 15) {

        private val backgroundColors = arrayOf(
            theme.listEntryColorA,
            theme.listEntryColorB,
            theme.listEntrySelectedColor
        )

        override fun paint(graphics2D: Graphics2D) {
            graphics2D.color = backgroundColors[if (selected) 2 else (if (odd) 1 else 0)]
            graphics2D.fillRect(0,0, width, height)
            graphics2D.color = theme.listEntryTextColor
            graphics2D.text(string, 3, 2)
        }
    }

}