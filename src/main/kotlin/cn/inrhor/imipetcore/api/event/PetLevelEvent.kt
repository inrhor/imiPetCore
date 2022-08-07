package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 宠物属性状态变化事件
 * @param player
 * @param petData 宠物数据
 */
class AttributeChangePetEvent(val player: Player, val petData: PetData): BukkitProxyEvent()