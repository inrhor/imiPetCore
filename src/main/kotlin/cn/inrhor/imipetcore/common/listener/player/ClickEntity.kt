package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.event.OwnerRightClickPet
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.common.option.TriggerOption
import org.bukkit.event.player.PlayerInteractEntityEvent
import taboolib.common.platform.event.SubscribeEvent

object ClickEntity {

    @SubscribeEvent
    fun right(ev: PlayerInteractEntityEvent) {
        val entity = ev.rightClicked
        val owner = entity.getOwner()?: return
        if (owner != ev.player) return
        val petData = entity.getPetData(owner)?: return
        OwnerRightClickPet(owner, petData).call()
    }

    @SubscribeEvent
    fun trigger(ev: OwnerRightClickPet) {
        val petData = ev.petData
        petData.petOption().trigger.forEach {
            if (it.type == TriggerOption.Type.OWNER_RIGHT_CLICK) {
                it.runScript(ev.player, petData)
            }
        }
    }

}