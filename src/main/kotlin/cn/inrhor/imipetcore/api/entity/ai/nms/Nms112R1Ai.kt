package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAi
import net.minecraft.server.v1_12_R1.PathfinderGoal

class Nms112R1Ai(val universalAi: UniversalAi): PathfinderGoal() {

    /**
     * shouldExecute
     */
    override fun a() = universalAi.shouldExecute()

    /**
     * continueExecute
     */
    override fun b() = universalAi.continueExecute()

    /**
     * startTask
     */
    override fun c() { universalAi.startTask() }

    /**
     * resetTask
     */
    override fun d() { universalAi.resetTask() }

    /**
     * updateTask
     */
    override fun e() { universalAi.updateTask() }

}