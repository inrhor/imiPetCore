package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.api.manager.MetaManager.isPet
import cn.inrhor.imipetcore.api.manager.PetManager.isInvulnerablePlayer
import cn.inrhor.imipetcore.api.manager.PetManager.setByHurtEntity
import com.sucy.skill.api.event.SkillDamageEvent
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * 宠物受到伤害
 */
object PetByHurt {

    @SubscribeEvent
    fun e(ev: EntityDamageByEntityEvent) {
        val entity = ev.entity
        val d = ev.damager
        val owner = entity.getOwner()?: return
        if (d is Player && owner == d) return
        val petData = entity.getPetData(owner)?: return
        if (petData.isInvulnerablePlayer()) {
            ev.isCancelled = true
            return
        }
        owner.delCurrentHP(petData, ev.damage)
        petData.petEntity?.setByHurtEntity(d)
    }

    @SubscribeEvent(bind = "com.sucy.skill.api.event.SkillDamageEvent")
    fun proSkillDamage(op: OptionalEvent) {
        val ev = op.get<SkillDamageEvent>()
        val attacker = ev.damager
        val entity = ev.target?: return
        if (entity.isPet()) {
            if (attacker is Player) ev.isCancelled = true
        }
    }

}