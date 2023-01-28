package cn.inrhor.imipetcore.common.database.data

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.api.manager.OptionManager.petOption


/**
 * 宠物数据
 */
data class PetData(
    @Transient var name: String = "null_name", var id: String = "null_id", var following: Boolean = false,
    val attribute: AttributeData = AttributeData(),
    var currentExp: Int = 0, var maxExp: Int = 100, var level: Int = 1,
    val skillSystemData: SkillSystemData = SkillSystemData()
) {

    fun petOption() = id.petOption()

    @Transient
    var petEntity: PetEntity? = null

    /**
     * @return 宠物死亡
     */
    fun isDead(): Boolean {
        return petEntity?.entity?.isDead?: (attribute.currentHP <= 0)
    }

    fun isFollow(): Boolean {
        if (isDead()) following = false
        return following
    }
}

/**
 * 属性数据
 */
data class AttributeData(var currentHP: Double = 20.0, var maxHP: Double = 20.0, var speed: Double = 1.0, var attack: Double = 0.0, var attack_speed: Int = 2)

/**
 * 技能系统数据
 */
data class SkillSystemData(
    var point: Int = 0,
    var number: Int = 3,
    var loadSkill: MutableList<SkillData> = mutableListOf(),
    var unloadSkill: MutableList<SkillData> = mutableListOf())

/**
 * 技能数据
 */
data class SkillData(var id: String = "null", var skillName: String = "null",
                     var point: Int = 0, var coolDownTime: Long = 0)