package cn.inrhor.imipetcore.util

import cn.inrhor.imipetcore.common.nms.NMS
import cn.inrhor.imipetcore.server.PluginLoader.protocolLibLoad
import cn.inrhor.imipetcore.util.PositionUtil.rotate
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common5.cbyte

object PacketUtil {

    /**
     * 发送ProtocolLib数据包
     */
    fun sendServerPacket(player: Player, packet: PacketContainer) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
    }

    fun Entity.packetRotation(player: Player, yaw: Float, pitch: Float) {
        if (protocolLibLoad) {
            val pc = PacketContainer(PacketType.Play.Server.ENTITY_LOOK)
            pc.integers.write(0, entityId)
            pc.bytes
                .write(0, yaw.rotate().cbyte)
                .write(1, pitch.rotate().cbyte)
            sendServerPacket(player, pc)
        }else {
            NMS.INSTANCE.entityRotation(player, entityId, yaw, pitch)
        }
    }

}