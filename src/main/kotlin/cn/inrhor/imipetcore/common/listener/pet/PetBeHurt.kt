package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.MetaManager.getMeta
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
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
        owner.delCurrentHP(owner.getPet(p.toString()), ev.damage)
    }

}