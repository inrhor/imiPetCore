package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.MetaManager.metaEntity
import cn.inrhor.imipetcore.api.manager.MetaManager.removeMeta
import cn.inrhor.imipetcore.api.manager.ModelManager.playAnimation
import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.common.location.distanceLoc
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.navigationMove

class UniversalAiAttack(val petEntity: PetEntity, val action: String = "attack", var delay: Int = 0): UniversalAi() {

    override fun shouldExecute(): Boolean {
        val target = petEntity.entity?.metaEntity("target")?: return false
        return !target.isDead && petEntity.petData.attribute.attack > 0 &&
                ( petEntity.entity?.distanceLoc(petEntity.owner)?: 100.0) <= 20.0
    }

    override fun startTask() {
        delay = petEntity.petData.attribute.attack_speed*20
    }

    override fun continueExecute(): Boolean {
        val target = petEntity.entity?.metaEntity("target")?: return false
        return (petEntity.entity?.distanceLoc(petEntity.owner)?: 20.0) < 10.0 && !target.isDead
    }

    override fun updateTask() {
        val entity = petEntity.entity?: return
        val petData = petEntity.petData
        val attribute = petData.attribute
        val target = entity.metaEntity("target")?: return
        entity.navigationMove(target.location, attribute.speed)
        if (entity.distanceLoc(target)<= 3.0) {
            if (petData.attribute.attack_speed*20 == delay) {
                (target as LivingEntity).damage(attribute.attack, entity)
                val attackOption = petEntity.getStateOption(action)
                if (attackOption != null) {
                    val model = petEntity.model()
                    entity.playAnimation(model.id, model.select, action, attackOption)
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