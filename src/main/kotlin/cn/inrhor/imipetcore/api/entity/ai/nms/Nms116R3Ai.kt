package cn.inrhor.imipetcore.api.entity.ai.nms.version

import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAi
import net.minecraft.server.v1_16_R3.PathfinderGoal

class Nms116R3Ai(val universalAi: UniversalAi): PathfinderGoal() {

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