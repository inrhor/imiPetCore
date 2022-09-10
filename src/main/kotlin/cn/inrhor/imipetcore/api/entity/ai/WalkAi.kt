package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.location.distanceLoc
import taboolib.module.ai.SimpleAi
import taboolib.module.ai.navigationMove

class WalkAi(val petEntity: PetEntity): SimpleAi() {

    /**
     * 检查，true执行startTask
     */
    override fun shouldExecute(): Boolean {
        val owner = petEntity.owner
        return !petEntity.petData.isDead() && owner.isOnline && !owner.isDead &&
                (petEntity.entity?.distanceLoc(owner)?: 0.0) > 10.0
    }

    /**
     * 执行任务
     */
    override fun startTask() {
        val pet = petEntity.entity?: return
        val ow = petEntity.owner
        if (pet.world != ow.world || pet.distanceLoc(ow) > 64.0 ) {
            pet.teleport(ow)
        }else pet.navigationMove(ow, petEntity.petData.attribute.speed)
    }

    /**
     * 是否继续执行
     * false，终止并执行resetTask
     * true执行updateTask
     */
    override fun continueExecute(): Boolean {
        return false
    }

}