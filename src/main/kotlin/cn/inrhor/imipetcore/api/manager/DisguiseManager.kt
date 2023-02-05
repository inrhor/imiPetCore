package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import cn.inrhor.imipetcore.common.model.ModelSelect
import cn.inrhor.imipetcore.common.nms.NMS
import cn.inrhor.imipetcore.server.PluginLoader.protocolLibLoad
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object DisguiseManager {

    /**
     * 伪装实体
     */
    fun Entity.disguise(entityType: String) {
        val players = Bukkit.getOnlinePlayers().toSet()
        if (protocolLibLoad) {

        }else {
            NMS.INSTANCE.destroyEntity(players, entityId)
            NMS.INSTANCE.spawnEntityLiving(players, this, entityType)
        }
    }

    /**
     * 进入游戏伪装实体
     */
    fun Player.lookDisguise() {
        Bukkit.getOnlinePlayers().forEach {
            if (it != this) {
                it.followingPet().forEach { pet ->
                    if (pet.model().select == ModelSelect.COMMON) {
                        pet.entity?.let { entity ->
                            NMS.INSTANCE.entityRotation(setOf(this), entity.entityId, entity.location.yaw, entity.location.pitch)
                        }
                    }
                }
            }
        }
    }

}