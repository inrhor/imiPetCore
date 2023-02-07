package cn.inrhor.imipetcore.common.nms

import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiGoal.addAi
import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiGoal.versionPack
import cn.inrhor.imipetcore.common.nms.DataSerializerUtil.createDataSerializer
import cn.inrhor.imipetcore.server.ConfigRead.nms
import cn.inrhor.imipetcore.server.ReadManager.isUniversal
import cn.inrhor.imipetcore.server.ReadManager.major
import cn.inrhor.imipetcore.server.ReadManager.minor
import cn.inrhor.imipetcore.util.PositionUtil.rotate
import net.minecraft.network.PacketDataSerializer
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity
import net.minecraft.server.v1_9_R2.*
import net.minecraft.world.entity.EntityTypes
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity
import org.bukkit.entity.*
import org.bukkit.entity.Entity
import org.bukkit.event.entity.EntityTargetEvent
import taboolib.common5.cbyte
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.nms.sendPacket

class NMSImpl: NMS() {

    /**
     * 1.13- 旋转实体
     */
    override fun entityRotation(players: Set<Player>, entityId: Int, yaw: Float, pitch: Float) {
        sendPacket(
            players,
            PacketPlayOutEntity.PacketPlayOutEntityLook::class.java.unsafeInstance(),
            "a" to entityId,
            "e" to yaw.rotate().cbyte,
            "f" to pitch.rotate().cbyte
        )
    }

    override fun spawnEntity(players: Set<Player>, entity: Entity, entityType: String) {
        val entityId = entity.entityId
        val uuid = entity.uniqueId
        val location = entity.location
        val yaw = (location.yaw * 256.0f / 360.0f).toInt().toByte()
        val pitch = (location.pitch * 256.0f / 360.0f).toInt().toByte()
        sendPacket(
            players,
            PacketPlayOutSpawnEntity(createDataSerializer {
            writeVarInt(entityId)
            writeUUID(uuid)
            when (minor) {
                0, 1, 2 -> writeVarInt(Class.forName("net.minecraft.core.IRegistry").getProperty<Any>("ENTITY_TYPE", isStatic = true)!!.invokeMethod<Int>("getId", getEntityType(entityType) as EntityTypes<*>)!!)
                3 -> writeVarInt(NMS1193.INSTANCE.entityTypeGetId(getEntityType(entityType)))
            }
            writeDouble(location.x)
            writeDouble(location.y)
            writeDouble(location.z)
            writeByte(pitch)
            writeByte(yaw)
            writeByte(yaw)
            writeVarInt(0)
            writeShort(0)
            writeShort(0)
            writeShort(0)
        }.build() as PacketDataSerializer))
    }

    override fun spawnEntityLiving(players: Set<Player>, entity: Entity, entityType: String) {
        val entityId = entity.entityId
        val uuid = entity.uniqueId
        val location = entity.location
        val yaw = (location.yaw * 256.0f / 360.0f).toInt().toByte()
        val pitch = (location.pitch * 256.0f / 360.0f).toInt().toByte()
        if (isUniversal) {
            if (major > 10) {
                spawnEntity(players, entity, entityType)
            }else {
                sendPacket(
                    players,
                    Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving").unsafeInstance(),
                    "id" to entityId,
                    "uuid" to uuid,
                    "type" to Class.forName("net.minecraft.core.IRegistry").getProperty<Any>("ENTITY_TYPE", isStatic = true)!!.invokeMethod<Int>("getId", getEntityType(entityType) as EntityTypes<*>)!!,
                    "x" to location.x,
                    "y" to location.y,
                    "z" to location.z,
                    "xd" to 0,
                    "yd" to 0,
                    "zd" to 0,
                    "yRot" to yaw,
                    "xRot" to pitch,
                    "yHeadRot" to yaw,
                )
            }
        }else {
            sendPacket(
                players,
                net.minecraft.server.v1_16_R1.PacketPlayOutSpawnEntityLiving(),
                "a" to entityId,
                "b" to uuid,
                "c" to when {
                    major > 5 -> net.minecraft.server.v1_16_R1.IRegistry.ENTITY_TYPE.a(getEntityType(entityType) as net.minecraft.server.v1_16_R1.EntityTypes<*>)
                    major == 5 -> net.minecraft.server.v1_13_R2.IRegistry.ENTITY_TYPE.a(getEntityType(entityType) as net.minecraft.server.v1_13_R2.EntityTypes<*>)
                    else -> EntityType.valueOf(entityType.uppercase()).typeId
                },
                "d" to location.x,
                "e" to location.y,
                "f" to location.z,
                "g" to 0,
                "h" to 0,
                "i" to 0,
                "j" to yaw,
                "k" to pitch,
                "l" to yaw,
                "m" to when {
                    major > 6 -> null
                    else -> DataWatcher(null)
                }
            )
        }
    }

