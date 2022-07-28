package cn.inrhor.imipetcore.server

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.common.file.loadPet
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * 插件管理
 */
object PluginLoader {

    @Awake(LifeCycle.LOAD)
    fun load() {
        loadPet()
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        Bukkit.getOnlinePlayers().forEach {
            it.getData().petDataList.forEach { e ->
                e.petEntity?.entity?.remove()
            }
        }
    }

}