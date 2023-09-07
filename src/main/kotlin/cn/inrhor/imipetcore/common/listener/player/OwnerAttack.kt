package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.entity.ai.Controller.attackEntity
import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.OwnerManager.setDamageByEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * 主人攻击目标，宠物参与战斗
 */
object OwnerAttack {

    @SubscribeEvent(EventPriority.HIGH)
    fun e(ev: EntityDamageByEntityEvent) {
        if (ev.damager !is Player) return
        val player = ev.damager as Player
        val target = ev.entity
        if (target.getOwner() == player) {
            ev.isCancelled = true
            return
        }
        player.setDamageByEntity(target)
        player.followingPet().forEach {
            it.entity?.attackEntity(target, false)
        }
    }

}