package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 乘骑宠物事件
 */
class DrivePetEvent(val player: Player, val petData: PetData): BukkitProxyEvent()

/**
 * 取消乘骑宠物
 */
class UnDriveRidePet(val player: Player, val petData: PetData): BukkitProxyEvent()