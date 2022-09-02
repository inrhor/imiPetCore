package cn.inrhor.imipetcore.server

import cn.inrhor.imipetcore.api.data.DataContainer.actionOptionMap
import cn.inrhor.imipetcore.api.data.DataContainer.petOptionMap
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.pluginVersion
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart

object Metrics {

    private val bStats by lazy {
        Metrics(16338, pluginVersion, Platform.BUKKIT)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        bStats.let {
            it.addCustomChart(SingleLineChart("pet") {
                petOptionMap.size
            })
            it.addCustomChart(SingleLineChart("action") {
                actionOptionMap.size
            })
        }
    }

}