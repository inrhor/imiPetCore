package cn.inrhor.imipetcore.common.nms

import cn.inrhor.imipetcore.util.PositionUtil.rotate
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity
import org.bukkit.entity.Player
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.common.reflect.Reflex.Companion.unsafeInstance
import taboolib.common5.cbyte
import taboolib.module.nms.sendPacket

class NMSImpl: NMS() {

    override fun entityRotation(players: Set<Player>, entityId: Int, yaw: Float, pitch: Float) {
        sendPacket(
            players,
            PacketPlayOutEntity.PacketPlayOutEntityLook::class.java.unsafeInstance(),
            "a" to entityId,
            "e" to yaw.rotate().cbyte,
            "f" to pitch.rotate().cbyte
        )
    }

    private fun sendPacket(players: Set<Player>, packet: Any, vararg fields: Pair<String, Any>) {
        val f = setFields(packet, *fields)
        players.forEach { it.sendPacket(f) }
    }

    private fun setFields(any: Any, vararg fields: Pair<String, Any?>): Any {
        fields.forEach { (key, value) ->
            if (value != null) {
                any.setProperty(key, value)
            }
        }
        return any
    }

}