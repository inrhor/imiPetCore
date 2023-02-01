package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.location.distanceLoc
import taboolib.module.ai.navigationMove

class UniversalAiWalk(val petEntity: PetEntity): UniversalAi() {

    override fun shouldExecute(): Boolean {
        val owner = petEntity.owner
        return !petEntity.petData.isDead() && owner.isOnline && !owner.isDead &&
                (petEntity.entity?.distanceLoc(owner)?: 0.0) > 6.0
    }

    override fun startTask() {
        val pet = petEntity.entity?: return
        val owner = petEntity.owner
        if (pet.distanceLoc(owner) > 16.0) {
            pet.teleport(owner)
        }else {
            pet.navigationMove(owner.location, petEntity.petData.attribute.speed)
        }
    }

}