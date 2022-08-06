package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.option.ActionOption
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.ai.SimpleAi
import taboolib.module.kether.KetherShell

class HookAi(val actionOption: ActionOption, val petEntity: PetEntity): SimpleAi() {

    override fun shouldExecute(): Boolean {
        val owner = petEntity.owner
        return KetherShell.eval("all [ ${actionOption.should} ]", sender = adaptPlayer(owner)) {
            rootFrame().variables()["@PetData"] = petEntity.petData
        }.thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun startTask() {
        val owner = petEntity.owner
        KetherShell.eval(actionOption.start, sender = adaptPlayer(owner)) {
            rootFrame().variables()["@PetData"] = petEntity.petData
        }
        val action = actionOption.name
        val attackOption = petEntity.getStateOption(action)
        if (attackOption != null) {
            val active = petEntity.modelEntity?.getActiveModel(petEntity.petData.petOption().model.id)
            active?.addState(action, attackOption.lerpin, attackOption.lerpout, attackOption.speed)
        }
    }

    override fun continueExecute(): Boolean {
        return false
    }

}