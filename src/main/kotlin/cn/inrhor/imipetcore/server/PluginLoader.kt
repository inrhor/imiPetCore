package cn.inrhor.imipetcore.server

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.data.DataContainer
import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.common.database.Database
import cn.inrhor.imipetcore.common.database.type.DatabaseManager
import cn.inrhor.imipetcore.common.database.type.DatabaseType
import cn.inrhor.imipetcore.common.file.loadAction
import cn.inrhor.imipetcore.common.file.loadPet
import cn.inrhor.imipetcore.common.file.loadUi
import cn.inrhor.imipetcore.common.ui.UiData
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang

/**
 * 插件管理
 */
object PluginLoader {

    @Awake(LifeCycle.LOAD)
    fun load() {
        logo()
        loadTask()
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        logo()
        unloadTask()
    }

    fun loadTask() {
        val pluginCon = ImiPetCore.plugin.description
        console().sendLang("LOADER_INFO", pluginCon.name, pluginCon.version)
        if (ImiPetCore.config.getString("data.type")?.uppercase() == "MYSQL") {
            DatabaseManager.type = DatabaseType.MYSQL
        }
        Database.initDatabase()
        loadPet()
        loadAction()
        loadUi()
        Bukkit.getOnlinePlayers().forEach {
            Database.database.pull(it.uniqueId)
        }
    }

    fun unloadTask() {
        Bukkit.getOnlinePlayers().forEach {
            it.getData().petDataList.forEach { e ->
                e.petEntity?.entity?.remove()
            }
        }
        DataContainer.petOptionMap.clear()
        DataContainer.actionOptionMap.clear()
        DataContainer.playerContainer.clear()
        UiData.customUi.clear()
    }

    fun logo() {
        val logo = listOf(
                " _       _ ______           ______                 ",
                "(_)     (_|_____ \\     _   / _____)                ",
                " _ ____  _ _____) )___| |_| /      ___   ____ ____ ",
                "| |    \\| |  ____/ _  )  _) |     / _ \\ / ___) _  )",
                "| | | | | | |   ( (/ /| |_| \\____| |_| | |  ( (/ / ",
                "|_|_|_|_|_|_|    \\____)\\___)______)___/|_|   \\____)")
        for (s in logo) {
            console().sendMessage(s.colored())
        }
    }

}