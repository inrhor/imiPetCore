package com.github.inrhor.pier.server

import com.github.inrhor.pier.api.manager.BoardManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.pluginVersion
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart

object Metrics {

    private val bStats by lazy {
        Metrics(15732, pluginVersion, Platform.BUKKIT)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        bStats.let {
            it.addCustomChart(SingleLineChart("board") {
                BoardManager.boardMap.size
            })
        }
    }

}