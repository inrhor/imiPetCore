package cn.inrhor.imipetcore.common.option

/**
 * 技能配置
 */
class SkillOption(var id: String = "null", val name: String = "null", val script: String = "",
                  val tree: TreeSkillOption = TreeSkillOption(),
                  val coolDown: String = "0", val icon: ItemElement = ItemElement())

/**
 * 技能树配置
 */
class TreeSkillOption(val point: Int = 0, val select: List<String> = listOf())

/**
 * 技能图标配置
 */
class IconOption(var id: String = "", val item: ItemElement = ItemElement())