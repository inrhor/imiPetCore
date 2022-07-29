package cn.inrhor.imipetcore.common.script.kether

import org.bukkit.entity.Entity
import taboolib.module.kether.ScriptFrame

fun ScriptFrame.selectPetEntity() = variables().get<Entity?>("@PetEntity")
    .orElse(null)?: error("unknown @PetEntity")

fun ScriptFrame.selectPetOwner() = variables().get<Entity?>("@PetOwner")
    .orElse(null)?: error("unknown @PetOwner")