package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * 宠物受到伤害
 */
object PetBeHurt {

    @SubscribeEvent
    fun e(ev: EntityDamageByEntityEvent) {
        val entity = ev.entity
        val d = ev.damager
        val owner = entity.getOwner()?: return
        if (d is Player && owner == d) return
        val petData = entity.getPetData(owner)?: return
        owner.delCurrentHP(petData, ev.damage)
    }

}