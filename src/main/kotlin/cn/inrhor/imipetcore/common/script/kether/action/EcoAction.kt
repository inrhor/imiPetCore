package cn.inrhor.imipetcore.common.script.kether.action

import cn.inrhor.imipetcore.common.script.kether.player
import taboolib.common5.Coerce
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.getBalance
import taboolib.platform.compat.withdrawBalance

class EcoAction {
    companion object {

        @KetherParser(["economy"], shared = true)
        fun parser() = scriptParser {
            it.switch {
                case("get") {
                    actionNow {
                        player().getBalance()
                    }
                }
                case("add") {
                    val amount = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(amount).run<Any>().thenApply { d ->
                            player().depositBalance(Coerce.toDouble(d))
                        }
                    }
                }
                case("del") {
                    val amount = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(amount).run<Any>().thenApply { d ->
                            player().withdrawBalance(Coerce.toDouble(d))
                        }
                    }
                }
            }
        }
    }
}