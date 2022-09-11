package cn.inrhor.imipetcore.api.entity.ai.universal

abstract class UniversalAi {

    abstract fun shouldExecute(): Boolean

    open fun startTask() {}

    open fun continueExecute(): Boolean = false

    open fun updateTask() {}

    open fun resetTask() {}

}