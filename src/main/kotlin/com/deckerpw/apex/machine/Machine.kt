package com.deckerpw.apex.machine

import com.deckerpw.apex.apps.filemanager.FileTypeRegistry
import com.deckerpw.apex.machine.filesystem.DriveLetter
import com.deckerpw.apex.machine.filesystem.Filesystem
import com.deckerpw.apex.machine.filesystem.JavaDrive
import com.deckerpw.apex.machine.filesystem.PathDrive
import com.deckerpw.apex.machine.util.Localization
import com.deckerpw.apex.ui.Screen
import com.deckerpw.apex.ui.scenes.DesktopScene
import com.deckerpw.apex.ui.scenes.LoginScene
import com.deckerpw.apex.ui.scenes.WindowManagerScene
import com.deckerpw.apex.ui.screenHeight
import com.deckerpw.apex.ui.screenWidth
import com.deckerpw.apex.ui.widgets.WindowManager
import com.google.gson.internal.LinkedTreeMap

private var _machine: Machine? = null
val machine: Machine
    get() = _machine ?: throw RuntimeException("Machine has not been initialized")

class Machine(val path: String) {

    val filesystem = Filesystem()
    internal val config: FileConfig
    internal val users = mutableListOf<User>()

    private var _currentUser: User? = null
    val currentUser: User?
        get() = _currentUser

    private var running = false
    val isRunning: Boolean
        get() = running

    val localization: Localization

    private var _locale = "en"
    val locale: String
        get() = _locale

    // Screen can be either a regular Screen or an OpenGLScreen
    var screen: Screen
    val windowManager: WindowManager?
        get() {
            (screen as? Screen)?.let { s ->
                (s.currentScene as? WindowManagerScene)?.let {
                    return it.windowManager
                }
            }
            return null
        }

    val fileTypeRegistry: FileTypeRegistry = FileTypeRegistry()

    init {
        if (_machine != null) {
            throw IllegalStateException("Machine already exists")
        }
        _machine = this
        filesystem.mount(DriveLetter.A, PathDrive(path, "Root"))
        filesystem.mount(DriveLetter.J, JavaDrive.javaDrive)
        config = FileConfig(filesystem.getFile("A:config.json"))
        localization = Localization()
        loadLanguages()
        populateUsers()

        loader.findPrograms()
        screen = Screen()
        if (config.has("autologin") and config.has("autologin.username") and config.has("autologin.password")) {
            val username = config.get<String>("autologin.username")
            val password = config.get<String>("autologin.password")
            val user = machine.login(username, password)
            if (user != null)
                screen.setScene(DesktopScene(screen, screenWidth, screenHeight))
            else
                screen.setScene(LoginScene(screen, screenWidth, screenHeight))
        }else{
            screen.setScene(LoginScene(screen, screenWidth, screenHeight))
        }
    }

    private fun loadLanguages() {
        localization.apply {
            loadLanguage("en",filesystem.getURL("J:/lang/en.json") ?: throw RuntimeException("Localization file not found"))
            loadLanguage("de",filesystem.getURL("J:/lang/de.json") ?: throw RuntimeException("Localization file not found"))
        }
    }

    private fun populateUsers() {
        if (!config.has("users")) {
            println("No users found in config")
            return
        }
        val usersJson = config.get<LinkedTreeMap<String, Any>>("users")
        for (user in usersJson.keys) {
            users.add(User(this, user))
        }
    }

    fun login(username: String, password: String): User? {
        if (currentUser != null) {
            println("User ${currentUser?.username} is already logged in")
            return null
        }
        val user = users.find { it.username == username }
        if (user == null) {
            println("User $username not found")
            return null
        }
        if (!user.checkCredentials(password)) {
            println("User $username not found")
            return null
        }
        _currentUser = user
        return user
    }

    fun logout() {
        if (currentUser == null) {
            println("No user is logged in")
            return
        }
        _currentUser = null
    }

    fun run(): SuperUser? {
        if (running) {
            println("Machine is already running")
            return null
        }
        running = true
        println("Machine is running at $path")
        return SuperUser(this)
    }

    fun runProgram(program: String) {
        loader.getProgram(program)?.onStart()
    }
}

class User internal constructor(machine: Machine, val username: String) {

    private var _displayName: String by ConfigValue("users.$username.display_name", machine.config, username)
    val displayName: String
        get() = _displayName

    private var pass: Int by ConfigValue("users.$username.pass", machine.config, "".hashCode())
    val config = FileConfig(machine.filesystem.getFile("A:home/$username/config.json"))

    internal fun checkCredentials(password: String): Boolean {
        return pass == password.hashCode()
    }

    internal fun changePassword(password: String) {
        pass = password.hashCode()
    }

}

class SuperUser internal constructor(val machine: Machine){

    internal fun createUser(username: String, password: String, displayName: String? = null){
        machine.apply {
            if (users.find { it.username == username } != null) {
                println("User $username already exists")
                return
            }
            val userPath = "A:/home/$username"
            filesystem.createDirectory(userPath)
            config.set("users.$username.display_name", displayName ?: username)
            config.set("users.$username.pass", password.hashCode())
            users.add(User(this, username))
        }
    }

}
