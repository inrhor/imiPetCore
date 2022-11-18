package cn.inrhor.imipetcore.common.option

/**
 * 技能配置
 */
class SkillOption(var id: String = "null", val name: String = "null", val script: String = "",
                  val tree: TreeSkillOption = TreeSkillOption(),
                  val coolDown: String = "0") {
}

class TreeSkillOption(val point: Int = 0, val select: List<String> = listOf())