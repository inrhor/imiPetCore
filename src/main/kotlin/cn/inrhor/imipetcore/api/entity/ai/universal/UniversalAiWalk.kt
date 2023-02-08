package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.location.distanceLoc
import org.bukkit.entity.Wolf
import taboolib.module.ai.navigationMove

class UniversalAiWalk(val petEntity: PetEntity): UniversalAi() {

    override fun shouldExecute(): Boolean {
        val owner = petEntity.owner
        return !petEntity.petData.isDead() && owner.isOnline && !owner.isDead &&
                (petEntity.entity?.distanceLoc(owner)?: 0.0) > 6.0
    }

    override fun continueExecute(): Boolean {
        return false
    }

    override fun updateTask() {
        val pet = petEntity.entity?: return
        val owner = petEntity.owner
        val wolf = pet as Wolf
        wolf.target = null
        if (pet.distanceLoc(owner) > 16.0) {
            pet.teleport(owner)
        }else {
            pet.navigationMove(owner.location, 1.2)
        }
    }

}