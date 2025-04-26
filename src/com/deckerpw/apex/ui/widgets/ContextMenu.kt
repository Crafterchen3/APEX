package com.deckerpw.apex.ui.widgets

import com.deckerpw.apex.ui.graphics.TextureFill
import com.deckerpw.apex.ui.graphics.theme
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.max

private var _contextMenuHost: Container? = null
var contextMenuHost: Container?
    get() = _contextMenuHost;
    set(value) {
        if (_contextMenuHost == null) {
            _contextMenuHost = value
        }
    }

fun createContextMenu(x: Int, y: Int, actions: List<ContextMenu.Action>) {
    if (contextMenuHost == null) {
        return
    }
    contextMenuHost?.clear()
    val contextMenu = createContextMenu(contextMenuHost, x, y, actions)
    contextMenuHost?.add(contextMenu)
    contextMenu.update()
}

private fun createContextMenu(parent: Widget?, x: Int, y: Int, actions: List<ContextMenu.Action>): ContextMenu {
    val width = max(actions.maxOf { it.name.length } + 10, 100)
    val height = actions.size * 20 + 2
    return ContextMenu(parent, x-2, y-2, width, height, actions)
}

class ContextMenu(
    parent: Widget?,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    actions: List<Action>
) : Container(parent, x, y, width, height, TextureFill(createBackground(width, height))) {

    init {
        actions.forEachIndexed { index, action ->
            add(
                colorButton(
                    this,
                    1,
                    index * 20 + 1,
                    width - 2,
                    20,
                    action.name,
                    arrayOf(Color(0, 0, 0, 0), theme.primaryColor, theme.primaryColor),
                    false
                ) { _, _, _ ->
                    action.action()
                    (parent as? Container)?.remove(this)
                    parent?.update()
                }
            )
        }
    }

    data class Action(
        val name: String,
        val action: () -> Unit
    )

    override fun onExit() {
        super.onExit()
        (parent as? Container)?.remove(this)
    }

}

private fun createBackground(width: Int, height: Int): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics2D = image.createGraphics()
    graphics2D.color = theme.windowBGColor
    graphics2D.fillRect(0, 0, width, height)
    graphics2D.color = Color.WHITE
    graphics2D.drawRect(0, 0, width - 1, height - 1)
    graphics2D.dispose()
    return image
}

