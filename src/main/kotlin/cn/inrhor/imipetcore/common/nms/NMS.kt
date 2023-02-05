package cn.inrhor.imipetcore.common.nms

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.module.nms.nmsProxy

abstract class NMS {

    /**
     * 1.13- 旋转实体
     */
    abstract fun entityRotation(players: Set<Player>, entityId: Int, yaw: Float, pitch: Float)

    /**
     * 1.18-
     */
    abstract fun spawnEntityLiving(players: Set<Player>, entity: Entity, entityType: String)

    /**
     * 1.19+
     */
    abstract fun spawnEntity(players: Set<Player>, entity: Entity, entityType: String)

    abstract fun destroyEntity(players: Set<Player>, entityId: Int)

    companion object {

        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

}