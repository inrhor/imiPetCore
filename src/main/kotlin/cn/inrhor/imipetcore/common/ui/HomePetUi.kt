package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.util.buildItem

class HomePetUi(val title: String = "Home Pet Gui", val rows: Int = 6, val pet: PetSlot = PetSlot(),
                val previous: ButtonElement = ButtonElement(),
                val close: ButtonElement = ButtonElement(),
                val next: ButtonElement = ButtonElement()
) {

    fun open(player: Player, page: Int = 0) {
        player.openMenu<Linked<PetData>>(title.replace("{page}", "$page")) {
            rows(rows)
            slots(pet.slot)
            elements {
                player.getData().petDataList
            }
            onGenerate() { _, element, _, _ ->
                element.petOption().item.itemStack
            }
            onClick { event, element ->
                event.clicker.sendMessage("test "+element.name)
            }
        }
    }

}

class PetSlot(val slot: List<Int> = listOf())

class ButtonElement(val slot: Int = 0, val item: ItemElement = ItemElement())

class ItemElement(val material: Material = Material.APPLE, val name: String = "", val lore: List<String> = listOf(),val modelData: Int = 0) {
    val itemStack = buildItem(this@ItemElement.material) {
        name = this@ItemElement.name
        customModelData = modelData
    }
}