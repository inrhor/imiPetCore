package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiAttack

data class NmsAiAttack(val petEntity: PetEntity, val action: String = "attack", var delay: Int = 0): NmsAi() {

    val universalAi = UniversalAiAttack(petEntity, action, delay)

    /**
     * 检测
     * true -> startTask
     */
    override fun a(): Boolean {
        return universalAi.shouldExecute()
    }

    /**
     * 向目标移动并攻击，播放攻击动作(Model)
     */
    override fun c() {
        universalAi.startTask()
    }

    override fun b(): Boolean {
        return universalAi.continueExecute()
    }

    override fun e() {
        universalAi.updateTask()
    }

    override fun d() {
        universalAi.resetTask()
    }

}