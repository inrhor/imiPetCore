package cn.inrhor.imipetcore.common.script.kether

import taboolib.common5.Coerce
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser
import taboolib.module.kether.switch
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.getBalance
import taboolib.platform.compat.withdrawBalance

class EconomyAction {

    companion object {
        @KetherParser(["economy"], shared = true)
        fun parser() = scriptParser {
            it.switch {
                case("get") {
                    actionNow {
                        player().getBalance()
                    }
                }
                case("del") {
                    val a = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(a).run<Any>().thenAccept { e ->
                            player().withdrawBalance(Coerce.toDouble(e))
                        }
                    }
                }
                case("add") {
                    val a = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(a).run<Any>().thenAccept { e ->
                            player().depositBalance(Coerce.toDouble(e))
                        }
                    }
                }
            }
        }
    }

}