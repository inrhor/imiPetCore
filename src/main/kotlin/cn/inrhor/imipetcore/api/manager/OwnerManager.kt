package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object OwnerManager {

    /**
     * 设置主人被攻击来源实体
     */
    fun Player.setDamageByEntity(entity: Entity?) {
        getData().commonData.attacker = entity
    }

    /**
     * 获取主人被攻击来源实体
     */
    fun Player.getDamageByEntity(): Entity? {
        val en = getData().commonData.attacker
        return if (en?.isDead == false) {
            en
        }else {
            getData().commonData.attacker = null
            null
        }
    }

}