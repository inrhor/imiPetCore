package cn.inrhor.imipetcore.common.listener.player

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.event.OwnerRightClickPet
import cn.inrhor.imipetcore.api.manager.MetaManager.getOwner
import cn.inrhor.imipetcore.api.manager.MetaManager.getPetData
import cn.inrhor.imipetcore.common.option.TriggerOption
import cn.inrhor.imipetcore.common.option.trigger
import ink.ptms.um.Mythic
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.SubscribeEvent

object ClickEntity {

    @SubscribeEvent
    fun castSkill(ev: PlayerInteractEntityEvent) {
        if (ev.hand != EquipmentSlot.HAND) return
        // 右键实体选定目标
        val p = ev.player
        val entity = ev.rightClicked
        // 技能禁止对宠物释放
        if (entity.getOwner() != null) return
        val data = p.getData().castSkillData
        val pet = data.petData?.petEntity?.entity?: return
        if (data.skill.isNotEmpty()) {
            Mythic.API.castSkill(pet, data.skill, entity)
            data.skill = ""
            data.petData = null
        }
    }

    @SubscribeEvent
    fun right(ev: PlayerInteractEntityEvent) {
        if (ev.hand != EquipmentSlot.HAND) return
        val entity = ev.rightClicked
        val owner = entity.getOwner()?: return
        if (owner != ev.player) return
        val petData = entity.getPetData(owner)?: return
        OwnerRightClickPet(owner, petData).call()
    }

    @SubscribeEvent
    fun trigger(ev: OwnerRightClickPet) {
        ev.petData.trigger(ev.player, TriggerOption.Type.OWNER_RIGHT_CLICK)
    }

}