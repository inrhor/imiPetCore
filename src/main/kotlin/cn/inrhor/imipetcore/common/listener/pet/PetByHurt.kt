package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.api.manager.MetaManager.isPet
import cn.inrhor.imipetcore.api.manager.PetManager.isInvulnerablePlayer
import cn.inrhor.imipetcore.api.manager.PetManager.setByHurtEntity
import com.sucy.skill.api.event.SkillDamageEvent
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * 宠物受到伤害
 */
object PetByHurt {

    /**
     * 受到攻击
     */
    @SubscribeEvent
    fun damage(ev: EntityDamageByEntityEvent) {
        val entity = ev.entity
        val damager = ev.damager
        val owner = entity.getOwner()?: return
        val petData = entity.getPetData(owner)?: return
        if (damager is Player) {
            // 攻击者不能是主人
            if (damager == owner) {
                ev.isCancelled = true
                return
            }
            // 免疫玩家伤害组件
            if (petData.isInvulnerablePlayer()) {
                ev.isCancelled = true
                return
            }
        }
        // 抛物攻击
        if (ev.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            val shooter = (ev.damager as Projectile).shooter
            if (shooter is Player) {
                if (shooter == owner) {
                    ev.isCancelled = true
                    return
                }
                if (petData.isInvulnerablePlayer()) {
                    ev.isCancelled = true
                    return
                }
            }
        }
        owner.delCurrentHP(petData, ev.damage)
        petData.petEntity?.setByHurtEntity(damager)
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