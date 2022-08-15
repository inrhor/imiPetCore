package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.script.kether.eval
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

class UiVariable(val name: String = "", val default: Any)

class MedicalUi(val title: String = "", val rows: Int = 6, val custom: MutableList<CustomButton> = mutableListOf()) {

    fun open(player: Player, petData: PetData, value: Double = 1.0) {
        player.openMenu<Linked<PetData>>(title.replace("{{pet name}}", petData.name)) {
            rows(rows)
            custom.forEach {
                set(it.slot, it.item.itemStackPet(player, petData, UiVariable("value", value))) {
                    clicker.eval(it.script, { s ->
                        s.rootFrame().variables()["@PetData"] = petData
                        s.rootFrame().variables()["value"] = value
                    }, {}, true)
                }
            }
        }
    }

}