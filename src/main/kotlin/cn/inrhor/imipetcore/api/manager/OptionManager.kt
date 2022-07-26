package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.petOptionMap
import cn.inrhor.imipetcore.common.option.PetOption

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
}