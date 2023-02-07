package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.common.nms.NMS
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

object Controller {

    /**
     * 攻击实体
     */
    fun LivingEntity.attackEntity(entity: Entity) {
        NMS.INSTANCE.attack(this, entity)
    }

}