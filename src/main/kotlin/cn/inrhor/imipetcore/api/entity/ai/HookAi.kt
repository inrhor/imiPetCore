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
        return KetherShell.eval("if all [ ${actionOption.should} ]", sender = adaptPlayer(owner)) {
            rootFrame().variables()["@PetOwner"] = petEntity.owner
            rootFrame().variables()["@PetEntity"] = petEntity.entity
        }.thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun startTask() {
        val owner = petEntity.owner
        KetherShell.eval(actionOption.start, sender = adaptPlayer(owner)) {
            rootFrame().variables()["@PetOwner"] = petEntity.owner
            rootFrame().variables()["@PetEntity"] = petEntity.entity
        }
    }

    override fun continueExecute(): Boolean {
        return false
    }

}