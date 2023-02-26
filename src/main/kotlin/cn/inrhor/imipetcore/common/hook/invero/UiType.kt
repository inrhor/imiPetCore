package cn.inrhor.imipetcore.common.hook.invero

import cn.inrhor.imipetcore.api.manager.PetManager.followingPetData
import cn.inrhor.imipetcore.api.manager.PetManager.getPets
import cn.inrhor.imipetcore.api.manager.SkillManager.getAllSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getLoadSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUnloadSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.getUpdateSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.treeSkillOption
import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.data.SkillData
import cn.inrhor.imipetcore.common.option.SkillOption
import org.bukkit.entity.Player

enum class UiTypePet {

    ALL_PET { override fun list(player: Player) = player.getPets()
        override fun uiName() = "pets"
    },
    FOLLOW_PET { override fun list(player: Player) = player.followingPetData()
        override fun uiName() = "followPets"
    };

    abstract fun list(player: Player): List<PetData>

    abstract fun uiName(): String

}

enum class UiTypeSkill {

    LOAD {
        override fun list(petData: PetData) = petData.getLoadSkills()
        override fun uiName() = "petLoadSkill"
    },
    UNLOAD {
        override fun list(petData: PetData) = petData.getUnloadSkills()
        override fun uiName() = "petUnloadSkill"
    },
    UPDATE {
        override fun list(petData: PetData) = petData.getUpdateSkills()
        override fun uiName() = "petUpdateSkill"
    },
    LOAD_SLOT {
        override fun list(petData: PetData) = petData.getLoadSkills()
        override fun uiName() = "petLoadSlotSkill"
    },
    POINT {
        override fun list(petData: PetData) = petData.getAllSkills()
        override fun uiName() = "petPointSkill"
    },
    UPDATE_SELECT {
        override fun list(skillData: SkillData) = skillData.treeSkillOption()
        override fun uiName() = "petUpdateSkillSelect"
    };

    open fun list(petData: PetData): List<SkillData> = listOf()

    open fun list(skillData: SkillData): List<SkillOption> = listOf()

    abstract fun uiName(): String

}