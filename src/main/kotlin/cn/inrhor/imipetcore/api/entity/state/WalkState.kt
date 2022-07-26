package cn.inrhor.imipetcore.api.entity.state

import cn.inrhor.imipetcore.api.entity.PetEntity
import org.bukkit.entity.Entity

/**
 * 行走状态
 */
class WalkState: ActiveState() {

    override val action = "walk"

    override fun run(petEntity: PetEntity, target: Entity?) {
    }

    override fun end(petEntity: PetEntity) {
    }

}