package cn.inrhor.imipetcore.server

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.data.DataContainer
import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.manager.ModelManager
import cn.inrhor.imipetcore.api.manager.PetManager.callPet
import cn.inrhor.imipetcore.common.database.Database
import cn.inrhor.imipetcore.common.database.type.DatabaseManager
import cn.inrhor.imipetcore.common.database.type.DatabaseType
import cn.inrhor.imipetcore.common.file.loadAction
import cn.inrhor.imipetcore.common.file.loadPet
import cn.inrhor.imipetcore.common.file.loadSkill
import cn.inrhor.imipetcore.common.hook.protocol.ProtocolEntity
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.warning
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang

/**
 * 插件管理
 */
object PluginLoader {

    val attributePlus by lazy {
        Bukkit.getPluginManager().getPlugin("AttributePlus") != null
    }

    val authMeLoad by lazy {
        Bukkit.getPluginManager().getPlugin("AuthMe") != null
    }

    val protocolLibLoad by lazy {
        Bukkit.getPluginManager().getPlugin("ProtocolLib") != null
                && ConfigRead.nms == "mod"
    }

    @Awake(LifeCycle.ENABLE)
    fun load() {
        logo()
        loadTask()
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        logo()
        unloadTask()
    }

    private fun yamlLoad(yaml: String, a: () -> Unit) {
        try {
            a()
        } catch (e: Exception) {
            warning("加载配置文件出错，请检查${yaml}配置")
        }
    }

    fun loadTask() {
        val pluginCon = ImiPetCore.plugin.description
        console().sendLang("LOADER_INFO", pluginCon.name, pluginCon.version)

        if (ImiPetCore.config.getString("data.type")?.uppercase() == "MYSQL") {
            DatabaseManager.type = DatabaseType.MYSQL
        }
        Database.initDatabase()
        ProtocolEntity.initEntityMap()
        ModelManager.modelLoader.load()

        yamlLoad("宠物") { loadPet() }
        yamlLoad("行为") { loadAction() }
        yamlLoad("技能") { loadSkill() }

        Bukkit.getOnlinePlayers().forEach {
            Database.database.pull(it.uniqueId)
            it.getData().petDataList.forEach { petData ->
                if (petData.isFollow()) it.callPet(petData)
            }
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