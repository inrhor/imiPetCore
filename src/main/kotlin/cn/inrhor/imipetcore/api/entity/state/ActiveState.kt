package cn.inrhor.imipetcore.api.entity.state

import cn.inrhor.imipetcore.api.entity.PetEntity
import org.bukkit.Location
import org.bukkit.entity.Entity

/**
 * 动作状态抽象
 */
abstract class ActiveState(var entity: Entity? = null, var location: Location? = null, var delay: Int = 0) {

    abstract val action: String

    /**
     * 执行
     */
    abstract fun run(petEntity: PetEntity, target: Entity? = null)

    /**
     * 终止
     */
    abstract fun end(petEntity: PetEntity)

}