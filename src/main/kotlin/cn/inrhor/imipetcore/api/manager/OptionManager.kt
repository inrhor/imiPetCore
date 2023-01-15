package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.actionOptionMap
import cn.inrhor.imipetcore.api.data.DataContainer.iconOptionMap
import cn.inrhor.imipetcore.api.data.DataContainer.petOptionMap
import cn.inrhor.imipetcore.api.data.DataContainer.skillOptionMap
import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.option.*

object OptionManager {
    /**
     * @return 根据 id 返回宠物配置
     */
    fun String.petOption(): PetOption {
        return petOptionMap[this]?: error("null id")
    }

    /**
     * 存储宠物配置到容器
     */
    fun PetOption.save() {
        petOptionMap[id] = this
    }

    /**
     * 存储图标配置到容器
     */
    fun IconOption.save() {
        iconOptionMap[id] = this
    }

    /**
     * @return HookAi
     */
    fun String.getActionOption(): ActionOption? {
        return actionOptionMap[this]
    }

    /**
     * 存储动物行为Ai配置
     */
    fun ActionOption.save() {
        actionOptionMap[name] = this
    }

    /**
     *  存储技能配置
     */
    fun SkillOption.save() {
        skillOptionMap[id] = this
    }

    /**
     * 模型配置
     */
    fun PetEntity.model(): ModelOption = petData.petOption().model
}