package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer
import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.event.*
import cn.inrhor.imipetcore.api.manager.AttributeManager.loadAttributeData
import cn.inrhor.imipetcore.api.manager.MetaManager.setMeta
import cn.inrhor.imipetcore.api.manager.ModelManager.delDriveRide
import cn.inrhor.imipetcore.api.manager.ModelManager.driveRide
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption
import cn.inrhor.imipetcore.common.database.Database.Companion.database
import cn.inrhor.imipetcore.common.database.data.*
import cn.inrhor.imipetcore.common.option.TriggerOption
import cn.inrhor.imipetcore.common.option.trigger
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

/**
 * 宠物管理器
 */
object PetManager {

    /**
     * 添加新的宠物数据
     *
     * @param name 宠物名称，不可重复
     * @param id 宠物ID
     * @param following 跟随与否，默认为false
     */
    fun Player.addPet(name: String, id: String, following: Boolean = false) {
        if (existPetName(name)) {
            // lang
            return
        }
        val opt = id.petOption()
        val def = opt.default
        val a = def.attribute
        val p = a.health
        val petData = PetData(name, id, following,
            AttributeData(p, p, a.speed, a.attack), 0, def.exp,
            skillSystemData = SkillSystemData(opt.skill.number))
        addPet(petData)
        if (following) callPet(name)
    }

    /**
     * 添加宠物数据
     *
     * @param petData
     */
    fun Player.addPet(petData: PetData) {
        getData().petDataList.add(petData)
        database.createPet(uniqueId, petData)
        ReceivePetEvent(this, petData)
    }

    /**
     * @return 存在重复名称宠物
     */
    fun Player.existPetName(name: String): Boolean {
        getData().petDataList.forEach {
            if (it.name == name) return true
        }
        return false
    }

    /**
     * 删除宠物
     * @param name 宠物唯一名称
     */
    fun Player.deletePet(name: String) {
        val list = getData().petDataList.iterator()
        while (list.hasNext()) {
            val pet = list.next()
            if (pet.name == name) {
                pet.petEntity?.entity?.remove()
                list.remove(); break
            }
        }
        database.deletePet(uniqueId, name)
    }

    /**
     * @return 返回宠物数据
     */
    fun Player.getPet(name: String): PetData {
        getData().petDataList.forEach {
            if (name == it.name) return it
        }
        error("null pet name")
    }

    /**
     * @param name 宠物唯一名称
     *
     * @return 宠物实体
     */
    fun Player.petEntity(name: String): PetEntity {
        val petData = getPet(name)
        if (petData.petEntity == null) petData.petEntity = PetEntity(this, petData)
        return petData.petEntity?: error("error init entity")
    }

    fun Player.followingPet(): List<PetEntity> {
        val list = mutableListOf<PetEntity>()
        followingPetData().forEach {
            it.petEntity?.let { pet -> list.add(pet) }
        }
        return list
    }

    fun Player.followingPetData(): List<PetData> {
        val list = mutableListOf<PetData>()
        getData().petDataList.forEach {
            if (it.isFollow()) list.add(it)
        }
        return list
    }

    /**
     * 宠物跟随
     *
     * @param following 跟随（默认true)
     */
    fun Player.callPet(name: String, following: Boolean = true) {
        if (following) {
            petEntity(name).spawn()
        }else {
            petEntity(name).back()
        }
        val petData = getPet(name)
        database.updatePet(uniqueId, petData)
        FollowPetEvent(this, petData, following).call()
    }

    /**
     * 宠物跟随
     *
     * @param following 跟随（默认true)
     */
    fun Player.callPet(petData: PetData, following: Boolean = true) {
        callPet(petData.name, following)
    }

    /**
     * 重命名宠物名称
     */
    fun Player.renamePet(petData: PetData, newName: String) {
        val old = petData.name
        if (existPetName(newName)) {
            return
        }
        petData.name = newName
        petData.petEntity?.entity?.setMeta("entity", newName)
        database.renamePet(uniqueId, old, petData)
    }

    /**
     * 修改宠物ID
     */
    fun Player.changePetId(petData: PetData, newID: String) {
        if (!DataContainer.petOptionMap.contains(newID)) return
        petData.id = newID
        database.changePetID(uniqueId, petData)
        val en = petData.petEntity?: return
        if (petData.isFollow()) {
            en.back()
            en.spawn()
        }
    }

