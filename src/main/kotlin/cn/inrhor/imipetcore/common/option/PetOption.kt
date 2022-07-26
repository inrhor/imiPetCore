package cn.inrhor.imipetcore.common.option

import org.bukkit.entity.EntityType

/**
 * 宠物配置
 */
class PetOption(val id: String = "", val default: DefaultOption = DefaultOption(), val entityType: EntityType = EntityType.PIG, val model: ModelOption = ModelOption())

/**
 * option.default
 */
class DefaultOption(val name: List<String> = listOf(), val attribute: OptionAttribute = OptionAttribute()
)

/**
 * option.default.attribute
 */
class OptionAttribute(val health: Double = 20.0, val speed: Double = 1.0,
                      val attack: Double = 0.0,
                      val attack_speed: Int = 2)

/**
 * 宠物配置model
 */
class ModelOption(val id: String = "", val action: MutableList<ActionOption> = mutableListOf())

class ActionOption(val id: String = "attack", val lerpin: Int = 0, val lerpout: Int = 1, val speed: Double = 1.0)