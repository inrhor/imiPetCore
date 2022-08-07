package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.entity.state.ActiveState
import org.bukkit.Location
import org.bukkit.entity.Entity

object ActionManager {

    /**
     * 行为状态数据设置目标实体
     */
    fun ActiveState.setActionData(target: Entity) {
        entity = target
    }

    /**
     * 行为状态数据设置目标位置
     */
    fun ActiveState.setActionData(loc: Location) {
        location = loc
    }

    /**
     * 行为状态数据设置延迟
     */
    fun ActiveState.setActionData(int: Int) {
        delay = int
    }

}