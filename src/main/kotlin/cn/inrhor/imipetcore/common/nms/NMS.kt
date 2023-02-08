package cn.inrhor.imipetcore.common.nms

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
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

    /**
     * 销毁实体
     */
    abstract fun destroyEntity(players: Set<Player>, entityId: Int)

    /**
     * 添加攻击行为
     */
    abstract fun addAiAttack(livingEntity: LivingEntity, priority: Int)

    /**
     * 实体攻击目标
     */
    abstract fun attack(livingEntity: LivingEntity, entity: Entity)

    companion object {

        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

}