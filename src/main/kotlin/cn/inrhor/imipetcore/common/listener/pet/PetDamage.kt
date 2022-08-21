package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.event.PetDeathEvent
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.PetManager.setCurrentHP
import cn.inrhor.imipetcore.common.option.TriggerOption
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import taboolib.common.platform.event.SubscribeEvent

object PetDamage {

    @SubscribeEvent
    fun damage(ev: EntityDamageEvent) {
        val entity = ev.entity
        if (ev.cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if (entity.hasMetadata("imipetcore_entity")) {
                ev.isCancelled = true
            }
        }else {
            val owner = entity.getOwner()?: return
            val petData = entity.getPetData(owner)?: return
            if (ev.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return
            owner.delCurrentHP(petData, ev.damage)
        }
    }

    @SubscribeEvent
    fun fire(ev: EntityCombustEvent) {
        val entity = ev.entity
        if (entity.hasMetadata("imipetcore_entity")) {
            ev.isCancelled = true
        }
    }

    @SubscribeEvent
    fun death(ev: EntityDeathEvent) {
        val entity = ev.entity
        val owner = entity.getOwner()?: return
        val petData = entity.getPetData(owner)?: return
        PetDeathEvent(owner, petData).call()
    }

    @SubscribeEvent
    fun petDeath(ev: PetDeathEvent) {
        val petData = ev.petData
        val player = ev.player
        player.setCurrentHP(petData, 0.0)
        petData.petOption().trigger.forEach {
            if (it.type == TriggerOption.Type.DEATH) {
                it.runScript(player, petData)
            }
        }
    }

}