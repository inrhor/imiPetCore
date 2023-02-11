package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.data.SkillData
import cn.inrhor.imipetcore.common.option.SkillOption
import org.bukkit.entity.Player
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.script

fun ScriptFrame.selectPetData() = variables().get<PetData>("@PetData")
    .orElse(null)?: error("unknown @PetData")

fun ScriptFrame.selectSkillData() = variables().get<SkillData>("@PetSkillData")
    .orElse(null)?: error("unknown @PetSkillData")

fun ScriptFrame.selectIdSkill() = variables().get<String>("@IdSkill")
    .orElse(null)?: error("unknown @IdSkill")

fun ScriptFrame.attributeHookSelect() = variables().get<String>("@AttributeHook")
    .orElse(null)?: error("please select attribute hook")

fun ScriptFrame.attributeHookKey() = variables().get<String>("@AttributeHookKey")
    .orElse(null)?: error("please select attribute hook key")

fun ScriptFrame.selectIndex() = variables().get<Int>("@Index")
    .orElse(null)?: 0

fun ScriptFrame.player() = script().sender?.castSafely<Player>()?: error("unknown player")

fun ScriptFrame.getUiPage() = variables().get<Int?>("@UiPage")
    .orElse(null)?: 0