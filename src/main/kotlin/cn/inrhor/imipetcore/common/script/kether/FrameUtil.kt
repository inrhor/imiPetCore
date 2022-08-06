package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.script

fun ScriptFrame.selectPetData() = variables().get<PetData>("@PetData")
    .orElse(null)?: error("unknown @PetData")

fun ScriptFrame.player() = script().sender?.castSafely<Player>()?: error("unknown player")

fun ScriptFrame.getUiPage() = variables().get<Int?>("@UiPage")
    .orElse(null)?: 0