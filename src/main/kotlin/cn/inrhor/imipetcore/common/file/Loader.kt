package cn.inrhor.imipetcore.common.file

import cn.inrhor.imipetcore.api.manager.OptionManager.save
import cn.inrhor.imipetcore.common.option.ActionOption
import cn.inrhor.imipetcore.common.option.IconOption
import cn.inrhor.imipetcore.common.option.PetOption
import cn.inrhor.imipetcore.common.option.SkillOption
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