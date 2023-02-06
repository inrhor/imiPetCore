package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.MetaManager.metaEntity
import cn.inrhor.imipetcore.api.manager.MetaManager.removeMeta
import cn.inrhor.imipetcore.common.location.distanceLoc

class UniversalAiAttack(val petEntity: PetEntity, val action: String = "attack", var delay: Int = 0): UniversalAi() {

    override fun shouldExecute(): Boolean {
        val target = petEntity.entity?.metaEntity("target")?: return false
        return !target.isDead && petEntity.petData.attribute.attack > 0 &&
                ( petEntity.entity?.distanceLoc(petEntity.owner)?: 100.0) <= 20.0
    }

    override fun startTask() {
    }

    override fun continueExecute(): Boolean {
        val target = petEntity.entity?.metaEntity("target")?: return false
        return (petEntity.entity?.distanceLoc(petEntity.owner)?: 20.0) < 15.0 && !target.isDead
    }

    override fun updateTask() {
        val entity = petEntity.entity?: return
        val target = entity.metaEntity("target")?: return
        entity.attack(target)
    }

    override fun resetTask() {
        petEntity.entity?.removeMeta("target")
    }

}