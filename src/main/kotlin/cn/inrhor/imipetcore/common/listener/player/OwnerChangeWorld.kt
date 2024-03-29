package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.event.PetTeleportEvent
import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import cn.inrhor.imipetcore.server.ReadManager
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.world.ChunkUnloadEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object OwnerChangeWorld {

    @SubscribeEvent
    fun e(ev: PlayerChangedWorldEvent) {
        val p = ev.player
        resetFollow(p)
    }

    @SubscribeEvent
    fun tp(ev: PlayerTeleportEvent) {
        if (ReadManager.major > 4) return
        val p = ev.player
        submit(delay = 10L) {
            val list = p.followingPet()
            list.forEach {
                it.back()
            }
            list.forEach {
                it.spawn()
                PetTeleportEvent(it).call()
            }
        }
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
        val list = p.followingPet()
        list.forEach {
            it.back()
        }
        submit(delay = 15L) {
            list.forEach {
                it.spawn()
                PetTeleportEvent(it).call()
            }
        }
    }

}