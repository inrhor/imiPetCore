package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.event.AttributeChangePetEvent
import cn.inrhor.imipetcore.api.event.FollowPetEvent
import cn.inrhor.imipetcore.api.event.PetDeathEvent
import cn.inrhor.imipetcore.api.event.ReceivePetEvent
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption
import cn.inrhor.imipetcore.common.database.Database.Companion.database
import cn.inrhor.imipetcore.common.database.data.AttributeData
import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import java.util.*

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
        val petData = PetData(name, id, following, AttributeData(p, p, a.speed, a.attack))
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
        database.updatePet(uniqueId, petData)
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
            if (it.following && it.petEntity != null) list.add(it.petEntity!!)
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
        petData.name = newName
    }

    fun Entity.setMeta(meta: String, obj: Any) {
        setMetadata("imipetcore:$meta", FixedMetadataValue(ImiPetCore.plugin, obj))
    }

    /**
     * @return 有否标签 imipetcore:"meta"
     */
    fun Entity.hasMeta(meta: String): Boolean {
        return hasMetadata("imipetcore:$meta")
    }

    /**
     * @return 标签数值
     */
    fun Entity.getMeta(meta: String): MetadataValue? {
        return getMetadata("imipetcore:$meta")[0]
    }

    /**
     * @return 主人
     */
    fun Entity.getOwner(): Player? {
        return Bukkit.getPlayer(UUID.fromString(getMeta("owner")?.asString()))
    }

    /**
     * 扣除宠物当前血量
     */
    fun String.delCurrentHP(owner: Player, double: Double) {
        val petData = owner.getPet(this)
        val attribute = petData.attribute
        attribute.currentHP -= double
        val entity = petData.petEntity?.entity ?: return
        if (attribute.currentHP <= 0) {
            entity.remove()
            PetDeathEvent(owner, petData).call()
        }
        AttributeChangePetEvent(owner, petData).call()
    }

    /**
     * 增加宠物当前血量
     */
    fun String.addCurrentHP(owner: Player, double: Double) {
        val petData = owner.getPet(this)
        val attribute = petData.attribute
        attribute.currentHP += double
        val max = attribute.maxHP
        if (attribute.currentHP > max) attribute.currentHP = max
        AttributeChangePetEvent(owner, petData).call()
    }

}