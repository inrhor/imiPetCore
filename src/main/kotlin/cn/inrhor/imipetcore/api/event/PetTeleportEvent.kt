package cn.inrhor.imipetcore.api.event

import cn.inrhor.imipetcore.api.entity.PetEntity
import taboolib.platform.type.BukkitProxyEvent

/**
 * 宠物传送事件
 *
 * @param petEntity 宠物实体
 */
class PetTeleportEvent(val petEntity: PetEntity): BukkitProxyEvent()