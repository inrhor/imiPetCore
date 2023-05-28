package cn.inrhor.imipetcore.common.script.kether.action

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.entity.ai.Controller.attackEntity
import cn.inrhor.imipetcore.api.manager.ModelManager.playAnimation
import cn.inrhor.imipetcore.api.manager.PetManager
import cn.inrhor.imipetcore.api.manager.PetManager.addCurrentHP
import cn.inrhor.imipetcore.api.manager.PetManager.allPetsName
import cn.inrhor.imipetcore.api.manager.PetManager.callPet
import cn.inrhor.imipetcore.api.manager.PetManager.changePetId
import cn.inrhor.imipetcore.api.manager.PetManager.delCurrentHP
import cn.inrhor.imipetcore.api.manager.PetManager.deletePet
import cn.inrhor.imipetcore.api.manager.PetManager.driveRidePet
import cn.inrhor.imipetcore.api.manager.PetManager.followPetsName
import cn.inrhor.imipetcore.api.manager.PetManager.followingPet
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
import cn.inrhor.imipetcore.api.manager.PetManager.hasPassenger
import cn.inrhor.imipetcore.api.manager.PetManager.hookAttribute
import cn.inrhor.imipetcore.api.manager.PetManager.renamePet
import cn.inrhor.imipetcore.api.manager.PetManager.setCurrentExp
import cn.inrhor.imipetcore.api.manager.PetManager.setCurrentHP
import cn.inrhor.imipetcore.api.manager.PetManager.setLevel
import cn.inrhor.imipetcore.api.manager.PetManager.setMaxExp
import cn.inrhor.imipetcore.api.manager.PetManager.setMaxHP
import cn.inrhor.imipetcore.api.manager.PetManager.setPetAttack
import cn.inrhor.imipetcore.api.manager.PetManager.setPetSpeed
import cn.inrhor.imipetcore.api.manager.PetManager.unDriveRidePet
import cn.inrhor.imipetcore.api.manager.SkillManager.addNewSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.addPoint
import cn.inrhor.imipetcore.api.manager.SkillManager.delPoint
import cn.inrhor.imipetcore.api.manager.SkillManager.getPoint
import cn.inrhor.imipetcore.api.manager.SkillManager.removeSkill
import cn.inrhor.imipetcore.common.database.data.HookAttribute
import cn.inrhor.imipetcore.common.script.kether.attributeHookKey
import cn.inrhor.imipetcore.common.script.kether.attributeHookSelect
import cn.inrhor.imipetcore.common.script.kether.player
import cn.inrhor.imipetcore.common.script.kether.selectPetData
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.common.platform.function.submit
import taboolib.common5.Coerce
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.ai.controllerJumpReady
import taboolib.module.ai.controllerLookAt
import taboolib.module.ai.navigationMove
import taboolib.module.kether.*
import taboolib.platform.util.asLangText
import java.util.concurrent.CompletableFuture

class PetAction {

    class ActionPetEntity(val who: WhoType): ScriptAction<Entity>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Entity> {
            val e = if (who == WhoType.OWNER) frame.selectPetData().petEntity?.owner else frame.selectPetData().petEntity?.entity
            return CompletableFuture.completedFuture(e)
        }

