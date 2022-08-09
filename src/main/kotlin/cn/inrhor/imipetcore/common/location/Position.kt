package cn.inrhor.imipetcore.common.location

import cn.inrhor.imipetcore.api.manager.MetaManager.getMeta
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
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
            val model = pet?.petEntity?.modelEntity
            if (model != null) {
                val e = model.entity
                val v = Vector(e.location.x+model.width/2, e.location.y+model.height, e.location.z+model.width/2)
                return v.distance(entity.boundingBox.max)
            }
        }
    }
    return boundingBox.max.distance(entity.boundingBox.max)
}