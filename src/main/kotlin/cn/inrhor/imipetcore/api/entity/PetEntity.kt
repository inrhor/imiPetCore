package cn.inrhor.imipetcore.api.entity

import cn.inrhor.imipetcore.api.entity.ai.addAi
import cn.inrhor.imipetcore.api.manager.AttributeManager.loadAttributeData
import cn.inrhor.imipetcore.api.manager.DisguiseManager.disguise
import cn.inrhor.imipetcore.api.manager.MetaManager.setMeta
import cn.inrhor.imipetcore.api.manager.ModelManager.clearModel
import cn.inrhor.imipetcore.api.manager.ModelManager.display
import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.api.manager.PetManager.setCurrentHP
import cn.inrhor.imipetcore.api.manager.PetManager.setMaxHP
import cn.inrhor.imipetcore.api.manager.PetManager.setPetAttack
import cn.inrhor.imipetcore.api.manager.PetManager.setPetSpeed
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.model.ModelSelect
import cn.inrhor.imipetcore.common.option.AddonSelect
import cn.inrhor.imipetcore.common.option.AddonType
import cn.inrhor.imipetcore.common.option.StateOption
import cn.inrhor.imipetcore.common.script.kether.evalStrPetData
import cn.inrhor.imipetcore.server.ReadManager.adyeshachLoad
import cn.inrhor.imipetcore.server.ReadManager.decentHologramsLoad
import eu.decentsoftware.holograms.api.DHAPI
import ink.ptms.adyeshach.api.AdyeshachAPI
import org.bukkit.entity.*
import taboolib.common.platform.function.submit
import taboolib.common5.Baffle
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
     * 强制播放模型动作
     */
    var actionBaffle: Baffle? = null

    /**
     * 受到实体伤害
     */
    var hurtEntity: Entity? = null

    /**
     * 召唤宠物
     */
    fun spawn() {
        if (petData.isDead()) {
            owner.sendLang("PET_IS_DEAD")
            return
        }
        if (entity != null) return
        petData.following = true
        entity = owner.world.spawnEntity(owner.location, EntityType.WOLF) as LivingEntity
        entity?.setMeta("entity", petData.name)
        val wolf = entity as Wolf
        wolf.isTamed = true
        // 静音
        wolf.isSilent = true
        wolf.owner = owner
        // 远离玩家后不消失
        wolf.removeWhenFarAway = false
        initAttribute()
        initAction()
        updateModel(true)
        initAddon()
    }

    /**
     * 初始化属性
     */
    fun initAttribute() {
        owner.setMaxHP(petData, call = false)
        owner.setCurrentHP(petData, call = false)
        owner.setPetSpeed(petData, call = false)
        owner.setPetAttack(petData, call = false)

        entity?.loadAttributeData(petData)
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
    fun back(update: Boolean = true) {
        if (update) petData.following = false
        if (petData.isDead()) return
        entity?.clearModel(model().select)
        entity?.remove()
        entity = null
    }

    /**
     * 初始化加载Addon组件
     */
    fun initAddon() {
        entity?: return
        val petOption = petData.petOption()
        petOption.addon.forEach {
            when (it.type) {
                AddonType.NAME -> {
                    val list = owner.evalStrPetData(it.lines, petData)
                    val loc = entity!!.location.clone().add(0.0, it.height, 0.0)
                    when (it.select) {
                        AddonSelect.ADYESHACH -> {
                            if (!adyeshachLoad) return
                            val holo = AdyeshachAPI.createHologram(loc, list)
                            submit(async = true, period = 1L) {
                                if (entity?.isDead == true || entity == null) {
                                    holo.delete()
                                    cancel()
                                    return@submit
                                }
                                if (entity?.hasMetadata("imipetcore_drive") == true) {
                                    holo.update(emptyList())
                                    return@submit
                                }
                                holo.update(list)
                                holo.teleport(entity!!.location.clone().add(0.0, it.height, 0.0))
                            }
                        }
                        AddonSelect.DECENT_HOLOGRAMS -> {
                            if (!decentHologramsLoad) return
                            val dha = DHAPI.createHologram(entity!!.uniqueId.toString(), loc, list)
                            submit(async = true, period = 1L) {
                                if (entity?.isDead == true || entity == null) {
                                    dha.destroy()
                                    cancel()
                                    return@submit
                                }
                                if (entity?.hasMetadata("imipetcore_drive") == true) {
                                    DHAPI.setHologramLines(dha, emptyList())
                                    return@submit
                                }
                                DHAPI.setHologramLines(dha, list)
                                DHAPI.moveHologram(dha, entity!!.location.clone().add(0.0, it.height, 0.0))
                            }
                        }
                        else -> {}
                    }
                }
                else -> {}
            }
        }
    }

    /**
     * 更新宠物模型
     */
    fun updateModel(init: Boolean = false) {
        val entityType = petData.petOption().entityType
        if (model().select == ModelSelect.COMMON) {
            submit(delay = 5L) {
                entity?.disguise(entityType)
            }
        }else {
            val model = model()
            val modelID = model.id
            if (!petData.isFollow()) return
            entity?.display(modelID, init, model.select)
        }
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