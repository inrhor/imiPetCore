package cn.inrhor.imipetcore.api.entity.ai.simple

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiHook
import cn.inrhor.imipetcore.common.option.ActionOption
import taboolib.module.ai.SimpleAi

class HookAi(val actionOption: ActionOption, val petEntity: PetEntity, var time: Int = 0): SimpleAi() {

    val universalAi = UniversalAiHook(actionOption, petEntity, time)

    override fun shouldExecute(): Boolean {
        return universalAi.shouldExecute()
    }

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