package cn.inrhor.imipetcore.common.database.type

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.data.DataContainer.initData
import cn.inrhor.imipetcore.api.data.DataContainer.playerData
import cn.inrhor.imipetcore.common.database.Database
import cn.inrhor.imipetcore.common.database.data.PetData
import taboolib.common.io.newFile
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.getObject
import taboolib.module.configuration.Configuration.Companion.setObject
import java.io.File
import java.util.*

/**
 * 实现数据YAML
 */
class DatabaseLocal: Database() {

    fun getLocal(uuid: UUID): File {
        return newFile(ImiPetCore.resource.getDataFolder(), "data/$uuid.yml")
    }

    override fun pull(uuid: UUID) {
        uuid.initData()
        val yaml = Configuration.loadFromFile(getLocal(uuid))
        yaml.getConfigurationSection("pet")?.getKeys(false)?.forEach {
            val petData = yaml.getObject<PetData>("pet.$it", false)
            petData.name = it
            uuid.playerData().petDataList.add(petData)
        }
    }

    override fun updatePet(uuid: UUID, petData: PetData) {
        val file = getLocal(uuid)
        val yaml = Configuration.loadFromFile(file)
        yaml.setObject("pet.${petData.name}", petData)
        yaml.saveToFile(file)
    }

    override fun deletePet(uuid: UUID, name: String) {
        val file = getLocal(uuid)
        val yaml = Configuration.loadFromFile(file)
        yaml["pet.$name"] = null
        yaml.saveToFile(file)
    }

    override fun renamePet(uuid: UUID, oldName: String, petData: PetData) {
        val file = getLocal(uuid)
        val yaml = Configuration.loadFromFile(file)
        yaml["pet.$oldName"] = null
        yaml.setObject("pet.${petData.name}", petData)
        yaml.saveToFile(file)
    }
}