package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.event.UnDriveRidePet
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import org.spigotmc.event.entity.EntityDismountEvent
import taboolib.common.platform.event.SubscribeEvent

object DrivePet {

    @SubscribeEvent
    fun unDrive(ev: EntityDismountEvent) {
        val en = ev.dismounted
        if (en.hasMetadata("imipetcore_drive")) {
            val player = en.getOwner()?: return
            val petData = en.getPetData(player)?: return
            if (player == ev.entity) {
                UnDriveRidePet(player, petData).call()
            }
        }
    }

}