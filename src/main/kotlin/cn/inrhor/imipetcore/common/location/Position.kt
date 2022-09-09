package cn.inrhor.imipetcore.common.location

import cn.inrhor.imipetcore.api.manager.MetaManager.getMeta
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.OptionManager.model
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
import cn.inrhor.imipetcore.common.model.ModelSelect
import com.ticxo.modelengine.api.ModelEngineAPI
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

/**
 * Entity 宠物或其它实体
 * @param entity 其它实体
 * @return 距离
 */
fun Entity.distanceLoc(entity: Entity, def: Double = 1000000.0): Double {
    if (world != entity.world) return def
    if (hasMetadata("imipetcore_entity")) {
        val owner = getOwner()
        val p = getMeta("entity")
        if (p != null) {
            val pet = owner?.getPet(p.toString())
            val modelOption = pet?.petEntity?.model()
            when (modelOption?.select) {
                ModelSelect.MODEL_ENGINE -> {
                    val base = ModelEngineAPI.getModeledEntity(uniqueId).base
                    val loc = base.location
                    val hitbox = base.hitbox
                    val v = Vector(
                        loc.x + hitbox.maxWidth,
                        loc.y + hitbox.height,
                        loc.z + hitbox.maxWidth
                    )
                    return v.distance(entity.boundingBox.max)
                }
            }
        }
    }
    return boundingBox.max.distance(entity.boundingBox.max)
}