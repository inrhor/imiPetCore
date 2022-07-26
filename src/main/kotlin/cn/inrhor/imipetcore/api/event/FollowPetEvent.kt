package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 宠物跟随状态变化事件
 * @param player
 * @param petData 宠物数据
 * @param follow 跟随状态
 */
class FollowPetEvent(val player: Player, val petData: PetData, val follow: Boolean): BukkitProxyEvent()