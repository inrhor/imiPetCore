package cn.inrhor.imipetcore.common.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.option.ActionOption
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.ai.SimpleAi
import taboolib.module.kether.KetherShell

class HookAi(val actionOption: ActionOption, val petEntity: PetEntity): SimpleAi() {

    override fun shouldExecute(): Boolean {
        val owner = petEntity.owner
        return KetherShell.eval(actionOption.should, sender = adaptPlayer(owner)).thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun startTask() {
        val owner = petEntity.owner
        KetherShell.eval(actionOption.should, sender = adaptPlayer(owner)).thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun continueExecute(): Boolean {
        return false
    }

}