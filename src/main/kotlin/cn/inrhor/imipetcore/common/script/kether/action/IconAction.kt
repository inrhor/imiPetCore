package cn.inrhor.imipetcore.common.script.kether.action

import cn.inrhor.imipetcore.api.manager.IconManager.iconItem
import cn.inrhor.imipetcore.api.manager.SkillManager.skillOption
import cn.inrhor.imipetcore.common.script.kether.player
import cn.inrhor.imipetcore.common.script.kether.selectIdSkill
import cn.inrhor.imipetcore.common.script.kether.selectPetData
import cn.inrhor.imipetcore.common.script.kether.selectSkillData
import org.bukkit.inventory.ItemStack
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

class IconAction {
    companion object {

        @KetherParser(["iconImi"], shared = true)
        fun parser() = scriptParser {
            it.switch {
                case("pet") {
                    actionNow {
                        selectPetData().iconItem(player())
                    }
                }
                case("skill") {
                    val id = next(ArgTypes.ACTION)
                    ActionSkillDataItem(id)
                }
                case("option") {
                    ActionSkillOptionItem()
                }
            }
        }
    }

    class ActionSkillDataItem(val id: ParsedAction<*>): ScriptAction<ItemStack>() {
        override fun run(frame: ScriptFrame): CompletableFuture<ItemStack> {
            val comp = CompletableFuture<ItemStack>()
            frame.newFrame(id).run<String>().thenApply { e ->
                comp.complete(frame.selectSkillData().iconItem(frame.player(), frame
                    .selectPetData(), e))
            }
            return comp
        }
    }

    class ActionSkillOptionItem: ScriptAction<ItemStack>() {
        override fun run(frame: ScriptFrame): CompletableFuture<ItemStack> {
            val comp = CompletableFuture<ItemStack>()
            val skillOption = frame.selectIdSkill().skillOption()
            if (skillOption != null) {
                comp.complete(frame.selectSkillData().iconItem(frame.player(), frame
                    .selectPetData(), frame.selectIdSkill().skillOption()!!))
            }
            return comp
        }
    }
}