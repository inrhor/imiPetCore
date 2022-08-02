package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.navigationMove

data class AttackAi(val petEntity: PetEntity, val action: String = "attack"): SimpleAi() {

    private val speed = petEntity.petData.petOption().default.attribute.attack_speed*20

    /**
     * 检测
     * true -> startTask
     */
    override fun shouldExecute(): Boolean {
        val attack = petEntity.getActionData(action)?: return false
        val target = attack.entity?: return false
        return !target.isDead && petEntity.petData.attribute.attack > 0 &&
                (petEntity.entity?.boundingBox?.max?.distance(petEntity.owner.boundingBox.max)?: 100.0) <= 20.0
    }

    override fun continueExecute(): Boolean {
        return false
    }

    override fun resetTask() {
        petEntity.state.updateState(petEntity, "walk")
    }

    /**
     * 向目标移动并攻击，播放攻击动作(Model)
     */
    override fun startTask() {
        val entity = petEntity.entity?: return
        val attribute = petEntity.petData.petOption().default.attribute
        val attack = petEntity.getActionData(action)?: return
        val target = attack.entity
        val delay = attack.delay
        entity.navigationMove(target!!.location, attribute.speed)
        if (entity.boundingBox.max.distance(target.boundingBox.max)<= 3.0) {
            if (speed == delay) {
                (target as LivingEntity).damage(attribute.attack, entity)
                val active = petEntity.modelEntity?.getActiveModel(petEntity.petData.petOption().model.id)
                val attackOption = petEntity.getStateOption(action)
                if (attackOption != null) {
                    active?.addState(action, attackOption.lerpin, attackOption.lerpout, attackOption.speed)
                }
            }else if (delay == 0) {
                attack.delay = speed
                return
            }
            attack.delay--
        }
    }

}