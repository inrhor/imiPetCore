package cn.inrhor.imipetcore.core.entity

import com.ticxo.modelengine.api.model.ModeledEntity
import org.bukkit.entity.Player

/**
 * 实体可视数据
 */
class ViewersData(var modeledEntity: ModeledEntity) {

    val viewers = mutableSetOf<Player>()

    fun displayModel() {
        viewers.forEach {
            modeledEntity.addPlayerAsync(it)
        }
    }

}