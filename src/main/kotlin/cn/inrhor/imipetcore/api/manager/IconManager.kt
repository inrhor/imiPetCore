package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.iconOptionMap
import cn.inrhor.imipetcore.api.manager.SkillManager.skillOption
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.data.SkillData
import cn.inrhor.imipetcore.common.option.ItemElement
import cn.inrhor.imipetcore.common.option.SkillOption
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.ScriptContext

object IconManager {

    /**
     * 根据ID获取图标物品
     */
    fun String.iconItem(): ItemElement? = iconOptionMap[this]?.item

    fun PetData.petVar(it: ScriptContext) {
        it.rootFrame().variables()["@PetData"] = this
    }

    fun SkillData.petVar(it: ScriptContext, petData: PetData) {
        it.rootFrame().variables()["@IdSkill"] = id
        it.rootFrame().variables()["@PetData"] = petData
        it.rootFrame().variables()["@PetSkillData"] = this
    }

    private fun SkillData.skillOptionDataVar(it: ScriptContext, petData: PetData, skillOption:
    SkillOption) {
        it.rootFrame().variables()["@IdSkill"] = skillOption.id
        it.rootFrame().variables()["@PetData"] = petData
        it.rootFrame().variables()["@PetSkillData"] = this
    }

    /**
     * 获取宠物图标
     */
    fun PetData.iconItem(player: Player): ItemStack {
        val i = petOption().item
        val item = ItemElement(i.material, i.name, i.lore, i.modelData)
        return item.itemStack(player) {
            petVar(it)
        }
    }

    /**
     * @return 获取技能图标物品
     */
    fun SkillData.iconItem(player: Player, petData: PetData, iconId: String): ItemStack? {
        val icon = iconId.iconItem()?: return null
        val option = id.skillOption()?: return null
        val skillIcon = option.icon
        val item = ItemElement(skillIcon.material, modelData = skillIcon.modelData)
        if (icon.name.isNotEmpty()) {
            item.name = icon.name
        }else {
            item.name = skillIcon.name
        }
        if (icon.lore.isNotEmpty()) {
            item.lore = icon.lore
        }else {
            item.lore = skillIcon.lore
        }
        return item.itemStack(player) {
            petVar(it, petData)
        }
    }

    /**
     * @return 获取技能配置图标物品
     */
    fun SkillData.iconItem(player: Player, petData: PetData, skillOption: SkillOption): ItemStack {
        val icon = skillOption.icon
        val item = ItemElement(icon.material, modelData = icon.modelData)
        if (icon.name.isNotEmpty()) {
            item.name = icon.name
        } else {
            item.name = icon.name
        }
        if (icon.lore.isNotEmpty()) {
            item.lore = icon.lore
        } else {
            item.lore = icon.lore
        }
        return item.itemStack(player) {
            skillOptionDataVar(it, petData, skillOption)
        }
    }

}