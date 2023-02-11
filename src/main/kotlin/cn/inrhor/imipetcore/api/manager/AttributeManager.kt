package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.common.database.data.HookAttribute
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.server.ReadManager.attributePlus
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.api.AttributeAPI
import org.serverct.ersha.attribute.data.AttributeData

object AttributeManager {

    /**
     * 为实体加载挂钩属性数据
     */
    fun LivingEntity.loadAttributeData(petData: PetData) {
        if (attributePlus) {
            AttributeData.create(this) // 重置
            val data = AttributeAPI.getAttrData(this)
            val list = mutableListOf<String>()
            petData.attribute.hook.forEach {
                if (it.type == HookAttribute.ATTRIBUTE_PLUS) {
                    list.add("${it.key}: ${it.value}")
                }
            }
            AttributeAPI.addSourceAttribute(data, "imiPetCore", list)
        }
    }

}