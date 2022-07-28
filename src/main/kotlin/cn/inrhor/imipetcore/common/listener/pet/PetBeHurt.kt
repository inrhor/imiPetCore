package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.PetManager.delHp
import cn.inrhor.imipetcore.api.manager.PetManager.getMeta
import cn.inrhor.imipetcore.api.manager.PetManager.getOwner
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

/**
 * 宠物受到伤害
 */
object PetBeHurt {

    @SubscribeEvent
    fun e(ev: EntityDamageByEntityEvent) {
        val pet = ev.entity
        val owner = pet.getOwner()?: return
        val p = pet.getMeta("entity")?: return
        val pUUID = UUID.fromString(p.asString())
        pUUID.delHp(owner, ev.damage)
    }

}