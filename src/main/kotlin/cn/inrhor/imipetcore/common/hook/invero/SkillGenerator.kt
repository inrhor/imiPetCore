package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator
import cn.inrhor.imipetcore.api.manager.SkillManager.getAllSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUnloadSkills
import cn.inrhor.imipetcore.common.database.data.PetData
import taboolib.common.platform.function.info

class SkillGenerator: ContextGenerator() {

    var uiType = UiTypeSkill.LOAD

    override fun generate(context: Context) {
        val petData = context.variables["pet_data"] as PetData

        info("petData $petData  name "+petData.javaClass.simpleName)

        info("list "+uiType.list(petData).toString())

        info("uiType $uiType")

        generated = /*uiType.list(petData)*/petData.getUnloadSkills().map {
            info("it>>>   ${it.id}")
            sourceObject {
                put("self_skill", it) // 目的传递给下一个菜单
                put("id", it.id)
                put("name", petData.name)
            }
        }
    }
}
