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
                if (it.isFollow()) ev.player.callPet(it.name)
            }
        }
    }

    @SubscribeEvent
    fun quit(ev: PlayerQuitEvent) {
        ev.player.getData().petDataList.forEach {
            if (it.isFollow()) ev.player.callPet(it.name, false)
        }
    }

}