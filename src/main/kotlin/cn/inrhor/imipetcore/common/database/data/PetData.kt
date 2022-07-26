package cn.inrhor.imipetcore.common.database.data

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption
import java.util.*


/**
 * 宠物数据
 */
data class PetData(@Transient var uuid: String = "null_uuid", val id: String = "null_id", var following: Boolean = false, val attribute: AttributeData = AttributeData()) {

    fun petOption() = id.petOption()

    fun uniqueId() = UUID.fromString(uuid)

    @Transient
    var petEntity: PetEntity? = null
}

/**
 * 属性数据
 */
data class AttributeData(var currentHP: Double = 20.0, var maxHP: Double = 20.0, var speed: Double = 1.0, var attack: Double = 0.0)
