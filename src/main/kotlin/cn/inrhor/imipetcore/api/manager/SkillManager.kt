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

    /**
     * @return 宠物技能数据
     */
    fun PetData.getSkillData(id: String): SkillData? {
        skillSystemData.loadSkill.forEach {
            if (it.id == id) return it
        }
        skillSystemData.unloadSkill.forEach {
            if (it.id == id) return it
        }
        return null
    }

    /**
     * 为宠物增加技能点
     */
    fun PetData.addPoint(int: Int) {
        skillSystemData.point += int
    }

    /**
     * 为宠物扣除技能点
     *
     * @return 操作成否
     */
    fun PetData.delPoint(int: Int): Boolean {
        if (skillSystemData.point >= int) {
            skillSystemData.point -= int
            return true
        }
        return false
    }

    /**
     * 为宠物技能添加技能点
     *
     * @param del 是否扣除宠物技能点
     * @return 操作成否
     */
    fun PetData.addSkillPoint(id: String, int: Int, del: Boolean = true): Boolean {
        val skill = getSkillData(id)?: return false
        if (del && delPoint(int)) {
            skill.point += int
            return true
        }
        return false
    }

    /**
     * 更替宠物技能
     */
    fun PetData.replaceSkill(id: String, new: String) {
        val skill = getSkillData(id)?: return
        val opt = new.skillOption()?: return
        skill.id = opt.id
        skill.skillName = opt.name
        skill.coolDown = 0
        skill.point = 0
    }

    /**
     * 删除宠物技能
     *
     * @return 操作成否
     */
    fun PetData.removeSkill(id: String): Boolean {
        val n = skillSystemData.loadSkill.iterator()
        while (n.hasNext()) {
            val a = n.next()
            if (a.id == id) n.remove(); return true
        }
        val un = skillSystemData.unloadSkill.iterator()
        while (un.hasNext()) {
            val a = un.next()
            if (a.id == id) un.remove(); return true
        }
        return false
    }

}