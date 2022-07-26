package cn.inrhor.imipetcore.common.database.data

/**
 * 玩家数据
 *
 * @param petDataList 宠物数据
 */
data class PlayerData(val petDataList: MutableList<PetData> = mutableListOf())