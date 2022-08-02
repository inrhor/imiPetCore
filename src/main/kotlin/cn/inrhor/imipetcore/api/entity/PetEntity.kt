package cn.inrhor.imipetcore.api.entity

import cn.inrhor.imipetcore.api.data.DataContainer.getAction
import cn.inrhor.imipetcore.api.data.StateData
import cn.inrhor.imipetcore.api.entity.state.ActiveState
import cn.inrhor.imipetcore.api.manager.OptionManager.getActionOption
import cn.inrhor.imipetcore.api.manager.PetManager.setMeta
import cn.inrhor.imipetcore.api.entity.ai.AttackAi
import cn.inrhor.imipetcore.api.entity.ai.HookAi
import cn.inrhor.imipetcore.api.entity.ai.WalkAi
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.option.StateOption
import com.ticxo.modelengine.api.ModelEngineAPI
import com.ticxo.modelengine.api.model.ModeledEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.module.ai.*
import taboolib.platform.util.sendLang

/**
 * 宠物实体
 *
 * @param owner 主人
 * @param petData 宠物数据
 */
class PetEntity(val owner: Player, val petData: PetData, var state: StateData = StateData()) {

    /**
     * 实体
     */
    var entity: LivingEntity? = null

    /**
     * 模型
     */
    var modelEntity: ModeledEntity? = null

    init {
        getStateOption().forEach {
            val id = it.id
            val action = id.getAction()
            if (action != null) {
                state.addState(id, action)
            }
        }
    }

    /**
     * 释放宠物
     */
    fun spawn() {
        if (isDead()) {
            owner.sendLang("PET-IS-DEAD")
            return
        }
        if (entity != null) return
        petData.following = true
        if (entity != null) return
        entity = owner.world.spawnEntity(owner.location, petData.petOption().entityType) as LivingEntity
        entity?.setMeta("entity", petData.uuid)
        entity?.setMeta("owner", owner.uniqueId)
        initAction()
        updateModel()
    }

    /**
     * 初始化行为
     */
    fun initAction() {
        entity?.clearGoalAi()
        entity?.clearTargetAi()
        petData.petOption().action.forEach {
            val id = it.id
            val actionOption = id.getActionOption()
            if (actionOption != null) {
                val pri = it.priority
                when (id) {
                    "attack" -> {
                        entity?.addGoalAi(AttackAi(this@PetEntity), pri)
                    }
                    "walk" -> {
                        entity?.addGoalAi(WalkAi(this@PetEntity), pri)
                    }
                    else -> {
                        entity?.addGoalAi(HookAi(actionOption, this@PetEntity), pri)
                    }
                }
            }
        }
    }

    /**
     * 删除宠物
     */
    fun back() {
        petData.following = false
        if (isDead()) return
        entity?.remove()
        entity = null
    }

    /**
     * @return 宠物死亡
     */
    fun isDead(): Boolean {
        return entity?.isDead?: (petData.attribute.currentHP <= 0)
    }

    /**
     * 更新宠物模型
     */
    fun updateModel() {
        val modelID = petData.petOption().model.id
        if (petData.following && modelID.isNotEmpty()) {
            val me = ModelEngineAPI.api.modelManager
            val active = me.createActiveModel(modelID)
            if (modelEntity == null)modelEntity = me.createModeledEntity(entity)
            modelEntity?.addActiveModel(active)
            modelEntity?.detectPlayers()
            modelEntity?.isInvisible = true
        }
    }

    /**
     * @return 行为状态数据
     */
    fun getActionData(name: String): ActiveState? {
        return state.actionState[name]
    }

    /**
     * @return 模型动作配置
     */
    fun getStateOption(): List<StateOption> {
        return petData.petOption().model.state
    }

    /**
     * @return 模型动作配置
     */
    fun getStateOption(action: String): StateOption? {
        getStateOption().forEach {
            if (it.id == action) return it
        }
        return null
    }

}