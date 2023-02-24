package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator
import cn.inrhor.imipetcore.api.manager.PetManager.followingPetData
import cn.inrhor.imipetcore.api.manager.PetManager.getPets
import org.bukkit.entity.Player

/**
 * 玩家的所有宠物容器
 */
class PetsGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        val player = context.player

        generated = player.getPets().map {
            sourceObject {
                // https://invero.trixey.cc/docs/advance/basic/context
                // context set pet_data to element self_pet
                put("self_pet", it)
                put("name", it.name)
            }
        }
    }

}

/**
 * 玩家的跟随宠物容器
 */
class FollowPetsGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        val player = context.player

        generated = player.followingPetData().map {
            sourceObject {
                put("self_pet", it)
                put("name", it.name)
            }
        }
    }

}