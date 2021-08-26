package cn.inrhor.imipetcore.common.entity

import org.bukkit.Location
import java.util.*

/**
 * 宠物数据
 */
class PetData(val index: Int, val uuid: UUID, val position: Position) {

    fun getLocation(): Location {
        return position.clone().toLocation()
    }

    val viewersDate = ViewersData()

    var isDeath = false



}