package cn.inrhor.imipetcore.common.ui

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.variableReader
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.chat.colored
import taboolib.module.kether.KetherShell
import taboolib.module.kether.printKetherErrorMessage
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.compat.replacePlaceholder
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
                    ex.printKetherErrorMessage()
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
                //
            }
        }
    }

}

class PetSlot(val slot: List<Int> = listOf())

class ButtonElement(val slot: Int = 0, val item: ItemElement = ItemElement())

class ItemElement(val material: Material = Material.APPLE, val name: String = "", val lore: List<String> = listOf(),val modelData: Int = 0) {
    fun itemStack(player: Player): ItemStack = buildItem(this@ItemElement.material) {
        val a = this@ItemElement
        name = a.name.replacePlaceholder(player).colored()
        a.lore.forEach {
            lore.add(it.replacePlaceholder(player).colored())
        }
        customModelData = modelData
    }

    fun itemStackPet(player: Player, petData: PetData): ItemStack = buildItem(this@ItemElement.material) {
        val a = this@ItemElement
        name = a.name.replacePlaceholder(player).colored()
        a.lore.forEach { c ->
            var text = c
            c.variableReader().forEach { e ->
                text = c.replace("{{$e}}",
                    KetherShell.eval(e, sender = adaptPlayer(player)) {
                        rootFrame().variables()["@PetData"] = petData
                    }.thenApply {
                        Coerce.toString(it)
                    }.getNow(c)
                ).replacePlaceholder(player).colored()
            }
            lore.add(text)
        }
        customModelData = modelData
    }
}