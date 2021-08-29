package cn.inrhor.imipetcore.core.entity

import cn.inrhor.imipetcore.core.position.Position
import com.ticxo.modelengine.api.ModelEngineAPI
import org.bukkit.Location
import java.util.*

/**
 * 宠物数据
 */
class PetData(val index: Int, val uuid: UUID, var position: Position, val modelID: String) {

    fun getLocation(): Location {
        return position.clone().toLocation()
    }

    val modelPet = ModelPet(this)

    val modeledEntity = ModelEngineAPI.api.modelManager.createModeledEntity(modelPet)

    val activeModel = ModelEngineAPI.api.modelManager.createActiveModel(modelID)

    val viewersDate = ViewersData(modeledEntity)

    var follow = false

    fun follow(setFollow: Boolean = follow) {
        if (setFollow) {
            viewersDate.displayModel()
        }else {
            removeModel()
        }
    }

    init {
        modeledEntity.addActiveModel(activeModel)
    }

    fun removeModel() {
        modeledEntity.removeModel(modelID)
    }

}