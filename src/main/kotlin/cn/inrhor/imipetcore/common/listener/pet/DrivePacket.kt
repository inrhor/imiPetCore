package cn.inrhor.imipetcore.common.listener.pet

import cn.inrhor.imipetcore.api.manager.MetaManager.getMeta
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.api.manager.ModelManager
import cn.inrhor.imipetcore.common.hook.protocol.PacketProtocol.packetRotation
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Coerce
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketReceiveEvent


object DrivePacket {

    @SubscribeEvent
    fun receive(ev: PacketReceiveEvent) {
        if (ev.isCancelled) return
        if (ev.packet.name != "PacketPlayInSteerVehicle") return
        val p = ev.player
        val vehicle = p.vehicle?: return
        val actionType = vehicle.getMeta("drive")?: return
        // 1.16.5及以下分别是a  b c
        val list = if (MinecraftVersion.isUniversal) listOf("c", "d", "e") else listOf("a", "b", "c")
        var swSpeed = ev.packet.read<Float>(list[0])?: return // 前进速度
        var adSpeed = ev.packet.read<Float>(list[1])?: return // 横向速度
        val att = vehicle.getPetData(p)?.attribute?: return
        val sp = Coerce.toFloat(att.speed)
        swSpeed = if (swSpeed > 0.0f) sp else if (swSpeed < 0.0f) -sp else swSpeed
        adSpeed = if (adSpeed > 0.0f) sp else if (adSpeed < 0.0f) -sp else adSpeed
        val jumping = ev.packet.read<Boolean>(list[2])?: return
        val pLoc = p.location
        if (MinecraftVersion.major > 5) {
            vehicle.setRotation(pLoc.yaw, pLoc.pitch)
        }else {
            // 获取附近玩家包括自己
            val nearPlayer = vehicle.getNearbyEntities(36.0, 36.0, 36.0).filterIsInstance<Player>().toSet()
            vehicle.packetRotation(nearPlayer, pLoc.yaw, pLoc.pitch)
        }
        val forwardDir = pLoc.direction
        val sideways = forwardDir.clone().crossProduct(Vector(0, -1, 0))
        val total = forwardDir.multiply(adSpeed/10).add(sideways.multiply(swSpeed/5))
        if (actionType == ModelManager.ActionType.FLY) {
            total.y = if (jumping && pLoc.y < 200) 0.5 else 0.0
        }else {
            total.y = (if (jumping && vehicle.isOnGround) 0.5 else 0.0)
        }
        if (!vehicle.isOnGround) total.multiply(0.4)
        vehicle.velocity = vehicle.velocity.add(total)
    }

    @SubscribeEvent
    fun drop(ev: EntityDamageEvent) {
        if (ev.cause == EntityDamageEvent.DamageCause.FALL) {
            ev.entity.getMeta("drive")?: return
            ev.isCancelled = true
        }
    }

}