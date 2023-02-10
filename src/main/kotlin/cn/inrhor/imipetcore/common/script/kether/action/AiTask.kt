package cn.inrhor.imipetcore.common.script.kether

import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser

class AiTask {

    companion object {
        @KetherParser(["taskTime"])
        fun parserPet() = scriptParser {
            actionNow {
                variables().get<Int?>("@TaskTime")
                    .orElse(null)?: 0
            }
        }
    }

}