        enum class WhoType {
            OWNER, PET
        }
    }

    class ActionPetLook(val en: ParsedAction<*>): ScriptAction<Void>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Void> {
            frame.newFrame(en).run<Entity>().thenApply {
                frame.selectPetData().petEntity?.entity?.controllerLookAt(it)
            }
            return CompletableFuture.completedFuture(null)
        }
    }

    companion object {

        @KetherParser(["pet"], shared = true)
        fun parserPet() = scriptParser {
            it.switch {
                case("entity") {
                    ActionPetEntity(ActionPetEntity.WhoType.PET)
                }
                case("owner") {
                    ActionPetEntity(ActionPetEntity.WhoType.OWNER)
                }
                case("look") {
                    val en = it.next(ArgTypes.ACTION)
                    ActionPetLook(en)
                }
                case("attack") {
                    val en = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(en).run<Entity>().thenApply { e ->
                            selectPetData().petEntity?.entity?.attackEntity(e)
                        }
                    }
                }
                case("byHurtEntity") {
                    actionNow {
                        selectPetData().petEntity?.hurtEntity?: false
                    }
                }
                case("drive") {
                    when (it.expects("type", "has")) {
                        "type" -> {
                            val str = it.next(ArgTypes.ACTION)
                            actionNow {
                                newFrame(str).run<String>().thenApply { s ->
                                    player().driveRidePet(selectPetData(), s)
                                }
                            }
                        }
                        "has" -> {
                            actionNow {
                                selectPetData().hasPassenger()
                            }
                        }
                        else -> error("unknown drive ???")
                    }
                }
                case("undrive") {
                    actionNow {
                        player().unDriveRidePet(selectPetData())
                    }
                }
                case("move") {
                    val loc = it.next(ArgTypes.ACTION)
                    it.expect("speed")
                    val speed = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(loc).run<Location>().thenApply { l ->
                            newFrame(speed).run<Any>().thenApply { d ->
                                selectPetData().petEntity?.entity?.navigationMove(l, Coerce.toDouble(d))
                            }
                        }
                    }
                }
                case("jump") {
                    actionNow {
                        selectPetData().petEntity?.entity?.controllerJumpReady()
                    }
                }
                case("select") {
                    val a = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(a).run<String>().thenApply { e ->
                            variables().set("@PetData", player().getPet(e))
                        }
                    }
                }
                case("follow") {
                    try {
                        it.mark()
                        when (it.expects("set", "lang", "list")) {
                            "set" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        submit {
                                            player().callPet(selectPetData().name, Coerce.toBoolean(e))
                                        }
                                    }
                                }
                            }
                            "lang" -> {
                                actionNow {
                                    player().asLangText("PET_FOLLOW_" + selectPetData().isFollow().toString().uppercase())
                                }
                            }
                            "list" -> {
                                actionNow {
                                    player().followPetsName()
                                }
                            }
                            else -> error("pet follow ?")
                        }
                    }catch (ex: Exception) {
                        it.reset()
                        actionNow {
                            selectPetData().isFollow()
                        }
                    }
                }
                case("list") {
                    actionNow {
                        player().allPetsName()
                    }
                }
                case("name") {
                    try {
                        it.mark()
                        it.expect("set")
                        val a = it.next(ArgTypes.ACTION)
                        actionNow {
                            newFrame(a).run<String>().thenAccept { s ->
                                player().renamePet(selectPetData(), s)
                            }
                        }
                    }catch (ex: Throwable) {
                        it.reset()
                        actionNow {
                            selectPetData().name
                        }
                    }
                }
                case("number") {
                    actionNow {
                        player().getData().petDataList.size
                    }
                }
                case("follow_number") {
                    actionNow {
                        player().followingPet().size
                    }
                }
                case("id") {
                    try {
                        it.mark()
                        it.expect("set")
                        val a = it.next(ArgTypes.ACTION)
                        actionNow {
                            newFrame(a).run<String>().thenAccept { s ->
                                player().changePetId(selectPetData(), s)
                            }
                        }
                    }catch (ex: Throwable) {
                        it.reset()
                        actionNow {
                            selectPetData().id
                        }
                    }
                }
                case("release") {
                    actionNow {
                        submit {
                            player().deletePet(selectPetData().name)
                        }
                    }
                }
                case("attribute") {
                    when (it.nextToken()) {
                        "attack" -> {
                            try {
                                it.mark()
                                it.expect("set")
                                val s = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(s).run<Any>().thenAccept { a ->
                                        player().setPetAttack(selectPetData(), Coerce.toDouble(a))
                                    }
                                }
                            }catch (ex: Throwable) {
                                it.reset()
                                actionNow {
                                    selectPetData().attribute.attack
                                }
                            }
                        }
                        "speed" -> {
                            try {
                                it.mark()
                                it.expect("set")
                                val s = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(s).run<Any>().thenAccept { a ->
                                        player().setPetSpeed(selectPetData(), Coerce.toDouble(a))
                                    }
                                }
                            }catch (ex: Throwable) {
                                it.reset()
                                actionNow {
                                    selectPetData().attribute.speed
                                }
                            }
                        }
                        "current_hp" -> {
                            try {
                                it.mark()
                                when (it.expects("set", "add", "del")) {
                                    "set" -> {
                                        val s = it.next(ArgTypes.ACTION)
                                        actionNow {
                                            newFrame(s).run<Any>().thenAccept { a ->
                                                player().setCurrentHP(selectPetData(), Coerce.toDouble(a))
                                            }
                                        }
                                    }
                                    "add" -> {
                                        val s = it.next(ArgTypes.ACTION)
                                        actionNow {
                                            newFrame(s).run<Any>().thenAccept { a ->
                                                player().addCurrentHP(selectPetData(), Coerce.toDouble(a))
                                            }
                                        }
                                    }
                                    "del" -> {
                                        val s = it.next(ArgTypes.ACTION)
                                        actionNow {
                                            newFrame(s).run<Any>().thenAccept { a ->
                                                player().delCurrentHP(selectPetData(), Coerce.toDouble(a))
                                            }
                                        }
                                    }
                                    else -> error("pet attribute current_hp ?")
                                }
                            } catch (ex: Throwable) {
                                it.reset()
                                actionNow {
                                    selectPetData().attribute.currentHP
                                }
                            }
                        }
                        "max_hp" -> {
                            try {
                                it.mark()
                                it.expect("set")
                                val s = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(s).run<Any>().thenAccept { a ->
                                        player().setMaxHP(selectPetData(), Coerce.toDouble(a))
                                    }
                                }
                            } catch (ex: Throwable) {
                                it.reset()
                                actionNow {
                                    selectPetData().attribute.maxHP
                                }
                            }
                        }
                        "hook" -> {
                            when (it.expects("select", "key", "get", "set", "remove")) {
                                "select" -> { // 选择挂钩
                                    val a = it.nextToken()
                                    actionNow {
                                        variables().set("@AttributeHook", a)
                                    }
                                }
                                "key" -> { // 选择键
                                    val a = it.nextToken()
                                    actionNow {
                                        variables().set("@AttributeHookKey", a)
                                    }
                                }
                                "get" -> {
                                    actionNow {
                                        selectPetData().hookAttribute(player(),
                                            PetManager.OperateType.GET, HookAttribute.valueOf(attributeHookSelect().uppercase()),
                                            attributeHookKey())
                                    }
                                }
                                "remove" -> {
                                    actionNow {
                                        selectPetData().hookAttribute(player(),
                                            PetManager.OperateType.REMOVE, HookAttribute.valueOf(attributeHookSelect().uppercase()),
                                            attributeHookKey())
                                    }
                                }
                                "set" -> {
                                    val a = it.next(ArgTypes.ACTION)
                                    actionNow {
                                        newFrame(a).run<String>().thenAccept { b ->
                                            selectPetData().hookAttribute(player(),
                                                PetManager.OperateType.SET,
                                                HookAttribute.valueOf(attributeHookSelect().uppercase()),
                                                attributeHookKey(), b)
                                        }
                                    }
                                }
                                else -> error("pet attribute hook ?")
                            }
                        }
                        else -> error("pet attribute ?  (1.2.0+移除了attack_speed)")
                    }
                }
                case("current_exp") {
                    try {
                        it.mark()
                        it.expect("set")
                        val s = it.next(ArgTypes.ACTION)
                        actionNow {
                            newFrame(s).run<Any>().thenAccept { a ->
                                player().setCurrentExp(selectPetData(), Coerce.toInteger(a))
                            }
                        }
                    } catch (ex: Throwable) {
                        it.reset()
                        actionNow {
                            selectPetData().currentExp
                        }
                    }
                }
                case("max_exp") {
                    try {
                        it.mark()
                        it.expect("set")
                        val s = it.next(ArgTypes.ACTION)
                        actionNow {
                            newFrame(s).run<Any>().thenAccept { a ->
                                player().setMaxExp(selectPetData(), Coerce.toInteger(a))
                            }
                        }
                    } catch (ex: Throwable) {
                        it.reset()
                        actionNow {
                            selectPetData().maxExp
                        }
                    }
                }
                case("level") {
                    try {
                        it.mark()
                        it.expect("set")
                        val s = it.next(ArgTypes.ACTION)
                        actionNow {
                            newFrame(s).run<Any>().thenAccept { a ->
                                player().setLevel(selectPetData(), Coerce.toInteger(a))
                            }
                        }
                    } catch (ex: Throwable) {
                        it.reset()
                        actionNow {
                            selectPetData().level
                        }
                    }
                }
                case("skill") {
                    it.mark()
                    when (it.expects("add", "remove", "point")) {
                        "add" -> {
                            it.expect("id")
                            val id = it.nextToken()
                            actionNow {
                                selectPetData().addNewSkill(player(), id)
                            }
                        }
                        "remove" -> {
                            it.expect("id")
                            val id = it.nextToken()
                            actionNow {
                                selectPetData().removeSkill(player(), id)
                            }
                        }
                        "point" -> {
                            it.mark()
                            when (it.expects("add", "del", "get")) {
                                "add" -> {
                                    val a = it.next(ArgTypes.ACTION)
                                    actionNow {
                                        newFrame(a).run<Any>().thenAccept { e ->
                                            selectPetData().addPoint(player(), Coerce.toInteger(e))
                                        }
                                    }
                                }
                                "del" -> {
                                    val a = it.next(ArgTypes.ACTION)
                                    actionNow {
                                        newFrame(a).run<Any>().thenAccept { e ->
                                            selectPetData().delPoint(player(), Coerce.toInteger(e))
                                        }
                                    }
                                }
                                "get" -> {
                                    actionNow {
                                        selectPetData().getPoint()
                                    }
                                }
                                else -> error("unknown skill point ???")
                            }
                        }
                        else -> error("unknown skill ???")
                    }
                }
                case("animation") {
                    val action = it.next(ArgTypes.ACTION)
                    actionNow {
                        newFrame(action).run<String>().thenApply { s ->
                            selectPetData().petEntity?.playAnimation(s, true)
                        }
                    }
                }
            }
        }
    }

}