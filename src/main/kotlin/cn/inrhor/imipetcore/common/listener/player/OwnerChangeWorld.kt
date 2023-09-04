package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import cn.inrhor.imipetcore.server.ReadManager.major
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.world.ChunkUnloadEvent
import taboolib.common.platform.event.SubscribeEvent

object OwnerChangeWorld {

    @SubscribeEvent
    fun e(ev: PlayerChangedWorldEvent) {
        val p = ev.player
        resetFollow(p)
    }

    @SubscribeEvent
    fun tp(ev: PlayerTeleportEvent) {
        if (major <= 4) return
        val p = ev.player
        resetFollow(p)
    }

    @SubscribeEvent
    fun chunk(ev: ChunkUnloadEvent) {
        ev.chunk.entities.forEach {
            if (it.hasMetadata("imipetcore_entity")) {
                (it as LivingEntity).setAI(true)
            }
        }
    }

    private fun resetFollow(p: Player) {
        p.followingPet().forEach {
            it.back()
            it.spawn()
        }
    }

}