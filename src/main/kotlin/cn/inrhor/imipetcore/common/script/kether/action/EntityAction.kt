package cn.inrhor.imipetcore.common.script.kether.action

import cn.inrhor.imipetcore.api.manager.MetaManager.isPet
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.scriptParser
import taboolib.module.kether.switch

object EntityAction {

    @KetherParser(["entityCheck"], namespace = "imiPetCore")
    fun parser() = scriptParser {
        it.switch {
            case("isPet") {
                val entity = it.next(ArgTypes.ACTION)
                actionNow {
                    newFrame(entity).run<Entity>().thenApply { a ->
                        a.isPet()
                    }
                }
            }
            case("isPlayer") {
                val entity = it.next(ArgTypes.ACTION)
                actionNow {
                    newFrame(entity).run<Entity>().thenApply { a ->
                        a.isPlayer()
                    }
                }
            }
        }
    }

    private fun Entity.isPlayer(): Boolean {
        return this is Player
    }

}