    private fun getEntityType(entityTypes: String): Any {
        return net.minecraft.server.v1_16_R1.EntityTypes::class.java.getProperty<Any>(entityTypes.uppercase(), isStatic = true)!!
    }

    /**
     * 实现实体伪装
     */
    override fun destroyEntity(players: Set<Player>, entityId: Int) {
        if (isUniversal) {
            sendPacket(
                players,
                PacketPlayOutEntityDestroy(entityId)
            )
        }else {
            sendPacket(
                players,
                net.minecraft.server.v1_16_R1.PacketPlayOutEntityDestroy(entityId)
            )
        }
    }

    private fun addCommonAi(livingEntity: LivingEntity, priority: Int, pathfinderGoal: Any) {
        if (nms == "mod") {
            val entityCreature = livingEntity.getProperty<Any>("entity")!!
            val pathfinderGoalMeleeAttack = Class.forName(
                "$versionPack.$pathfinderGoal").invokeConstructor(
                entityCreature, 15.0, true)
            livingEntity.addAi(pathfinderGoalMeleeAttack, priority)
        }else {
            val nmsEntity = (livingEntity as CraftEntity).handle
            val entityInsentient = nmsEntity as EntityInsentient
            val goalSelector = entityInsentient.goalSelector
            val entityCreature = entityInsentient as EntityCreature
            goalSelector.a(priority, PathfinderGoalMeleeAttack(entityCreature, 15.0, true))
        }
    }

    override fun addAiAttack(livingEntity: LivingEntity, priority: Int) {
        if (nms == "mod") {
            val entityCreature = livingEntity.getProperty<Any>("entity")!!
            val pathfinderGoalMeleeAttack = Class.forName(
                "$versionPack.PathfinderGoalMeleeAttack").invokeConstructor(
                entityCreature, 15.0, true)
            livingEntity.addAi(pathfinderGoalMeleeAttack, priority)
        }else {
            val nmsEntity = (livingEntity as CraftEntity).handle
            val entityInsentient = nmsEntity as EntityInsentient
            val goalSelector = entityInsentient.goalSelector
            val entityCreature = entityInsentient as EntityCreature
            goalSelector.a(priority, PathfinderGoalMeleeAttack(entityCreature, 15.0, true))
        }
    }

    /**
     * 攻击实体
     */
    override fun attack(livingEntity: LivingEntity, entity: Entity) {
        val reason = EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET
        if (nms == "mod") {
            val entityCreature = livingEntity.getProperty<Any>("entity")!!
            val nmsTarget = entity.getProperty<Any>("entity")!!
            entityCreature.invokeMethod<Any>("setGoalTarget", nmsTarget, reason, true)
        }else {
            val nmsEntity = (livingEntity as CraftEntity).handle
            val entityInsentient = nmsEntity as EntityInsentient
            val nmsTarget = (entity as CraftLivingEntity).handle
            entityInsentient.setGoalTarget(nmsTarget, reason, true)
        }
    }

    private fun sendPacket(players: Set<Player>, packet: Any, vararg fields: Pair<String, Any?>) {
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