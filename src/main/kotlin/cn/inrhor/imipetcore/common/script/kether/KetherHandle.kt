package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.util.variableReader
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.library.kether.LocalizedException
import taboolib.module.chat.colored
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptContext
import taboolib.platform.compat.replacePlaceholder

fun String.eval(variable: (ScriptContext) -> Unit, get: (Any?) -> Any, def: Any): Any {
    return try {
        KetherShell.eval(this) {
            variable(this)
        }.thenApply {
            get(it)
        }.getNow(def)
    }catch (ex: LocalizedException) {
        ex.printStackTrace()
        def
    }
}

fun Player.eval(script: String, variable: (ScriptContext) -> Unit, get: (Any?) -> Any, def: Any): Any {
    return try {
        KetherShell.eval(script, sender = adaptPlayer(this)) {
            variable(this)
        }.thenApply {
            get(it)
        }.getNow(def)
    }catch (ex: LocalizedException) {
        ex.printStackTrace()
        def
    }
}

fun Player.eval(script: String) {
    eval(script, {}, {Coerce.toBoolean(it)}, true)
}

class UiVariable(val name: String = "", val default: Any)

fun Player.evalStrPetData(script: String, petData: PetData, vararg variable: UiVariable): String {
    var text = script
    script.variableReader().forEach { e ->
        text = text.replace("{{$e}}", eval(e, {
            it.rootFrame().variables()["@PetData"] = petData
            variable.forEach { v ->
                it.rootFrame().variables()[v.name] = v.default
            }
        }, {
            Coerce.toString(it)
        }, script).toString())
    }
    return text.replacePlaceholder(this).colored()
}

fun Player.evalString(script: String): String {
    return evalString(script) {}
}

fun Player.evalString(script: String, variable: (ScriptContext) -> Unit): String {
    var text = script
    script.variableReader().forEach { e ->
        text = text.replace("{{$e}}", eval(e, {variable(it)}, {
            Coerce.toString(it)
        }, script).toString())
    }
    return text.replacePlaceholder(this).colored()
}