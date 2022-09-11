package cn.inrhor.imipetcore.api.entity.ai.simple

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiAttack
import taboolib.module.ai.SimpleAi

data class AttackAi(val petEntity: PetEntity, val action: String = "attack", var delay: Int = 0): SimpleAi() {

    val universalAi = UniversalAiAttack(petEntity, action, delay)

    /**
     * 检测
     * true -> startTask
     */
    override fun shouldExecute(): Boolean {
        return universalAi.shouldExecute()
    }

    /**
     * 向目标移动并攻击，播放攻击动作(Model)
     */
    override fun startTask() {
        universalAi.startTask()
    }

    override fun continueExecute(): Boolean {
        return universalAi.continueExecute()
    }

    override fun updateTask() {
        universalAi.updateTask()
    }

    override fun resetTask() {
        universalAi.resetTask()
    }

}