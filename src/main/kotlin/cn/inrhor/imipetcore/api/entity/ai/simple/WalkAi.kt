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

    /**
     * 是否继续执行
     * false，终止并执行resetTask
     * true执行updateTask
     */
    override fun continueExecute(): Boolean {
        return universal.continueExecute()
    }

    override fun updateTask() {
        universal.updateTask()
    }

}