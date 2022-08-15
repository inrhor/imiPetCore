package cn.inrhor.imipetcore.common.file

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.manager.OptionManager.save
import cn.inrhor.imipetcore.common.option.ActionOption
import cn.inrhor.imipetcore.common.option.PetOption
import cn.inrhor.imipetcore.common.ui.UiData.homePetUi
import cn.inrhor.imipetcore.common.ui.UiData.managerPetUi
import cn.inrhor.imipetcore.common.ui.UiData.medicalUi
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.getObject

/**
 * 加载宠物文件
 */
fun loadPet() {
    val folder = getFile("pet", "PET_EMPTY_FILE", true)
    getFileList(folder).forEach {
        val option = Configuration.loadFromFile(it).getObject<PetOption>("pet", false)
        option.save()
    }
}

/**
 * 加载动作行为Ai文件
 */
fun loadAction() {
    val folder = getFile("action", "ACTION_EMPTY_FILE", true)
    getFileList(folder).forEach {
       val yaml = Configuration.loadFromFile(it)
        yaml.getConfigurationSection("action")?.getKeys(false)?.forEach { e ->
            val option = yaml.getObject<ActionOption>("action.$e", false)
            option.name = e
            option.save()
        }
    }
}

/**
 * 加载页面配置
 */
fun loadUi() {
    val homeUiFile = ImiPetCore.resource.releaseResourceFile("ui/homePet.yml", false)
    homePetUi = Configuration.loadFromFile(homeUiFile).getObject("", false)
    val managerPetUiFile = ImiPetCore.resource.releaseResourceFile("ui/managerpet.yml", false)
    managerPetUi = Configuration.loadFromFile(managerPetUiFile).getObject("", false)
    val medicalFile = ImiPetCore.resource.releaseResourceFile("ui/medical.yml", false)
    medicalUi = Configuration.loadFromFile(medicalFile).getObject("", false)
}