package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.api.data.DataContainer.iconOptionMap
import cn.inrhor.imipetcore.common.option.ItemElement

object IconManager {

    /**
     * 根据ID获取图标物品
     */
    fun String.iconItem(): ItemElement? = iconOptionMap[this]?.item

}