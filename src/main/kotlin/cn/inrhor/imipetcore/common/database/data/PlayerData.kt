package cn.inrhor.imipetcore.common.database.data

import org.bukkit.entity.Entity

/**
 * 玩家数据
 *
 * @param petDataList 宠物数据
 */
data class PlayerData(
    val petDataList: MutableList<PetData> = mutableListOf(),
    val castSkillData: CastSkillData = CastSkillData(),
    val commonData: CommonData = CommonData()
)

/**
 * 技能选择目标数据
 */
class CastSkillData(var skill: String = "", var petData: PetData? = null)

/**
 * 其他数据
 */
class CommonData(var attacker: Entity? = null)