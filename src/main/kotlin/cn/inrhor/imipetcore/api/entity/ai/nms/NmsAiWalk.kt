package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiWalk

class NmsAiWalk(val petEntity: PetEntity): NmsAi() {

    val universalAi = UniversalAiWalk(petEntity)

    override fun a(): Boolean {
        return universalAi.shouldExecute()
    }

    override fun c() {
        universalAi.startTask()
    }

}