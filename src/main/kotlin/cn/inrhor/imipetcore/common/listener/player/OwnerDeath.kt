package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.SubscribeEvent

object OwnerDeath {

    @SubscribeEvent
    fun death(ev: PlayerDeathEvent) {
        ev.entity.followingPet().forEach {
            it.back()
        }
    }

}