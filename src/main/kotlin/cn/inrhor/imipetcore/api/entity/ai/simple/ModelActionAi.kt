package cn.inrhor.imipetcore.api.entity.ai.simple

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiWalk
import taboolib.module.ai.SimpleAi

/**
 * 为了播放模型动作
 */
class ModelActionAi(val petEntity: PetEntity): SimpleAi() {

    val universal = UniversalAiWalk(petEntity)

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

    /**
     * 是否继续执行
     * false，终止并执行resetTask
     * true执行updateTask
     */
    override fun continueExecute(): Boolean {
        return false
    }

}