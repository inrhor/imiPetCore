package cn.inrhor.imipetcore.loader

import cn.inrhor.imipetcore.ImipetCore
import taboolib.common.platform.console

object Logger {

    private fun logoSend() {
        listOf(
            "  _                 _   _______         _    " ,
            " (_)               (_) |_   __ \\       / |_  " ,
            " __   _ .--..--.   __    | |__) |.---.`| |-' " ,
            "[  | [ `.-. .-. | [  |   |  ___// /__\\\\| |   " ,
            " | |  | | | | | |  | |  _| |_   | \\__.,| |,  " ,
            "[___][___||__||__][___]|_____|   '.__.'\\__/  ").forEach {
            console().sendMessage(it)
        }
    }

    fun loadInfo() {
        val pluginCon = ImipetCore.plugin.description
        logoSend()
    }

}