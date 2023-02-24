package cn.inrhor.imipetcore.common.script.kether.action

import cn.inrhor.imipetcore.api.manager.IconManager.iconItem
import cn.inrhor.imipetcore.common.script.kether.player
import cn.inrhor.imipetcore.common.script.kether.selectPetData
import cn.inrhor.imipetcore.common.script.kether.selectSkillData
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser
import taboolib.module.kether.switch

class IconAction {
    companion object {

        @KetherParser(["iconImi"], shared = true)
        fun parser() = scriptParser {
            it.switch {
                case("pet") {
                    actionNow {
                        selectPetData().iconItem(player())
                    }
                }
                case("skill") {
                    val id = next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(id).run<String>().thenAccept { e ->
                            selectSkillData().iconItem(player(), selectPetData(), e)
                        }
                    }
                }
            }
        }
    }
}