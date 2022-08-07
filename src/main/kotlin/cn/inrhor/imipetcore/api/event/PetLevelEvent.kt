package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 宠物属性状态变化事件
 * @param player
 * @param petData 宠物数据
 * @param addLevel 提升等级
 * @param level 新等级
 */
class PetLevelEvent(val player: Player, val petData: PetData, val addLevel: Int, val level: Int): BukkitProxyEvent()