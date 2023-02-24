package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator
import cn.inrhor.imipetcore.common.database.data.PetData

class SkillGenerator: ContextGenerator() {

    var uiType = UiTypeSkill.LOAD

    override fun generate(context: Context) {
        val petData = context.variables["pet_data"] as PetData

        generated = uiType.list(petData).map {
            sourceObject {
                put("self_skill", it) // 目的传递给下一个菜单
                put("id", it.id)
            }
        }
    }
}
