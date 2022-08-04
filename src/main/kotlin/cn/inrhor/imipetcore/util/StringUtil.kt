package cn.inrhor.imipetcore

import taboolib.common.util.VariableReader

fun String.variableReader(): MutableList<String> {
    val list = mutableListOf<String>()
    VariableReader().readToFlatten(this).forEach {
        if (it.isVariable) list.add(it.text)
    }
    return list
}