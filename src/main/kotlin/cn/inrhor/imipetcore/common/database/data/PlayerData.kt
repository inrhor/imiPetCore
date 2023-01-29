package cn.inrhor.imipetcore.common.database.data

/**
 * 玩家数据
 *
 * @param petDataList 宠物数据
 */
data class PlayerData(
    val petDataList: MutableList<PetData> = mutableListOf(),
    val castSkillData: CastSkillData = CastSkillData())

/**
 * 技能选择目标数据
 */
class CastSkillData(var skill: String = "", var petData: PetData? = null)