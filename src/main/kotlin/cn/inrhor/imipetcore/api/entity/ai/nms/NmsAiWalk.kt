package cn.inrhor.imipetcore.api.entity.ai.nms.v1_12_R1

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.NmsAi
import net.minecraft.server.v1_12_R1.Navigation
import taboolib.common.platform.function.info
import taboolib.module.ai.navigationMove

class TestAi(val petEntity: PetEntity): NmsAi() {

    private val navigation: Navigation? = null

    override fun a(): Boolean {
        info("aaa")
        return true
    }

    override fun c() {
        info("move")
        val owner = petEntity.owner
        val playerLoc = owner.location
        val pet = petEntity.entity?: return
        /*val petLoc = pet.location
        val pathFinder = createPathfinder(NodeEntity(playerLoc, owner.height, owner.width))
        val path = pathFinder.findPath(petLoc, distance = 5f)*/
        /*path!!.nodes.forEach {
            pet.nav
        }*/
        pet.navigationMove(owner, 2.0)
    }

}