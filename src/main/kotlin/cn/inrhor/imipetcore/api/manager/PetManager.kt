package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.event.FollowPetEvent
import cn.inrhor.imipetcore.api.event.ReceivePetEvent
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption
import cn.inrhor.imipetcore.common.database.Database.Companion.database
import cn.inrhor.imipetcore.common.database.data.AttributeData
import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player
import java.util.*

/**
 * 宠物管理器
 */
object PetManager {

    /**
     * 添加新的宠物数据
     *
     * @param pUUID 宠物UUID
     * @param id 宠物ID
     * @param following 跟随与否，默认为false
     */
    fun Player.addPet(pUUID: UUID = UUID.randomUUID(), id: String, following: Boolean = false) {
        val a = id.petOption().default.attribute
        val p = a.health
        val petData = PetData(pUUID.toString(), id, following, AttributeData(p, p, a.speed, a.attack))
        addPet(petData)
        if (following) callPet(petData.uniqueId())
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
     * 删除宠物
     * @param pUUID 宠物UUID
     */
    fun Player.deletePet(pUUID: UUID) {
        val list = getData().petDataList.iterator()
        while (list.hasNext()) {
            if (list.next().uniqueId() == pUUID) {
                list.remove(); break
            }
        }
        database.deletePet(uniqueId, pUUID)
    }

    /**
     * @param pUUID 宠物UUID
     *
     * @return 返回宠物数据
     */
    fun Player.getPet(pUUID: UUID): PetData {
        getData().petDataList.forEach {
            if (pUUID == it.uniqueId()) return it
        }
        error("null pet uuid")
    }

    /**
     * @param pUUID 宠物UUID
     *
     * @return 宠物实体
     */
    fun Player.petEntity(pUUID: UUID): PetEntity {
        val petData = getPet(pUUID)
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
    fun Player.callPet(pUUID: UUID, following: Boolean = true) {
        if (following) {
            petEntity(pUUID).spawn()
        }else {
            petEntity(pUUID).back()
        }
        val petData = getPet(pUUID)
        database.updatePet(uniqueId, petData)
        FollowPetEvent(this, petData, following).call()
    }

}