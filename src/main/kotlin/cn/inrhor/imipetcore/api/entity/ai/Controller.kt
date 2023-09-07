package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.PetManager.hasAction
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Wolf

object Controller {

    /**
     * 攻击实体
     *
     * @param ignore 是否忽略有无attack行为
     */
    fun LivingEntity.attackEntity(entity: Entity?, ignore: Boolean = true, petEntity: PetEntity? = null) {
        if (this is Wolf) {
            if (entity != null) {
                if (entity is LivingEntity) {
                    target = if (!ignore && petEntity?.hasAction("attack") == false) {
                        null
                    }else {
                        entity
                    }
                }
            }else target = null
        }
    }

}