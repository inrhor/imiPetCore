package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.api.manager.ActionManager.setActionData
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*

class StateAction {

    companion object {
        @KetherParser(["action"])
        fun parser() = scriptParser {
            it.switch {
                case("data") {
                    when (it.expects("select", "target")) {
                        "select" -> {
                            val i = it.nextToken()
                            actionNow {
                                variables().set("@ActionData", selectPetData().petEntity?.getActionData(i))
                            }
                        }
                        "target" -> {
                            val i = it.next(ArgTypes.ACTION)
                            actionNow {
                                newFrame(i).run<Entity>().thenApply { e ->
                                    selectActionData().setActionData(e)
                                }
                            }
                        }
                        "location" -> {
                            val i = it.next(ArgTypes.ACTION)
                            actionNow {
                                newFrame(i).run<Location>().thenApply { e ->
                                    selectActionData().setActionData(e)
                                }
                            }
                        }
                        "delay" -> {
                            val i = it.nextInt()
                            actionNow {
                                selectActionData().setActionData(i)
                            }
                        }
                        else -> error("action data ?")
                    }
                }
            }
        }
    }
}