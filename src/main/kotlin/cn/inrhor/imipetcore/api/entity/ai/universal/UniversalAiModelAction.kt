package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity

class UniversalAiModelAction(val petEntity: PetEntity): UniversalAi() {

    override fun shouldExecute(): Boolean {
        return petEntity.actionBaffle?.hasNext()?: false
    }

    override fun startTask() {
    }

    override fun continueExecute(): Boolean {
        return !(petEntity.actionBaffle?.hasNext()?: false)
    }

    override fun resetTask() {
        petEntity.actionBaffle?.reset()
        petEntity.actionBaffle = null
    }

}