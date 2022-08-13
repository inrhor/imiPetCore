package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.script.kether.eval
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

class UiVariable(val name: String = "", val default: String = "")

class CustomUi(val id: String = "null", val title: String = "", val rows: Int = 6,
               val pet: List<String> = listOf(), val custom: MutableList<CustomButton> = mutableListOf(),
               val variable: MutableList<UiVariable> = mutableListOf()) {

    fun open(player: Player, petData: PetData) {
        if (pet.isNotEmpty() && !pet.contains(petData.name)) return
        player.openMenu<Linked<Player>>(title) {
            rows(rows)
            custom.forEach {
                set(it.slot, it.item.itemStackPet(player, petData, *variable.toTypedArray())) {
                    clicker.eval(it.script
                        .replace("pet select pet name", "pet select "+petData.name), { s->
                        s.rootFrame().variables()["@PetData"] = petData
                    }, {}, true)
                }
            }
        }
    }

}