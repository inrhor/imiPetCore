package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.manager.MetaManager.removeMeta
import cn.inrhor.imipetcore.api.manager.MetaManager.setMeta
import cn.inrhor.imipetcore.common.model.ModelLoader
import cn.inrhor.imipetcore.common.model.ModelSelect
import cn.inrhor.imipetcore.common.option.StateOption
import com.germ.germplugin.api.GermPacketAPI
import com.ticxo.modelengine.api.ModelEngineAPI
import com.ticxo.modelengine.api.mount.controller.flying.FlyingMountController
import com.ticxo.modelengine.api.mount.controller.walking.WalkingMountController
import eos.moe.dragoncore.api.ModelAPI
import ltd.icecold.orangeengine.api.OrangeEngineAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.submit

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
                if (modelLoader.modelEngine) {
                    val active = ModelEngineAPI.createActiveModel(modelID)
                    val modelEntity =
                        if (init) ModelEngineAPI.createModeledEntity(this) else ModelEngineAPI.getModeledEntity(uniqueId)
                    modelEntity?.addModel(active, true)
                    modelEntity?.isBaseEntityVisible = false
                    if (name.isNotEmpty()) {
                        val nameHandle = active?.nametagHandler?.bones?.get("name")
                        nameHandle?.customName = name // 标签 tag_name
                        nameHandle?.isCustomNameVisible = true
                    }
                    return
                }
            }
            ModelSelect.ORANGE_ENGINE -> {
                if (modelLoader.orangeEngine) {
                    OrangeEngineAPI.getModelManager()?.addNewModelEntity(uniqueId, modelID)
                }
            }
            ModelSelect.GERM_ENGINE -> {
                if (modelLoader.germEngine) {
                    customName = modelID
                    return
                }
            }
            ModelSelect.DRAGON_CORE -> {
                if (modelLoader.dragonCore) {
                    ModelAPI.setEntityModel(uniqueId, modelID)
                    return
                }
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
                if (modelLoader.modelEngine) {
                    ModelEngineAPI.getModeledEntity(uniqueId).destroy()
                }
            }
            ModelSelect.ORANGE_ENGINE -> {
                if (modelLoader.orangeEngine) {
                    OrangeEngineAPI.getModelManager()?.removeModelEntity(uniqueId, true)
                }
            }
            ModelSelect.GERM_ENGINE -> {
                if (modelLoader.germEngine) {
                    customName = ""
                }
            }
            ModelSelect.DRAGON_CORE -> {
                if (modelLoader.dragonCore) {
                    ModelAPI.removeEntityModel(uniqueId)
                }
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
    fun LivingEntity.playAnimation(modelID: String, select: ModelSelect, action: String, state: StateOption) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                if (modelLoader.modelEngine) {
                    val modelEntity = ModelEngineAPI.getModeledEntity(uniqueId) ?: return
                    val active = modelEntity.getModel(modelID) ?: return
                    val animation = active.animationHandler ?: return
                    animation.playAnimation(action, state.lerpin, state.lerpout, state.speed, state.force)

                }
            }
            ModelSelect.ORANGE_ENGINE -> {
                if (modelLoader.orangeEngine) {
                    OrangeEngineAPI.getModelManager()?.getModelEntity(uniqueId)?.playAnimation(action)
                }
            }
            ModelSelect.GERM_ENGINE -> {
                if (modelLoader.germEngine) {
                    val s = "${modelID}_${action}"
                    Bukkit.getOnlinePlayers().forEach {
                        GermPacketAPI.sendModelAnimation(it, this, s)
                        submit(async = true, delay = state.lerpout.toLong()) {
                            GermPacketAPI.stopModelAnimation(it, this@playAnimation, s)
                        }
                    }
                }
            }
            ModelSelect.DRAGON_CORE -> {
                if (modelLoader.dragonCore) {
                    ModelAPI.setEntityAnimation(this, action, state.time)
                }
            }
        }
    }

    /**
     * 乘骑宠物
     *
     * @param driver 驾驶者
     * @param select 模型引擎
     * @param actionType 行为类型
     */
    fun Entity.driveRide(driver: Entity, select: ModelSelect, actionType: ActionType) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                val modelEntity = ModelEngineAPI.getModeledEntity(uniqueId)?: return
                val controller = if (actionType == ActionType.WALK) WalkingMountController() else FlyingMountController()
                modelEntity.mountManager.isCanSteer = true
                modelEntity.mountManager.setDriver(driver, controller)
            }
            else -> {
                setMeta("drive", actionType)
                addPassenger(driver)
            }
        }
    }

    /**
     * 卸载乘骑
     */
    fun Entity.delDriveRide(select: ModelSelect) {
        when (select) {
            ModelSelect.MODEL_ENGINE -> {
                val modelEntity = ModelEngineAPI.getModeledEntity(uniqueId) ?: return
                modelEntity.mountManager.removeDriver()
            }
            else -> {
                removeMeta("drive")
                passengers.forEach { removePassenger(it) }
            }
        }
    }

    enum class ActionType {
        FLY, WALK
    }

}