package cn.inrhor.imipetcore.common.hook.protocol

import cn.inrhor.imipetcore.common.nms.NMS
import cn.inrhor.imipetcore.server.PluginLoader.protocolLibLoad
import cn.inrhor.imipetcore.util.PositionUtil.rotate
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import taboolib.common5.Coerce
import taboolib.common5.cbyte
import taboolib.module.nms.MinecraftVersion
import java.util.*

object PacketProtocol {

    /**
     * 发送ProtocolLib数据包
     */
    fun sendServerPacket(players: Set<Player>, packet: PacketContainer) {
        val p = ProtocolLibrary.getProtocolManager()
        players.forEach {
            p.sendServerPacket(it, packet)
        }
    }

    /**
     * 旋转实体
     */
    fun Entity.packetRotation(players: Set<Player>, yaw: Float, pitch: Float) {
        if (protocolLibLoad) {
            val pc = PacketContainer(PacketType.Play.Server.ENTITY_LOOK)
            pc.integers.write(0, entityId)
            pc.bytes
                .write(0, yaw.rotate().cbyte)
                .write(1, pitch.rotate().cbyte)
            sendServerPacket(players, pc)
        }else {
            NMS.INSTANCE.entityRotation(players, entityId, yaw, pitch)
        }
    }

    private val major = MinecraftVersion.major

    private val minor = MinecraftVersion.minor

    fun Entity.spawnEntity(players: Set<Player>, entityTypeId: Int) {
        val packet = if (major > 10) PacketContainer(PacketType.Play.Server.SPAWN_ENTITY) else PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING)
        val yaw = (location.yaw * 256.0f / 360.0f).toInt().toByte()
        val pitch = (location.pitch * 256.0f / 360.0f).toInt().toByte()
        packet.modifier.writeDefaults()
        packet.integers.writeSafely(0, entityId)
        packet.uuiDs.writeSafely(0, uniqueId)
        packet.integers.writeSafely(1, entityTypeId)
        packet.doubles
            .writeSafely(0, location.x)
            .writeSafely(1, location.y)
            .writeSafely(2, location.z)
        packet.bytes
            .writeSafely(0, yaw)
            .writeSafely(1, pitch)
            .writeSafely(2, yaw)

        sendServerPacket(players, packet)
    }

    fun Entity.destroyEntity(players: Set<Player>) {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
        if (major >= 9 && minor == 0) {
            if (minor == 0) {
                packet.integers.writeSafely(0, entityId)
            }else {
                packet.intLists.writeSafely(0, listOf(entityId))
            }
        }else {
            packet.integerArrays.writeSafely(0, intArrayOf(entityId))
        }
        sendServerPacket(players, packet)
    }

}