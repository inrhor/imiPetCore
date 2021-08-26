package cn.inrhor.imipetcore.common.entity

import org.bukkit.Location
import org.bukkit.World

/**
 * 坐标数据
 */
class Position(
        val world: World,
        var x: Double = 0.0,
        var y: Double = 0.0,
        var z: Double = 0.0,
        var yaw: Float = 0f,
        var pitch: Float = 0f) {

    fun add(x: Double, y: Double, z: Double): Position {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun del(x: Double, y: Double, z: Double): Position {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun toLocation(): Location {
        return Location(world, x, y, z, yaw, pitch)
    }

    fun clone(): Position {
        return Position(world, x, y, z, yaw, pitch)
    }
}