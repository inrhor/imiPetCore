package cn.inrhor.imipetcore.common.nms

import org.bukkit.entity.Player
import taboolib.module.nms.nmsProxy

abstract class NMS {

    /**
     * 1.13- 旋转实体
     */
    abstract fun entityRotation(player: Player, entityId: Int, yaw: Float, pitch: Float)

    companion object {

        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

}