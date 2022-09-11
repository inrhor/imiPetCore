package cn.inrhor.imipetcore.api.entity

import cn.inrhor.imipetcore.api.entity.ai.addAi
import cn.inrhor.imipetcore.api.manager.MetaManager.setMeta
import cn.inrhor.imipetcore.api.manager.ModelManager.clearModel
import cn.inrhor.imipetcore.api.manager.ModelManager.display
import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.option.StateOption
import cn.inrhor.imipetcore.common.script.kether.evalStrPetData
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
class PetEntity(val owner: Player, val petData: PetData) {

    /**
     * 实体
     */
    var entity: LivingEntity? = null

    /**
     * 释放宠物
     */
    fun spawn() {
        if (petData.isDead()) {
            owner.sendLang("PET-IS-DEAD")
            return
        }
        if (entity != null) return
        petData.following = true
        entity = owner.world.spawnEntity(owner.location, petData.petOption().entityType) as LivingEntity
        entity?.setMeta("entity", petData.name)
        entity?.setMeta("owner", owner.uniqueId)
        initAction()
        updateModel(true)
    }

    /**
     * 初始化行为
     */
    fun initAction() {
        entity?.clearGoalAi()
        entity?.clearTargetAi()
        petData.petOption().action.forEach {
            val id = it.id
            val pri = it.priority
            entity?.addAi(this@PetEntity, id, pri)
        }
    }

    /**
     * 删除宠物
     */
    fun back() {
        petData.following = false
        if (petData.isDead()) return
        entity?.clearModel(model().select)
        entity?.remove()
        entity = null
    }

    /**
     * 更新宠物模型
     */
    fun updateModel(init: Boolean = false) {
        val name = owner.evalStrPetData(petData.petOption().default.displayName, petData)
        val model = model()
        val modelID = model.id
        if (!petData.isFollow()) return
        entity?.display(modelID, init, name, model.select)
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