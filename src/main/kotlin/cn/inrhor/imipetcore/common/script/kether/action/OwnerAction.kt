package cn.inrhor.imipetcore.common.script.kether.action

import cn.inrhor.imipetcore.api.manager.OwnerManager.getDamageByEntity
import cn.inrhor.imipetcore.common.script.kether.player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser
import taboolib.module.kether.switch

object OwnerAction {

    @KetherParser(["owner"], namespace = "imiPetCore")
    fun parser() = scriptParser {
        it.switch {
            case("attacker") {
                actionNow {
                    player().getDamageByEntity()
                }
            }
        }
    }

}