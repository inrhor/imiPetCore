package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.skillOptionMap
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.data.SkillData
import cn.inrhor.imipetcore.common.option.SkillOption

/**
 * 宠物技能管理器
 */
object SkillManager {

    /**
     * @return 技能配置
     */
    fun String.skillOption(): SkillOption? {
        return skillOptionMap[this]
    }

    /**
     *  为宠物添加新技能
     */
    fun PetData.addNewSkill(id: String, unload: Boolean = true, slot: Int = 0) {
        val option = id.skillOption()?: return
        val data = SkillData(id, option.name)
        if (unload) {
            skillSystemData.unloadSkill.add(data)
        }else loadSkill(data, slot)
    }

    /**
     * 为宠物装载技能
     */
    fun PetData.loadSkill(skillData: SkillData, slot: Int = 0) {
        val number = skillSystemData.number
        val load = skillSystemData.loadSkill
        val size = load.size
        if (number > size) {
            load.add(skillData)
        }else {
            if (load.isEmpty()) return
            val s = if (slot <= size-1) slot else 0
            skillSystemData.unloadSkill[s] = load[s]
            load[slot] = skillData
        }
    }

    /**
     * 为宠物卸载技能
     */
    fun PetData.unloadSkill(slot: Int) {
        val load = skillSystemData.loadSkill
        if (load.isEmpty()) return
        val size = load.size
        val s = if (slot <= size-1) slot else 0
        skillSystemData.unloadSkill[s] = load[s]
        load.removeAt(s)
    }

}