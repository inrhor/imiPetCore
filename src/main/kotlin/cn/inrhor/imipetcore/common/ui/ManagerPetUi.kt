package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.script.kether.eval
import org.bukkit.entity.Player
import taboolib.common5.Coerce
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

class ManagerPetUi(val title: String = "Pet Gui", val rows: Int = 6, val slot: List<Int> = listOf(),
                   val button: MutableList<ButtonUi> = mutableListOf(),
                   val custom: MutableList<CustomButton> = mutableListOf(),
                   val previous: ButtonElement = ButtonElement(),
                   val close: ButtonElement = ButtonElement(),
                   val next: ButtonElement = ButtonElement()) {
    fun open(player: Player, petData: PetData, page: Int = 0) {
        player.openMenu<Linked<ButtonUi>>(title.replace("{{pet name}}", petData.name)
            .replace("{{page}}", "$page")) {
            rows(rows)
            slots(slot)
            elements {
                button
            }
            onGenerate { player, element, _, _ ->
                element.item.itemStackPet(player, petData)
            }
            custom.forEach {
                set(it.slot, it.item.itemStackPet(player, petData)) {
                    clicker.eval(it.script)
                }
            }
            setPreviousPage(previous.slot) { _, _ ->
                previous.item.itemStack(player)
            }
            setNextPage(next.slot) { _, _ ->
                next.item.itemStack(player)
            }
            set(close.slot, close.item.itemStack(player)) {
                player.closeInventory()
            }
            onClick { event, element ->
                event.clicker.eval(element.script, {
                    it.rootFrame().variables()["@PetData"] = petData
                    it.rootFrame().variables()["@UiPage"] = page
                }, {
                    Coerce.toBoolean(it)
                }, true)
            }
        }
    }
}

class ButtonUi(val item: ItemElement = ItemElement(), val script: String = "")
class CustomButton(val slot: Int = 0, val item: ItemElement = ItemElement(), val script: String = "")