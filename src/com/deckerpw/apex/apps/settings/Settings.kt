package com.deckerpw.apex.apps.settings

import com.deckerpw.apex.apps.filemanager.ui.FileManagerList
import com.deckerpw.apex.machine.Executable
import com.deckerpw.apex.machine.Program
import com.deckerpw.apex.machine.machine
import com.deckerpw.apex.ui.graphics.theme
import com.deckerpw.apex.ui.scenes.DesktopScene
import com.deckerpw.apex.ui.widgets.SimpleSelectionList
import com.deckerpw.apex.ui.widgets.Window
import com.deckerpw.apex.ui.widgets.WindowManager
import com.deckerpw.apex.ui.widgets.colorButton
import java.awt.Color

@Program
class Settings : Executable {
    override fun onStart() {
        machine.windowManager?.add(SettingsWindow(machine.windowManager))
    }
}

class SettingsWindow(parent: WindowManager?): Window(parent, 100, 100, 400, 400, "Settings") {

    init {
        add(FileManagerList(this, 10, 30, 380, 300).also {
            it.addEntry(FileManagerList.FileEntry(it,380,"File1.txt","Never",false))
            it.addEntry(FileManagerList.FileEntry(it,380,"File2.txt","Never",false))
            it.addEntry(FileManagerList.FileEntry(it,380,"Folder","Never",true))
            it.addEntry(FileManagerList.FileEntry(it,380,"File3.txt","Never",false))
        })
        add(colorButton(this,10,340,80,20,"Button 1", arrayOf(Color.WHITE, theme.primaryColor, theme.primaryColor)){ _,_,_ ->

        })
    }

}