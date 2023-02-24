package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator

/**
 * 玩家的所有宠物容器
 */
class PetGenerator: ContextGenerator() {

    var uiType = UiTypePet.ALL_PET

    override fun generate(context: Context) {
        val player = context.player

        generated = uiType.list(player).map {
            sourceObject {
                // https://invero.trixey.cc/docs/advance/basic/context
                // context set pet_data to element self_pet
                put("self_pet", it) // 目的传递给下一个菜单
                put("name", it.name)
            }
        }
    }

}