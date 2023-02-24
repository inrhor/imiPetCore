package cn.inrhor.imipetcore.api.entity.ai.universal

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.ModelManager.playAnimation
import cn.inrhor.imipetcore.common.option.ActionOption
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.kether.KetherShell
import java.util.concurrent.CompletableFuture

class UniversalAiHook(val actionOption: ActionOption, val petEntity: PetEntity, var time: Int = 0): UniversalAi() {

    private fun eval(script: String): CompletableFuture<Any?> {
        val owner = petEntity.owner
        return KetherShell.eval(script, sender = adaptPlayer(owner)) {
            rootFrame().variables()["@PetData"] = petEntity.petData
            rootFrame().variables()["@TaskTime"] = time
        }
    }

    override fun shouldExecute(): Boolean {
        val s = actionOption.shouldExecute
        if (s.isEmpty()) return false
        time = actionOption.taskTime
        return eval(s).thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun startTask() {
        time = actionOption.taskTime
        val action = actionOption.name
        petEntity.playAnimation(action)
        eval(actionOption.startTask)
    }

    override fun continueExecute(): Boolean {
        val s = actionOption.continueExecute
        if (s.isEmpty()) return false
        return eval(s).thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun updateTask() {
        eval(actionOption.updateTask)
        time--
    }

    override fun resetTask() {
        eval(actionOption.resetTask)
    }

}