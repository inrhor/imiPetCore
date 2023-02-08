package cn.inrhor.imipetcore.api.entity.ai.simple

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiWalk
import taboolib.module.ai.SimpleAi

class WalkAi(val petEntity: PetEntity): SimpleAi() {

    val universal = UniversalAiWalk(petEntity)

    /**
     * 检查，true执行startTask
     */
    override fun shouldExecute(): Boolean {
        return universal.shouldExecute()
    }

    override fun updateTask() {
        universal.updateTask()
    }

}