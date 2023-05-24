package cn.inrhor.imipetcore

import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitIO
import taboolib.platform.BukkitPlugin

/**
 * 入口
 */
object ImiPetCore : Plugin() {

    @Config(migrate = true, autoReload = true)
    lateinit var config: Configuration
        private set

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    val resource by lazy {
        BukkitIO()
    }

}