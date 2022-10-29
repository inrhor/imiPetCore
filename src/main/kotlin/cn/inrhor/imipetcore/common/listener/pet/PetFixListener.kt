package cn.inrhor.imipetcore.common.listener.pet

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEntityEvent
import taboolib.common.platform.event.SubscribeEvent

object PetFixListener {

    /**
     * 禁止命名牌对宠物使用
     * 禁止马鞍对宠物使用
     */
    @SubscribeEvent
    fun nameTag(ev: PlayerInteractEntityEvent) {
        val entity = ev.rightClicked
        if (entity.hasMetadata("imipetcore_entity")) {
            val p = ev.player
            val type = p.inventory.itemInMainHand.type
            if (type == Material.NAME_TAG || type == Material.SADDLE) {
                ev.isCancelled = true
            }
        }
    }

}