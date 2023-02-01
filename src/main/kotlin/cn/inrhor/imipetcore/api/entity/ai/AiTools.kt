package cn.inrhor.imipetcore.api.entity.ai

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.entity.ai.nms.NmsAiGoal.addNmsAi
import cn.inrhor.imipetcore.api.entity.ai.simple.AttackAi
import cn.inrhor.imipetcore.api.entity.ai.simple.HookAi
import cn.inrhor.imipetcore.api.entity.ai.simple.ModelActionAi
import cn.inrhor.imipetcore.api.entity.ai.simple.WalkAi
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiAttack
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiHook
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiModelAction
import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAiWalk
import cn.inrhor.imipetcore.api.manager.OptionManager.getActionOption
import org.bukkit.entity.LivingEntity
import taboolib.module.ai.addGoalAi
import taboolib.module.nms.MinecraftVersion

private fun versionAiAdd(action: String, vararg ai: () -> Unit) {
    when (action) {
        "animation" -> {
            ai[0].invoke()
        }
        "attack" -> {
            ai[1].invoke()
        }
        "walk" -> {
            ai[2].invoke()
        }
        else -> {
            ai[3].invoke()
        }
    }
}

fun LivingEntity.addAi(petEntity: PetEntity, action: String, priority: Int) {
    if (ImiPetCore.config.getString("nms") == "mod") {
        if (MinecraftVersion.major == 4) {
            versionAiAdd(action, {
                    addNmsAi(UniversalAiModelAction(petEntity), 0)
                },{
                addNmsAi(UniversalAiAttack(petEntity), priority)
            }, {
                addNmsAi(UniversalAiWalk(petEntity), priority)
            }, {
                val actionOption = action.getActionOption()
                if (actionOption != null) {
                    addNmsAi(UniversalAiHook(actionOption, petEntity), priority)
                }
            })
        }
    }else {
        versionAiAdd(action, {
            addGoalAi(ModelActionAi(petEntity), 0)
        },{
            addGoalAi(AttackAi(petEntity), priority)
        }, {
            addGoalAi(WalkAi(petEntity), priority)
        }, {
            val actionOption = action.getActionOption()
            if (actionOption != null) {
                addGoalAi(HookAi(actionOption, petEntity), priority)
            }
        })
    }
}