package cn.inrhor.imipetcore.core.position

import org.bukkit.Location
import org.bukkit.World

/**
 * 坐标数据
 */
class Position(
        var world: World,
        var x: Double = 0.0,
        var y: Double = 0.0,
        var z: Double = 0.0,
        var yaw: Float = 0f,
        var pitch: Float = 0f) {

    fun toLocation(): Location {
        return Location(world, x, y, z, yaw, pitch)
    }

    fun set(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun clone(): Position {
        return Position(world, x, y, z, yaw, pitch)
    }
}