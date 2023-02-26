package cn.inrhor.imipetcore.common.file

import cc.trixey.invero.common.Invero
import cn.inrhor.imipetcore.api.manager.OptionManager.save
import cn.inrhor.imipetcore.common.hook.invero.*
import cn.inrhor.imipetcore.common.option.ActionOption
import cn.inrhor.imipetcore.common.option.IconOption
import cn.inrhor.imipetcore.common.option.PetOption
import cn.inrhor.imipetcore.common.option.SkillOption
import cn.inrhor.imipetcore.server.ReadManager.inveroLoad
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.getObject

/**
 * 加载宠物文件
 */
fun loadPet() {
    val folder = getFile("pet", "PET_EMPTY_FILE", true)
    getFileList(folder).forEach {
        val option = Configuration.loadFromFile(it).getObject<PetOption>("pet", false)
        option.save()
    }
}

/**
 * 加载动作行为Ai文件
 */
fun loadAction() {
    val folder = getFile("action", "ACTION_EMPTY_FILE", true)
    getFileList(folder).forEach {
       val yaml = Configuration.loadFromFile(it)
        yaml.getConfigurationSection("action")?.getKeys(false)?.forEach { e ->
            val option = yaml.getObject<ActionOption>("action.$e", false)
            option.name = e
            option.save()
        }
    }
}

/**
 * 加载技能文件
 */
fun loadSkill() {
    val folder = getFile("skill", "SKILL_EMPTY_FILE", true)
    getFileList(folder).forEach {
        val yaml = Configuration.loadFromFile(it)
        yaml.getConfigurationSection("skill")?.getKeys(false)?.forEach { e ->
            val option = yaml.getObject<SkillOption>("skill.$e", false)
            option.id = e
            option.save()
        }
        yaml.getConfigurationSection("icon")?.getKeys(false)?.forEach { e ->
            val option = yaml.getObject<IconOption>("icon.$e", false)
            option.id = e
            option.save()
        }
    }
}

/**
 * 加载Invero Generator
 */
fun loadInvero() {
    if (inveroLoad) {
        val api = Invero.API.getRegistry()
        val a = "imiPetCore"
        api.registerElementGenerator(a, UiTypePet.ALL_PET.uiName(), AllPetGenerator())
        api.registerElementGenerator(a, UiTypePet.FOLLOW_PET.uiName(), FollowPetGenerator())
        api.registerElementGenerator(a, UiTypeSkill.LOAD.uiName(), LoadSkillGenerator())
        api.registerElementGenerator(a, UiTypeSkill.UNLOAD.uiName(), UnloadSkillGenerator())
        api.registerElementGenerator(a, UiTypeSkill.UPDATE.uiName(), UpdateSkillGenerator())
        api.registerElementGenerator(a, UiTypeSkill.POINT.uiName(), PointSkillGenerator())
        api.registerElementGenerator(a, UiTypeSkill.LOAD_SLOT.uiName(), LoadSkillGenerator())
        api.registerElementGenerator(a, UiTypeSkill.UPDATE_SELECT.uiName(),
            UpdateSelectSkillGenerator())
    }
}