package cn.inrhor.imipetcore.common.ui

import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.util.buildItem

class HomeUi(val title: String = "Home Pet Gui", val rows: Int = 6, val pet: PetSlot = PetSlot(),
             val previous: ButtonElement = ButtonElement(),
             val close: ButtonElement = ButtonElement(),
             val next: ButtonElement = ButtonElement()
) {

    fun open(player: Player, page: Int = 0) {
        player.openMenu<Linked<PetElements>>(title.replace("{page}", "$page")) {
            rows(rows)
            slots(pet.slot)
            elements {

            }
        }
    }

}

class PetSlot(val slot: List<Int> = listOf())

class ButtonElement(val slot: Int = 0, val item: ItemElement = ItemElement())

class ItemElement(val material: Material = Material.APPLE, val name: String = "", val modelData: Int = 0) {
    val item = buildItem(this@ItemElement.material) {
        name = this@ItemElement.name
        customModelData = modelData
    }
}