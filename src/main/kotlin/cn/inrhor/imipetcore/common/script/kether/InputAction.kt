package cn.inrhor.imipetcore.common.script.kether

import taboolib.common5.Coerce
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser
import taboolib.module.nms.inputSign

class InputAction {

    companion object {
        private val tokenType = ArgTypes.listOf { reader ->
            reader.nextToken()
        }

        @KetherParser(["input"])
        fun parser() = scriptParser {
            val variable = it.nextToken()
            val line = try {
                it.mark()
                it.expect("line")
                it.nextInt()
            }catch (ex: Throwable) {
                it.reset()
                0
            }
            val type = try {
                it.mark()
                it.expect("type")
                it.nextToken()
            }catch (ex: Throwable) {
                it.reset()
                "double"
            }
            val default = try {
                it.mark()
                it.expect("default")
                it.nextToken()
            }catch (ex: Throwable) {
                it.reset()
                "0"
            }
            val text = try {
                it.mark()
                it.expect("text")
                it.next(tokenType)
            }catch (ex: Throwable) {
                it.reset()
                emptyList()
            }
            val click = try {
                it.mark()
                it.expect("click")
                it.nextToken()
            }catch (ex: Throwable) {
                it.reset()
                ""
            }
            actionNow {
                player().inputSign(text.toTypedArray()) { i ->
                    val index = if (line > 4) 4 else line
                    var get = ""
                    for (d in 0 until index) {
                        get += i[d]
                    }
                    val v = when (type) {
                        "double" -> get.toDoubleOrNull()?: default.toDoubleOrNull()?: 0.0
                        "int" -> get.toIntOrNull()?: default.toIntOrNull()?: 0
                        "boolean" -> get.toBooleanStrictOrNull()?: default.toBooleanStrictOrNull()?: true
                        else -> get.ifEmpty { default }
                    }
                    player().eval(click, { sc ->
                        sc.rootFrame().variables()[variable] = v
                    }, {}, true)
                }
            }
        }
    }

}