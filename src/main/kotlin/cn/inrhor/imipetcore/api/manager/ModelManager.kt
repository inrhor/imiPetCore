package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.common.model.ModelLoader
import cn.inrhor.imipetcore.common.model.ModelSelect
import cn.inrhor.imipetcore.common.option.StateOption
import com.ticxo.modelengine.api.ModelEngineAPI
import ltd.icecold.orangeengine.api.OrangeEngineAPI
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
                val active = ModelEngineAPI.createActiveModel(modelID)
                val modelEntity = if (init) ModelEngineAPI.createModeledEntity(this) else ModelEngineAPI.getModeledEntity(uniqueId)
                modelEntity?.addModel(active, true)
                modelEntity?.isBaseEntityVisible = false
                if (name.isNotEmpty()) {
                    val nameHandle = active?.nametagHandler?.bones?.get("name")
                    nameHandle?.customName = name // 标签 tag_name
                    nameHandle?.isCustomNameVisible = true
                }
                return
            }
            ModelSelect.ORANGE_ENGINE -> {
                OrangeEngineAPI.getModelManager()?.addNewModelEntity(uniqueId, modelID)
            }
        }
        customName = name
        isCustomNameVisible = true
    }

    /**
     * 清除模型
     */
    fun Entity.clearModel(select: ModelSelect) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                ModelEngineAPI.getModeledEntity(uniqueId).destroy()
            }
            ModelSelect.ORANGE_ENGINE -> {
                OrangeEngineAPI.getModelManager()?.removeModelEntity(uniqueId, true)
            }
        }
    }

    /**
     * 播放模型动作动画
     *
     * lerpIn 插入动画所用的时间（以秒为单位）
     * lerpOut 插出动画所用的时间（以秒为单位）
     * speed 播放速度乘数（默认速度为 1）
     * force 是否强制播放
     */
    fun Entity.playAnimation(modelID: String, select: ModelSelect, action: String, state: StateOption) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                val modelEntity = ModelEngineAPI.getModeledEntity(uniqueId)?: return
                val active = modelEntity.getModel(modelID)?: return
                val animation = active.animationHandler?: return
                animation.playAnimation(action, state.lerpin, state.lerpout, state.speed, state.force)
            }
            ModelSelect.ORANGE_ENGINE -> {
                OrangeEngineAPI.getModelManager()?.getModelEntity(uniqueId)?.playAnimation(action)
            }
        }
    }

}