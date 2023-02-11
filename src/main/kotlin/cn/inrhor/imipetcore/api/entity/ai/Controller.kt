package cn.inrhor.imipetcore.api.entity.ai

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Wolf

object Controller {

    /**
     * 攻击实体
     */
    fun LivingEntity.attackEntity(entity: Entity?) {
        if (this is Wolf) {
            if (entity != null) {
                if (entity is LivingEntity) {
                    target = entity
                }
            }else target = null
        }
    }

}