package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.manager.PetManager.callPet
import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import cn.inrhor.imipetcore.common.database.Database
import cn.inrhor.imipetcore.server.PluginLoader.authMeLoad
import fr.xephi.authme.events.LoginEvent
import fr.xephi.authme.events.RegisterEvent
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * 玩家进退监听
 */
object JoinQuit {

    @SubscribeEvent
    fun join(ev: PlayerJoinEvent) {
        if (!authMeLoad) {
            spawnPet(ev.player)
        }
    }

    private fun spawnPet(player: Player) {
        Database.database.pull(player.uniqueId)
        submit(delay = 10L) {
            player.getData().petDataList.forEach {
                if (it.isFollow()) player.callPet(it.name)
            }
        }
    }

    @SubscribeEvent(bind = "fr.xephi.authme.events.LoginEvent")
    fun login(op: OptionalEvent) {
        val ev = op.get<LoginEvent>()
        spawnPet(ev.player)
    }

    @SubscribeEvent(bind = "fr.xephi.authme.events.RegisterEvent")
    fun register(op: OptionalEvent) {
        val ev = op.get<RegisterEvent>()
        spawnPet(ev.player)
    }

    @SubscribeEvent
    fun quit(ev: PlayerQuitEvent) {
        ev.player.followingPet().forEach {
            it.back(false)
        }
    }

}