package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.script.kether.eval
import org.bukkit.entity.Player
import taboolib.common5.Coerce
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

class ManagerPetUi(val title: String = "Pet Gui", val rows: Int = 6, val slot: List<Int> = listOf() ,val button: MutableList<ButtonUi> = mutableListOf()) {
    fun open(player: Player, petData: PetData) {
        player.openMenu<Linked<ButtonUi>>(title.replace("{{pet name}}", petData.name)) {
            rows(rows)
            slots(slot)
            elements {
                button
            }
            onGenerate { player, element, _, _ ->
                element.item.itemStackPet(player, petData)
            }
            onClick { event, element ->
                event.clicker.eval(element.script, {
                    it.rootFrame().variables()["@PetData"] = petData
                }, {
                    Coerce.toBoolean(it)
                }, true)
            }
        }
    }
}

class ButtonUi(val item: ItemElement = ItemElement(), val script: String = "")