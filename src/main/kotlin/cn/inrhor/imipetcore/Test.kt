package cn.inrhor.imipetcore

import cn.inrhor.imipetcore.api.manager.PetManager.addPet
import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

/**
 * debug
 */
object Test {


    @SubscribeEvent
    fun test(ev: BlockBreakEvent) {
        val p = ev.player
        info("eeee")
        p.addPet(id = "test", following = true)
    }

    @SubscribeEvent
    fun attack(ev: EntityDamageByEntityEvent) {
        if (ev.damager !is Player) return
        info(ev.damager.name)
        val player = ev.damager as Player

        player.followingPet().forEach {
            it.state.updateState(it, "attack", ev.entity)
        }
    }

}