package cn.inrhor.imipetcore.common.listener

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.manager.PetManager.callPet
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * 玩家进退监听
 */
object JoinQuit {

    @SubscribeEvent
    fun join(ev: PlayerJoinEvent) {
        submit(delay = 5L) {
            ev.player.getData().petDataList.forEach {
                if (it.following) ev.player.callPet(it.uniqueId())
            }
        }
    }

    @SubscribeEvent
    fun quit(ev: PlayerQuitEvent) {
        ev.player.getData().petDataList.forEach {
            if (it.following) ev.player.callPet(it.uniqueId(), false)
        }
    }

}