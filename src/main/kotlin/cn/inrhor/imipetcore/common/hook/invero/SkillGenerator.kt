package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator
import cn.inrhor.imipetcore.api.manager.SkillManager.getLoadSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUnloadSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUpdateSkills
import cn.inrhor.imipetcore.common.database.data.PetData

/**
 * 装载技能容器
 */
class LoadSkillGenerator: ContextGenerator() {
    override fun generate(context: Context) {
        val petData = context.variables["@pet_data"] as PetData
        generated = petData.getLoadSkills().map {
            sourceObject {
                put("self_pet", petData)
                put("self_skill", it)
                put("name", it.skillName)
            }
        }
    }
}

/**
 * 未装载技能容器
 */
class UnLoadSkillGenerator: ContextGenerator() {
    override fun generate(context: Context) {
        val petData = context.variables["@pet_data"] as PetData
        generated = petData.getUnloadSkills().map {
            sourceObject {
                put("self_pet", petData)
                put("self_skill", it)
                put("name", it.skillName)
            }
        }
    }
}

/**
 * 可升级技能容器
 */
class UpdateSkillGenerator: ContextGenerator() {
    override fun generate(context: Context) {
        val petData = context.variables["@pet_data"] as PetData
        generated = petData.getUpdateSkills().map {
            sourceObject {
                put("self_pet", petData)
                put("self_skill", it)
                put("name", it.skillName)
            }
        }
    }
}