package cn.inrhor.imipetcore.common.option

class ActionOption(@Transient var name: String = "null_action",
                   val taskTime: Int = 0,
                   val shouldExecute: String = "", val startTask: String = "",
                   val continueExecute: String = "", val updateTask: String = "", val resetTask: String = "")