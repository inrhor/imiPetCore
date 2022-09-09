package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.ModelManager.playAnimation
import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.common.option.ActionOption
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.ai.SimpleAi
import taboolib.module.kether.KetherShell
import java.util.concurrent.CompletableFuture

class HookAi(val actionOption: ActionOption, val petEntity: PetEntity, var time: Int = 0): SimpleAi() {

    private fun eval(script: String): CompletableFuture<Any?> {
        val owner = petEntity.owner
        return KetherShell.eval("all [ $script ]", sender = adaptPlayer(owner)) {
            rootFrame().variables()["@PetData"] = petEntity.petData
            rootFrame().variables()["@TaskTime"] = time
        }
    }

    override fun shouldExecute(): Boolean {
        time = actionOption.taskTime
        return eval(actionOption.shouldExecute).thenApply {
            Coerce.toBoolean(it)
        }.getNow(true)
    }

    override fun startTask() {
        time = actionOption.taskTime
        eval(actionOption.startTask)
        val action = actionOption.name
        val stateOption = petEntity.getStateOption(action)
        if (stateOption != null) {
            val model = petEntity.model()
            petEntity.entity?.playAnimation(model.id, model.select, action, stateOption)
        }
    }

    override fun continueExecute(): Boolean {
        return eval(actionOption.continueExecute).thenApply {
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