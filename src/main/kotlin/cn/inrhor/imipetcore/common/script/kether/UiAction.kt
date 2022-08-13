package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.ui.UiData
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
                case("page") {
                    actionNow {
                        getUiPage()
                    }
                }
                case("close") {
                    actionNow {
                        player().closeInventory()
                    }
                }
                case("open") {
                    when (it.nextToken().lowercase()) {
                        "homepet" -> {
                            val page = try {
                                it.mark()
                                it.expect("page")
                                it.nextInt()
                            }catch (ex: Throwable) {
                                it.reset()
                                0
                            }
                            actionNow {
                                homePetUi.open(player(), page)
                            }
                        }
                        "managerpet" -> {
                            val page = try {
                                it.mark()
                                it.expect("page")
                                it.nextInt()
                            }catch (ex: Throwable) {
                                it.reset()
                                0
                            }
                            actionNow {
                                managerPetUi.open(player(), selectPetData(), page)
                            }
                        }
                        else -> error("ui open ?")
                    }
                }
                case("custom") {
                    when (it.nextToken()) {
                        "open" -> {
                            val ui = it.nextToken()
                            actionNow {
                                UiData.customUi[ui]?.open(player(), selectPetData())
                            }
                        }
                        else -> error("ui custom ?")
                    }
                }
            }
        }
    }

}