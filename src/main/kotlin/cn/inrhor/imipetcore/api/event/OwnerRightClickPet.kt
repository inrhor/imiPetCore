package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Player

/**
 * 主人点击宠物事件
 */
class OwnerRightClickPet(val player: Player, petData: PetData)