package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import org.bukkit.event.player.PlayerChangedWorldEvent
import taboolib.common.platform.event.SubscribeEvent

object OwnerChangeWorld {

    @SubscribeEvent
    fun e(ev: PlayerChangedWorldEvent) {
        val p = ev.player
        p.followingPet().forEach {
            it.back()
            it.spawn()
        }
    }

}