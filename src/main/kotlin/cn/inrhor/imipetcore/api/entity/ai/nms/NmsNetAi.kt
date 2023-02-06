package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAi
import net.minecraft.world.entity.ai.goal.PathfinderGoal

class NmsNetAi(val universalAi: UniversalAi): PathfinderGoal() {

    /**
     * shouldExecute
     */
    fun a() = universalAi.shouldExecute()

    /**
     * continueExecute
     */
    fun b() = universalAi.continueExecute()

    /**
     * startTask
     */
    fun c() { universalAi.startTask() }

    /**
     * resetTask
     */
    fun d() { universalAi.resetTask() }

    /**
     * updateTask
     */
    fun e() { universalAi.updateTask() }

    /**
     * shouldExecute
     */
    override fun canUse(): Boolean = universalAi.shouldExecute()

    /**
     * continueExecute
     */
    override fun canContinueToUse() = universalAi.continueExecute()

    /**
     * startTask
     */
    override fun start() { universalAi.startTask() }

    /**
     * resetTask
     */
    override fun stop() { universalAi.resetTask() }

    /**
     * updateTask
     */
    override fun tick() { universalAi.updateTask() }

}