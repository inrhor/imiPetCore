package cn.inrhor.imipetcore.api.entity.ai.simple

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiModelAction
import taboolib.module.ai.SimpleAi

/**
 * 为了播放模型动作
 */
class ModelActionAi(val petEntity: PetEntity): SimpleAi() {

    val universal = UniversalAiModelAction(petEntity)

    /**
     * 检查，true执行startTask
     */
    override fun shouldExecute(): Boolean {
        return universal.shouldExecute()
    }

    /**
     * 执行任务
     */
    override fun startTask() {
        universal.startTask()
    }

    override fun continueExecute(): Boolean {
        return universal.continueExecute()
    }

    override fun resetTask() {
        universal.resetTask()
    }

}