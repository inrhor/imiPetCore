package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.common.model.ModelLoader
import cn.inrhor.imipetcore.common.model.ModelSelect
import cn.inrhor.imipetcore.common.option.StateOption
import com.ticxo.modelengine.api.ModelEngineAPI
import org.bukkit.entity.Entity

/**
 * 模型管理
 */
object ModelManager {

    /**
     * 模型加载器
     */
    val modelLoader = ModelLoader()

    /**
     * 显示模型，名称
     */
    fun Entity.display(modelID: String, init: Boolean = false, name: String = "", select: ModelSelect = ModelSelect.MODEL_ENGINE) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                val me = ModelEngineAPI.api.modelManager
                val active = me.createActiveModel(modelID)
                val modelEntity = if (init) me.createModeledEntity(this) else me.getModeledEntity(uniqueId)
                modelEntity?.addActiveModel(active)
                modelEntity?.detectPlayers()
                modelEntity?.isInvisible = true
                if (name.isNotEmpty()) {
                    modelEntity?.nametagHandler?.setCustomName("name", name) // 标签 tag_name
                    modelEntity?.nametagHandler?.setCustomNameVisibility("name", true)
                }
            }
            else -> {
                customName = name
                isCustomNameVisible = true
            }
        }
    }

    /**
     * 清除模型
     */
    fun Entity.clearModel(select: ModelSelect) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                val me = ModelEngineAPI.api.modelManager
                me.getModeledEntity(uniqueId).clearModels()
            }
        }
    }

    /**
     * 设置模型动作状态
     */
    fun Entity.setModelState(modelID: String, select: ModelSelect, action: String, state: StateOption) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                val modelEntity = ModelEngineAPI.api.modelManager.getModeledEntity(uniqueId)
                val active = modelEntity.getActiveModel(modelID)
                active?.addState(action, state.lerpin, state.lerpout, state.speed)
            }
        }
    }

}