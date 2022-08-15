package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.ui.UiData
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*
import taboolib.module.nms.inputSign

class InputAction {

    companion object {
        private val tokenType = ArgTypes.listOf {
            it.nextToken()
        }

        @KetherParser(["input"])
        fun parser() = scriptParser {
            it.switch {
                case("select") {
                    when (it.expects("medical", "rename")) {
                        "medical" -> {
                            val text = try {
                                it.mark()
                                it.expect("text")
                                it.next(tokenType)
                            }catch (ex: Throwable) {
                                it.reset()
                                emptyList()
                            }
                            val line = try {
                                it.mark()
                                it.expect("line")
                                it.nextInt()
                            }catch (ex: Throwable) {
                                it.reset()
                                1
                            }
                            actionNow {
                                player().inputSign(text.toTypedArray()) { a ->
                                    val s = a[line-1]
                                    val v = if (s.isEmpty()) 1.0 else s.toDouble()
                                    UiData.medicalUi.open(player(), selectPetData(), v)
                                }
                            }
                        }
                        "rename" -> {
                            val text = try {
                                it.mark()
                                it.expect("text")
                                it.next(tokenType)
                            }catch (ex: Throwable) {
                                it.reset()
                                emptyList()
                            }
                            val line = try {
                                it.mark()
                                it.expect("line")
                                it.nextInt()
                            }catch (ex: Throwable) {
                                it.reset()
                                1
                            }
                            actionNow {
                                player().inputSign(text.toTypedArray()) { a ->
                                    val s = a[line-1]
                                    if (s.isNotEmpty()) selectPetData().name = s
                                    UiData.managerPetUi.open(player(), selectPetData())
                                }
                            }
                        }
                        else -> error("input select ?")
                    }
                }
            }
        }
    }

}