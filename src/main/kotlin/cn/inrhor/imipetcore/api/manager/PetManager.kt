package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.event.*
import cn.inrhor.imipetcore.api.manager.MetaManager.setMeta
import cn.inrhor.imipetcore.api.manager.ModelManager.delDriveRide
import cn.inrhor.imipetcore.api.manager.ModelManager.driveRide
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption
import cn.inrhor.imipetcore.common.database.Database.Companion.database
import cn.inrhor.imipetcore.common.database.data.AttributeData
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.option.TriggerOption
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
        val def = id.petOption().default
        val a = def.attribute
        val p = a.health
        val petData = PetData(name, id, following,
            AttributeData(p, p, a.speed, a.attack, a.attack_speed), 0, def.exp)
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
        getData().petDataList.forEach {
            if (it.isFollow()) list.add(it.petEntity!!)
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
    fun Player.setCurrentHP(petData: PetData, value: Double = petData.attribute.currentHP, effect: Boolean = false, call: Boolean = true) {
        val attribute = petData.attribute
        attribute.currentHP = value
        if (effect) petData.petEntity?.entity?.health = value
        if (call) PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物最大血量
     */
    fun Player.setMaxHP(petData: PetData, value: Double = petData.attribute.maxHP, effect: Boolean = false, call: Boolean = true) {
        val attribute = petData.attribute
        attribute.maxHP = value
        if (effect) petData.petEntity?.entity?.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = value
        if (call) PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物攻击属性
     */
    fun Player.setPetAttack(petData: PetData,attack: Double) {
        val attribute = petData.attribute
        attribute.attack = attack
        PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物行走速度
     */
    fun Player.setPetSpeed(petData: PetData,speed: Double) {
        val attribute = petData.attribute
        attribute.speed = speed
        PetChangeEvent(this, petData).call()
    }

    /**
     * 设置宠物攻击速度
     */
    fun Player.setPetAttackSpeed(petData: PetData,attack_speed: Int) {
        val attribute = petData.attribute
        attribute.attack_speed = attack_speed
        PetChangeEvent(this, petData).call()
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
        petOption().trigger.forEach {
            if (it.type == TriggerOption.Type.LEVEL_UP) {
                it.runScript(player, this)
            }
        }
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

}