package cn.inrhor.imipetcore.common.listener

import cn.inrhor.imipetcore.api.manager.MetaManager.getMeta
import cn.inrhor.imipetcore.api.manager.ModelManager
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketReceiveEvent

object DrivePacket {

    @SubscribeEvent
    fun receive(ev: PacketReceiveEvent) {
        if (ev.isCancelled) return
        if (ev.packet.name != "PacketPlayInSteerVehicle") return
        val vehicle = ev.player.vehicle?: return
        val actionType = vehicle.getMeta("drive")?: return
        val swSpeed = ev.packet.read<Float>("c")?: return // 1.16.5及以下分别是a b
        val fwSpeed = ev.packet.read<Float>("d")?: return
        val jumping = ev.packet.read<Boolean>("e")?: return
        val pLoc = ev.player.location
        vehicle.setRotation(pLoc.yaw, pLoc.pitch)
        val forwardDir = pLoc.direction
        val sideways = forwardDir.clone().crossProduct(Vector(0, -1, 0))
        val total = forwardDir.multiply(fwSpeed/10).add(sideways.multiply(swSpeed/5))
        if (actionType == ModelManager.ActionType.FLY) {
            total.y = if (jumping) 0.5 else 0.0
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