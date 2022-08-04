package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.ui.UiData.homePetUi
import cn.inrhor.imipetcore.common.ui.UiData.managerPetUi
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser
import taboolib.module.kether.switch

class UiAction {

    companion object {
        @KetherParser(["ui"], shared = true)
        fun parser() = scriptParser {
            it.switch {
                case("close") {
                    actionNow {
                        player().closeInventory()
                    }
                }
                case("open") {
                    val a = it.nextToken().lowercase()
                    when (a) {
                        "homepet" -> {
                            val page = try {
                                it.expect("page")
                                it.nextInt()
                            }catch (ex: Throwable) {
                                0
                            }
                            actionNow {
                                homePetUi.open(player(), page)
                            }
                        }
                        "managerpet" -> {
                            actionNow {
                                managerPetUi.open(player(), selectPetData())
                            }
                        }
                        else -> error("ui open ?")
                    }
                }
            }
        }
    }

}