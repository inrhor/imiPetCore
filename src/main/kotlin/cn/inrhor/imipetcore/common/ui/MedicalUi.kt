package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.script.kether.eval
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

class UiVariable(val name: String = "", val default: String = "")

class CustomUi(val title: String = "", val rows: Int = 6, val custom: MutableList<CustomButton> = mutableListOf()) {

    fun open(player: Player, petData: PetData?) {
        player.openMenu<Linked<Player>>(title) {
            rows(rows)
            custom.forEach {
                val item = if (petData != null) it.item.itemStackPet(player, petData) else it.item.itemStack(player)
                set(it.slot, item) {
                    clicker.eval(it.script, { s ->
                        if (petData != null) {
                            s.rootFrame().variables()["@PetData"] = petData
                        }
                    }, {}, true)
                }
            }
        }
    }

}