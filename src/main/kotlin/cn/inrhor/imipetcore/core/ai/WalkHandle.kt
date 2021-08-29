package cn.inrhor.imipetcore.core.ai

import cn.inrhor.imipetcore.core.entity.PetData
import com.ticxo.modelengine.api.model.ActiveModel
import org.bukkit.Location
import org.bukkit.World
import taboolib.common.platform.function.submit
import taboolib.module.navigation.*


object WalkHandle {

    fun run(start: Location, target: Location, petData: PetData) {
        if (start.world!!.name != target.world!!.name) {
            error("cannot request navigation in different worlds.")
        }
        val modeledEntity = petData.modeledEntity
        val height = modeledEntity.height.toDouble()
        val width = modeledEntity.width.toDouble()
        val entity = NodeEntity(start, height, width)
        val pathfinder = createPathfinder(entity)
        val path = pathfinder.findPath(target, distance = 3f)?: return
        val list = mutableListOf<Double>()

        // 化为两位小数点

        petData.activeModel.setState(ActiveModel.ModelState.WALK)
        toPath(petData, path, 0, target.world!!, target.yaw, target.pitch)
    }

    fun toPath(petData: PetData, path: Path, index: Int, world: World, yaw: Float, pith: Float) {
        if (index >= path.nodes.size) {
            petData.activeModel.setState(ActiveModel.ModelState.IDLE)
            return
        }
        submit(async = true, delay = 3) {
            val node = path.nodes[index]
            petData.position.set(node.x.toDouble(), node.y.toDouble(), node.z.toDouble())
            toPath(petData, path, index+1, world, yaw, pith)
        }
    }
}