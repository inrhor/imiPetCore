package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.PetManager.getMeta
import cn.inrhor.imipetcore.api.manager.PetManager.getOwner
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * 宠物受到伤害
 */
object PetBeHurt {

    @SubscribeEvent
    fun e(ev: EntityDamageByEntityEvent) {
        val pet = ev.entity
        val owner = pet.getOwner()?: return
        val p = pet.getMeta("entity")?: return
        val name = p.asString()
        owner.delCurrentHP(owner.getPet(name), ev.damage)
    }

}