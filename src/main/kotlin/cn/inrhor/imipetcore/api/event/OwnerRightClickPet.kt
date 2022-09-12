package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 主人点击宠物事件
 */
class OwnerRightClickPet(val player: Player, val petData: PetData): BukkitProxyEvent()