package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator

/**
 * 玩家的所有宠物容器
 */
class AllPetGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.petGenerate(context, UiTypePet.ALL_PET)
    }

}

class FollowPetGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.petGenerate(context, UiTypePet.FOLLOW_PET)
    }

}