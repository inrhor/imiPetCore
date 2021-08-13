package cn.inrhor.imipetcore.loader

import cn.inrhor.imipetcore.ImipetCore
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object PluginLoader {

    private var reloading = false

    fun isReloading(): Boolean {
        return reloading
    }

    fun doReload() {
        if (reloading) {
            throw RuntimeException("imiPetCore is reloading..")
        }
        reloading = true
        Logger.loadInfo()
        reloading = false
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        doReload()
    }


    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        // 确保取消调度的所有任务
        Bukkit.getScheduler().cancelTasks(ImipetCore.plugin)
    }
}