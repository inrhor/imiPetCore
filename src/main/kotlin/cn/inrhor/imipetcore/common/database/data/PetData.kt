package cn.inrhor.imipetcore.common.database.data

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption


/**
 * 宠物数据
 */
data class PetData(@Transient var name: String = "null_name", val id: String = "null_id", var following: Boolean = false, val attribute: AttributeData = AttributeData(),
                   var currentExp: Int = 0, var maxExp: Int = 100, var level: Int = 1) {

    fun petOption() = id.petOption()

    @Transient
    var petEntity: PetEntity? = null
}

/**
 * 属性数据
 */
data class AttributeData(var currentHP: Double = 20.0, var maxHP: Double = 20.0, var speed: Double = 1.0, var attack: Double = 0.0, var attack_speed: Int = 2)
