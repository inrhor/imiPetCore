package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.Controller.attackEntity
import cn.inrhor.imipetcore.api.manager.PetManager.hasTarget
import cn.inrhor.imipetcore.common.location.distanceLoc
import taboolib.module.ai.navigationMove

class UniversalAiWalk(val petEntity: PetEntity): UniversalAi() {

    val owner = petEntity.owner
    val petData = petEntity.petData
    val pet = petEntity.entity

    override fun shouldExecute(): Boolean {
        return !petData.isDead() && owner.isOnline && !owner.isDead &&
                (((pet?.distanceLoc(owner)?: 0.0) > 6.0 && !petEntity.hasTarget()) || (pet?.distanceLoc(owner)?: 0.0) > 32.0 )
    }

    override fun updateTask() {
        pet?: return
        pet.attackEntity(null)
        if (pet.distanceLoc(owner) > 32.0) {
            pet.teleport(owner)
        }else {
            pet.navigationMove(owner.location, 1.2)
        }
    }

}