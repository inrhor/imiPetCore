package cn.inrhor.imipetcore.common.location

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptLocation
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.toBukkitLocation

/**
 * Entity 宠物或其它实体
 * @param entity 其它实体
 * @return 距离
 */
fun Entity.distanceLoc(entity: Entity): Double {
    if (MinecraftVersion.major <= 5) {
        return location.distance(entity.location)
    }
    return boundingBox.max.distance(entity.boundingBox.max)
}

/**
 * @return 宠物跟随主人位置
 */
fun Player.referFollowLoc(): Location {
    return adaptLocation(location).referTo(location.yaw, 90f, 2.0, 0.5).toBukkitLocation()
}