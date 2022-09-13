package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.event.ReceivePetEvent
import cn.inrhor.imipetcore.common.option.TriggerOption
import cn.inrhor.imipetcore.common.option.trigger
import taboolib.common.platform.event.SubscribeEvent

object ReceivePet {

    @SubscribeEvent
    fun e(ev: ReceivePetEvent) {
        ev.petData.trigger(ev.player, TriggerOption.Type.RECEIVE_PET)
    }

}