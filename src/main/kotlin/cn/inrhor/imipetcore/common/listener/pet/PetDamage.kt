package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.event.PetDeathEvent
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.api.manager.PetManager.setCurrentHP
import cn.inrhor.imipetcore.common.option.TriggerOption
import cn.inrhor.imipetcore.common.option.trigger
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import taboolib.common.platform.event.SubscribeEvent


object PetDamage {

    @SubscribeEvent
    fun suffocation(ev: EntityDamageEvent) {
        if (ev.isCancelled) return
        val entity = ev.entity
        if (ev.cause == EntityDamageEvent.DamageCause.SUFFOCATION || ev.cause == EntityDamageEvent.DamageCause.FALL) {
            if (entity.hasMetadata("imipetcore_entity")) {
                ev.isCancelled = true
            }
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
        ev.drops.clear()
        ev.droppedExp = 0
       PetDeathEvent(owner, petData).call()
    }

    @SubscribeEvent
    fun petDeath(ev: PetDeathEvent) {
        val petData = ev.petData
        val player = ev.player
        player.setCurrentHP(petData, 0.0)
        petData.trigger(player, TriggerOption.Type.DEATH)
    }

}