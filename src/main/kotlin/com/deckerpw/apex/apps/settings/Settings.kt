package com.deckerpw.apex.apps.settings

import com.deckerpw.apex.machine.Executable
import com.deckerpw.apex.machine.Program
import com.deckerpw.apex.machine.machine
import com.deckerpw.apex.ui.scenes.DesktopScene
import com.deckerpw.apex.ui.widgets.Window
import com.deckerpw.apex.ui.widgets.WindowManager

@Program
class Settings : Executable {
    override fun onStart() {
        machine.windowManager?.add(SettingsWindow(machine.windowManager))
    }
}

class SettingsWindow(parent: WindowManager?): Window(parent, 100, 100, 400, 400, "Settings") {
}