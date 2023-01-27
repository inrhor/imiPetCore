package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.skillOptionMap
import cn.inrhor.imipetcore.api.event.PetChangeEvent
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.data.SkillData
import cn.inrhor.imipetcore.common.option.ItemElement
import cn.inrhor.imipetcore.common.option.SkillOption
import org.bukkit.entity.Player

/**
 * 宠物技能管理器
 */
object SkillManager {

    /**
     * @return 宠物技能数据
     */
    fun String.skillData(petData: PetData): SkillData? {
        return petData.getSkillData(this)
    }

    /**
     * @return 技能配置
     */
    fun String.skillOption(): SkillOption? {
        return skillOptionMap[this]
    }

    /**
     *  为宠物添加新技能
     */
    fun PetData.addNewSkill(owner: Player, id: String, unload: Boolean = true, slot: Int = 0) {
        val option = id.skillOption()?: return
        val data = SkillData(id, option.name)
        if (unload) {
            skillSystemData.unloadSkill.add(data)
        }else loadSkill(owner, data, slot)
        PetChangeEvent(owner, this).call()
    }

    /**
     * 为宠物装载技能
     *
     * @param slot 装载槽位
     */
    fun PetData.loadSkill(owner: Player, skillData: SkillData, slot: Int = 0) {
        val number = skillSystemData.number
        val load = skillSystemData.loadSkill
        val size = load.size
        if (number > size) {
            load.add(skillData)
        }else {
            if (load.isNotEmpty()) {
                skillSystemData.unloadSkill.add(skillSystemData.loadSkill[slot])
                skillSystemData.loadSkill[slot] = skillData
            }
        }
        removeUnloadSkill(skillData.id)
        PetChangeEvent(owner, this).call()
    }

    /**
     * 为宠物卸载技能
     *
     * @param slot 卸载何处技能
     */
    fun PetData.unloadSkill(owner: Player, slot: Int) {
        val load = skillSystemData.loadSkill
        if (load.isEmpty()) return
        val size = load.size
        val s = if (slot <= size-1) slot else 0
        // 置于未装载技能列表末尾
        skillSystemData.unloadSkill.add(load[s])
        load.removeAt(s)
        PetChangeEvent(owner, this).call()
    }

    /**
     * 移除未装载技能里的技能
     */
    fun PetData.removeUnloadSkill(id: String) {
        val unload = skillSystemData.unloadSkill
        if (unload.isEmpty()) return
        val data = unload.find { it.id == id }
        if (data != null) {
            unload.remove(data)
        }
    }

    /**
     * @return 宠物技能数据
     */
    fun PetData.getSkillData(id: String): SkillData? {
        getAllSkills().forEach {
            if (it.id == id) return it
        }
        return null
    }

    /**
     * 为宠物增加技能点
     */
    fun PetData.addPoint(owner: Player, int: Int) {
        skillSystemData.point += int
        PetChangeEvent(owner, this).call()
    }

    /**
     * 为宠物扣除技能点
     *
     * @return 操作成否
     */
    fun PetData.delPoint(owner: Player, int: Int): Boolean {
        if (skillSystemData.point >= int) {
            skillSystemData.point -= int
            PetChangeEvent(owner, this).call()
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
    fun PetData.addSkillPoint(owner: Player, skillData: SkillData, int: Int, del: Boolean = true): Boolean {
        if (del) {
            if (!delPoint(owner, int)) return false
        }
        skillData.point += int
        PetChangeEvent(owner, this).call()
        return true
    }

    /**
     * 为宠物技能减少技能点
     *
     * @return 操作成否
     */
    fun PetData.delSkillPoint(owner: Player, skillData: SkillData, int: Int): Boolean {
        skillData.point -= int
        PetChangeEvent(owner, this).call()
        return true
    }

    /**
     * 为宠物技能设置技能点
     *
     * @return 操作成否
     */
    fun PetData.setSkillPoint(owner: Player, skillData: SkillData, int: Int): Boolean {
        skillData.point = int
        PetChangeEvent(owner, this).call()
        return true
    }

    /**
     * 更替宠物技能
     */
    fun PetData.replaceSkill(owner: Player, skillData: SkillData, new: String) {
        val opt = new.skillOption()?: return
        skillData.id = opt.id
        skillData.skillName = opt.name
        skillData.coolDown = 0
        skillData.point = 0
        PetChangeEvent(owner, this).call()
    }

    /**
     * 删除宠物技能
     *
     * @return 操作成否
     */
    fun PetData.removeSkill(player: Player, id: String): Boolean {
        val n = skillSystemData.loadSkill.iterator()
        while (n.hasNext()) {
            val a = n.next()
            if (a.id == id) {
                n.remove()
                PetChangeEvent(player, this).call()
                return true
            }
        }
        val m = skillSystemData.unloadSkill.iterator()
        while (m.hasNext()) {
            val a = m.next()
            if (a.id == id) {
                m.remove()
                PetChangeEvent(player, this).call()
                return true
            }
        }
        return false
    }

    /**
     * @return 宠物所有装载的技能
     */
    fun PetData.getLoadSkills(): MutableList<SkillData> {
        return skillSystemData.loadSkill
    }

    /**
     * @return 宠物所有未装载的技能
     */
    fun PetData.getUnloadSkills(): MutableList<SkillData> {
        return skillSystemData.unloadSkill
    }

    /**
     * @return 技能配置
     */
    fun SkillData.skillOption(): SkillOption? {
        return id.skillOption()
    }

    /**
     * @return 技能图标
     */
    fun SkillData.icon(): ItemElement {
        return skillOption()?.icon?: ItemElement()
    }

    /**
     * @return 可升级的技能
     */
    fun PetData.getUpdateSkills(): MutableList<SkillData> {
        val list = mutableListOf<SkillData>()
        getAllSkills().forEach {
            if (it.fillPoint() && (it.id.skillOption()?.tree?.select?: listOf()).isNotEmpty()) list.add(it)
        }
        return list
    }

    /**
     * @return 满足技能点
     */
    fun SkillData.fillPoint(): Boolean {
        val p = skillOption()?.tree?.point ?: 0
        if (p <= 0) return false
        return point >= p
    }


    /**
     * @return 全部技能
     */
    fun PetData.getAllSkills(): MutableList<SkillData> {
        val list = mutableListOf<SkillData>()
        list.addAll(skillSystemData.loadSkill)
        list.addAll(skillSystemData.unloadSkill)
        return list
    }

    /**
     * @return 技能树配置集
     */
    fun SkillData.treeSkillOption(): MutableList<SkillOption> {
        val list = mutableListOf<SkillOption>()
        skillOption()?.tree?.select?.forEach {
            val opt = it.skillOption()
            if (opt != null) list.add(opt)
        }
        return list
    }

    /**
     * 宠物技能冷却设置
     */
    fun PetData.setCoolDown(owner: Player, skillData: SkillData, int: Int) {
        skillData.coolDown = int
        PetChangeEvent(owner, this).call()
    }

    /**
     * 宠物技能冷却减少
     */
    fun PetData.delCoolDown(owner: Player, skillData: SkillData, int: Int) {
        skillData.coolDown -= int
        PetChangeEvent(owner, this).call()
    }

    /**
     * 宠物技能冷却增加
     */
    fun PetData.addCoolDown(owner: Player, skillData: SkillData, int: Int) {
        skillData.coolDown += int
        PetChangeEvent(owner, this).call()
    }

}