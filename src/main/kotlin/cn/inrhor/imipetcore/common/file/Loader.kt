package cn.inrhor.imipetcore.common.file

import cn.inrhor.imipetcore.api.manager.OptionManager.save
import cn.inrhor.imipetcore.common.option.PetOption
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.getObject

/**
 * 加载宠物文件
 */
fun loadPet() {
    val folder = getFile("pet", "PET-EMPTY-FILE", true)
    getFileList(folder).forEach {
        val option = Configuration.loadFromFile(it).getObject<PetOption>("pet", false)
        option.save()
    }
}