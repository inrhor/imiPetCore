package cn.inrhor.imipetcore.api.entity.state

import cn.inrhor.imipetcore.api.entity.PetEntity
import org.bukkit.entity.Entity

/**
 * 攻击状态
 */
class AttackState: ActiveState() {

    override val action = "attack"

    override fun run(petEntity: PetEntity, target: Entity?) {
        delay = petEntity.petData.attribute.attack_speed*20
        entity = target
    }

    override fun end(petEntity: PetEntity) {
    }

}