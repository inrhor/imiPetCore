package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.event.FollowPetEvent
import cn.inrhor.imipetcore.common.option.TriggerOption
import cn.inrhor.imipetcore.common.option.trigger
import taboolib.common.platform.event.SubscribeEvent

object FollowListener {

    @SubscribeEvent
    fun e(ev: FollowPetEvent) {
        val f = if (ev.follow) TriggerOption.Type.FOLLOW else TriggerOption.Type.UNFOLLOW
        ev.petData.trigger(ev.player, f)
    }

}