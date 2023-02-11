package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.api.manager.PetManager.followingPetData
import cn.inrhor.imipetcore.common.hook.protocol.PacketProtocol.destroyEntity
import cn.inrhor.imipetcore.common.hook.protocol.PacketProtocol.spawnEntity
import cn.inrhor.imipetcore.common.hook.protocol.ProtocolEntity.protocolEntityId
import cn.inrhor.imipetcore.common.model.ModelSelect
import cn.inrhor.imipetcore.common.nms.NMS
import cn.inrhor.imipetcore.server.ReadManager.protocolLibLoad
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

object DisguiseManager {

    /**
     * 伪装实体
     */
    fun Entity.disguise(entityType: EntityType) {
        val players = Bukkit.getOnlinePlayers().toSet()
        if (protocolLibLoad) {
            destroyEntity(players)
            spawnEntity(players, entityType.protocolEntityId())
        }else {
            NMS.INSTANCE.destroyEntity(players, entityId)
            NMS.INSTANCE.spawnEntityLiving(players, this, entityType.name)
        }
    }

    /**
     * 进入游戏伪装实体
     */
    fun Player.lookDisguise() {
        Bukkit.getOnlinePlayers().forEach {
            if (it != this) {
                it.followingPetData().forEach { data ->
                    val pet = data.petEntity
                    if (pet != null) {
                        if (pet.model().select == ModelSelect.COMMON) {
                            pet.entity?.let { entity ->
                                val loc = entity.location
                                destroyEntity(setOf(this))
                                spawnEntity(setOf(this), entity.type.protocolEntityId())
                            }
                        }
                    }
                }
            }
        }
    }

}