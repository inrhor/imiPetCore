package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiHook
import cn.inrhor.imipetcore.common.option.ActionOption

class NmsAiHook(val actionOption: ActionOption, val petEntity: PetEntity, var time: Int = 0): NmsAi() {

    val universalAi = UniversalAiHook(actionOption, petEntity, time)

    override fun a(): Boolean {
        return universalAi.shouldExecute()
    }

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