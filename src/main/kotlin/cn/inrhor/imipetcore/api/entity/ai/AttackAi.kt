package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.MetaManager.metaEntity
import cn.inrhor.imipetcore.api.manager.MetaManager.removeMeta
import cn.inrhor.imipetcore.common.location.distanceLoc
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.navigationMove

data class AttackAi(val petEntity: PetEntity, val action: String = "attack", var delay: Int = 0): SimpleAi() {

    /**
     * 检测
     * true -> startTask
     */
    override fun shouldExecute(): Boolean {
        val target = petEntity.entity?.metaEntity("target")?: return false
        return !target.isDead && petEntity.petData.attribute.attack > 0 &&
                ( petEntity.entity?.distanceLoc(petEntity.owner)?: 100.0) <= 20.0
    }

    /**
     * 向目标移动并攻击，播放攻击动作(Model)
     */
    override fun startTask() {
        delay = petEntity.petData.attribute.attack_speed*20
    }

    override fun continueExecute(): Boolean {
        val target = petEntity.entity?.metaEntity("target")?: return false
        return (petEntity.entity?.distanceLoc(petEntity.owner)?: 20.0) < 10.0 && !target.isDead
    }

    override fun updateTask() {
        val entity = petEntity.entity?: return
        val attribute = petEntity.petData.attribute
        val target = petEntity.entity?.metaEntity("target")?: return
        entity.navigationMove(target.location, attribute.speed)
        if (entity.distanceLoc(target)<= 3.0) {
            if (petEntity.petData.attribute.attack_speed*20 == delay) {
                (target as LivingEntity).damage(attribute.attack, entity)
                val attackOption = petEntity.getStateOption(action)
                if (attackOption != null) {
                    val active = petEntity.modelEntity?.getActiveModel(petEntity.petData.petOption().model.id)
                    active?.addState(action, attackOption.lerpin, attackOption.lerpout, attackOption.speed)
                }
            }else if (delay == 0) {
                delay = petEntity.petData.attribute.attack_speed*20
                return
            }
            delay--
        }
    }

    override fun resetTask() {
        petEntity.entity?.removeMeta("target")
    }

}