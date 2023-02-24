package cn.inrhor.imipetcore.common.hook.invero

import cn.inrhor.imipetcore.api.manager.PetManager.followingPetData
import cn.inrhor.imipetcore.api.manager.PetManager.getPets
import cn.inrhor.imipetcore.api.manager.SkillManager.getLoadSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUnloadSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUpdateSkills
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.data.SkillData
import org.bukkit.entity.Player

enum class UiTypePet {

    ALL_PET { override fun list(player: Player) = player.getPets()
        override fun uiName() = "pets"
    },
    FOLLOW_PET { override fun list(player: Player) = player.followingPetData()
        override fun uiName() = "followPets"
    };

    open fun list(player: Player): List<PetData> = listOf()

    abstract fun uiName(): String

}

enum class UiTypeSkill {

    LOAD {
        override fun list(petData: PetData) = petData.getLoadSkills()
        override fun uiName() = "petLoadSkill"
    },
    UNLOAD {
        override fun list(petData: PetData) = petData.getUnloadSkills()
        override fun uiName() = "petUnLoadSkill"
    },
    UPDATE {
        override fun list(petData: PetData) = petData.getUpdateSkills()
        override fun uiName() = "petUpdateSkill"
    };

    open fun list(petData: PetData): MutableList<SkillData> = mutableListOf()

    abstract fun uiName(): String

}