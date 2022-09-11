package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiAttack
import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiGoal.addNmsAi
import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiHook
import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiWalk
import cn.inrhor.imipetcore.api.entity.ai.simple.AttackAi
import cn.inrhor.imipetcore.api.entity.ai.simple.HookAi
import cn.inrhor.imipetcore.api.entity.ai.simple.WalkAi
import cn.inrhor.imipetcore.api.manager.OptionManager.getActionOption
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.addGoalAi
import taboolib.module.nms.MinecraftVersion

fun LivingEntity.addAi(petEntity: PetEntity, action: String, priority: Int) {
    if (ImiPetCore.config.getString("nms") == "mod") {
        if (MinecraftVersion.major == 4) {
            when (action) {
                "attack" -> {
                    addNmsAi(NmsAiAttack(petEntity), priority)
                }
                "walk" -> {
                    addNmsAi(NmsAiWalk(petEntity), priority)
                }
                else -> {
                    val actionOption = action.getActionOption()
                    if (actionOption != null) {
                        addNmsAi(NmsAiHook(actionOption, petEntity), priority)
                    }
                }
            }
        }
    }else {
        when (action) {
            "attack" -> {
                addGoalAi(AttackAi(petEntity), priority)
            }
            "walk" -> {
                addGoalAi(WalkAi(petEntity), priority)
            }
            else -> {
                val actionOption = action.getActionOption()
                if (actionOption != null) {
                    addGoalAi(HookAi(actionOption, petEntity), priority)
                }
            }
        }
    }
}