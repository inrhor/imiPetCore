package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.location.distanceLoc
import taboolib.module.ai.navigationMove

class UniversalAiModelAction(val petEntity: PetEntity): UniversalAi() {

    override fun shouldExecute(): Boolean {
        val owner = petEntity.owner
        return !petEntity.petData.isDead() && owner.isOnline && !owner.isDead &&
                (petEntity.entity?.distanceLoc(owner)?: 0.0) > 10.0
    }

    override fun startTask() {
        val pet = petEntity.entity?: return
        val ow = petEntity.owner
        pet.navigationMove(ow.location, petEntity.petData.attribute.speed)
    }

}