    /**
     * 扣除宠物当前血量
     */
    fun Player.delCurrentHP(petData: PetData, double: Double) {
        val attribute = petData.attribute
        attribute.currentHP -= double
        val entity = petData.petEntity?.entity?: return
        if (attribute.currentHP <= 0) {
            attribute.currentHP = 0.0
            entity.remove()
            petData.petEntity?.entity = null
            PetDeathEvent(this, petData).call()
        }
        PetChangeEvent(this, petData).call()
    }

    /**
     * 玩家乘骑宠物
     */
    fun Player.driveRidePet(petData: PetData, actionType: ModelManager.ActionType) {
        val select = petData.petOption().model.select
        petData.petEntity?.entity?.driveRide(this, select, actionType)
    }

    /**
     * 玩家乘骑宠物
     */
    fun Player.driveRidePet(petData: PetData, actionType: String) {
        driveRidePet(petData, ModelManager.ActionType.valueOf(actionType.uppercase()))
    }

    /**
     * 玩家取消乘骑宠物
     */
    fun Player.unDriveRidePet(petData: PetData) {
        val select = petData.petOption().model.select
        petData.petEntity?.entity?.delDriveRide(select)
    }

    /**
     * 增加宠物当前血量
     */
    fun Player.addCurrentHP(petData: PetData, double: Double) {
        val attribute = petData.attribute
        attribute.currentHP += double
        val max = attribute.maxHP
        if (attribute.currentHP > max) attribute.currentHP = max
        PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物当前血量
     */
    fun Player.setCurrentHP(petData: PetData, value: Double = petData.attribute.currentHP, call: Boolean = true) {
        val attribute = petData.attribute
        attribute.currentHP = value
        petData.petEntity?.entity?.health = value
        if (call) PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物最大血量
     */
    fun Player.setMaxHP(petData: PetData, value: Double = petData.attribute.maxHP, call: Boolean = true) {
        val attribute = petData.attribute
        attribute.maxHP = value
        petData.petEntity?.entity?.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = value
        if (call) PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物攻击属性
     */
    fun Player.setPetAttack(petData: PetData, attack: Double = petData.attribute.attack, call: Boolean = true) {
        val attribute = petData.attribute
        attribute.attack = attack
        petData.petEntity?.entity?.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = attack
        if (call) PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物行走速度
     */
    fun Player.setPetSpeed(petData: PetData, speed: Double = petData.attribute.speed, call: Boolean = true) {
        val attribute = petData.attribute
        attribute.speed = speed
        val entity = petData.petEntity?.entity
        entity?.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = speed/10
        entity?.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.baseValue = speed/10
        if (call) PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物当前经验
     */
    fun Player.setCurrentExp(petData: PetData,value: Int) {
        if (value >= petData.maxExp) {
            petData.currentExp = 0
            petData.addLevel(this, 1)
        }else petData.currentExp = value
        PetChangeEvent(this, petData).call()
    }

    /**
     * 宠物升级
     */
    fun PetData.addLevel(player: Player, int: Int) {
        val max = petOption().default.level
        if (level >= max) return
        level = if (level+int > max) max else level+int
        trigger(player, TriggerOption.Type.LEVEL_UP)
        PetLevelEvent(player, this, int, level).call()
    }

    /**
     * 设置宠物最大经验
     */
    fun Player.setMaxExp(petData: PetData, value: Int) {
        petData.maxExp = value
        PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物当前等级
     */
    fun Player.setLevel(petData: PetData,value: Int) {
        petData.level = value
        PetChangeEvent(this, petData).call()
    }

    /**
     * @return 宠物是否有乘客
     */
    fun PetData.hasPassenger(): Boolean {
        val entity = petEntity?.entity?: return false
        return entity.passengers.isNotEmpty()
    }

    /**
     * 操作宠物挂钩属性数据
     *
     * @param effect 是否影响实体本身
     */
    fun PetData.hookAttribute(player: Player, operateType: OperateType, type: HookAttribute, key: String, value: String, effect: Boolean = true) {
        when (type) {
            HookAttribute.ATTRIBUTE_PLUS -> {
                when (operateType) {
                    OperateType.REMOVE -> {
                        attribute.hook.removeIf { it.type == type && it.key == key }
                    }
                    OperateType.SET -> {
                        attribute.hook.removeIf { it.type == type && it.key == key }
                        attribute.hook.add(AttributeHookData(type, key, value))
                    }
                    else -> {}
                }
                if (effect) {
                    petEntity?.entity?.loadAttributeData(this)
                }
                PetChangeEvent(player, this).call()
            }
        }
    }

    enum class OperateType {
        SET, REMOVE, GET
    }

}