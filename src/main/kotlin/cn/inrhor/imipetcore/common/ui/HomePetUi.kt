package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.script.kether.evalStrPetData
import cn.inrhor.imipetcore.common.script.kether.evalString
import cn.inrhor.imipetcore.common.ui.UiData.managerPetUi
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.util.buildItem

class HomePetUi(val title: String = "Home Pet Gui", val rows: Int = 6, val pet: PetSlot = PetSlot(),
                val previous: ButtonElement = ButtonElement(),
                val close: ButtonElement = ButtonElement(),
                val next: ButtonElement = ButtonElement()
) {

    fun open(player: Player, page: Int = 0) {
        player.openMenu<Linked<PetData>>(title.replace("{{page}}", "$page")) {
            rows(rows)
            slots(pet.slot)
            elements {
                player.getData().petDataList
            }
            onGenerate { p, element, _, _ ->
                try {
                    element.petOption().item.itemStackPet(p, element)
                }catch (ex: Throwable) {
                    ItemStack(Material.STONE)
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
                managerPetUi.open(event.clicker, element)
            }
        }
    }

}

class PetSlot(val slot: List<Int> = listOf())

class ButtonElement(val slot: Int = 0, val item: ItemElement = ItemElement())

class ItemElement(val material: Material = Material.APPLE, val name: String = "", val lore: List<String> = listOf(),val modelData: Int = 0) {
    fun itemStack(player: Player): ItemStack = buildItem(this@ItemElement.material) {
        val a = this@ItemElement
        name = player.evalString(a.name)
        a.lore.forEach {
            lore.add(player.evalString(it))
        }
        customModelData = modelData
    }

    fun itemStackPet(player: Player, petData: PetData): ItemStack = buildItem(this@ItemElement.material) {
        val a = this@ItemElement
        name = player.evalStrPetData(a.name, petData)
        a.lore.forEach { c ->
            lore.add(player.evalStrPetData(c, petData))
        }
        customModelData = modelData
    }
}