package cn.inrhor.imipetcore.common.location

import org.bukkit.entity.Entity
import taboolib.module.nms.MinecraftVersion

/**
 * Entity 宠物或其它实体
 * @param entity 其它实体
 * @return 距离
 */
fun Entity.distanceLoc(entity: Entity, def: Double = 1000000.0): Double {
    if (world != entity.world) return def
    if (MinecraftVersion.major <= 5) {
        return location.distance(entity.location)
    }
    return boundingBox.max.distance(entity.boundingBox.max)
}