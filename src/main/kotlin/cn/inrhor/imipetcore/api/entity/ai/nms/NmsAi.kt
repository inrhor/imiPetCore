package cn.inrhor.imipetcore.api.entity.ai.nms

import net.minecraft.server.v1_12_R1.PathfinderGoal

abstract class NmsAi: PathfinderGoal() {

    /**
     * shouldExecute
     */
    abstract override fun a(): Boolean

    /**
     * continueExecute
     */
    override fun b(): Boolean {
        return false
    }

    /**
     * startTask
     */
    override fun c() {}

    /**
     * resetTask
     */
    override fun d() {}

    /**
     * updateTask
     */
    override fun e() {}

}