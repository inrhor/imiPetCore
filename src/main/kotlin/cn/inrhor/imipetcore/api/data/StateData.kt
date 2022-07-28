package cn.inrhor.imipetcore.api.data

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.state.ActiveState
import cn.inrhor.imipetcore.api.entity.state.WalkState
import org.bukkit.entity.Entity
import taboolib.common.platform.function.warning

/**
 * 行为状态数据
 *
 * @param state 当前行为状态
 * @param actionState 行为状态
 */
class StateData(var state: String = "walk", val actionState: MutableMap<String, ActiveState> = mutableMapOf()) {

    init {
        addState("walk", WalkState())
    }

    fun existState(name: String): Boolean {
        return actionState.containsKey(name)
    }

    /**
     * 新增行为状态
     */
    fun addState(name: String, activeState: ActiveState) {
        if (existState(name)) {
            warning("$name 行为状态已存在")
            return
        }
        actionState[name] = activeState
    }

    /**
     * 删除行为状态
     */
    fun removeState(name: String) {
        actionState.remove(name)
    }

    /**
     * 更新行为
     */
    fun updateState(petEntity: PetEntity, name: String, target: Entity?) {
        if (!existState(name)) return
        state = name
        callState(petEntity, target)
    }

    /**
     * 执行行为
     */
    private fun callState(petEntity: PetEntity, entity: Entity? = null) {
        if (actionState.containsKey(state)) {
            actionState[state]?.run(petEntity, entity)
        }
    }

}