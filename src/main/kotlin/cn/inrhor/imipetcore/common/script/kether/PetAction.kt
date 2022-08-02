package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.api.manager.PetManager.callPet
import cn.inrhor.imipetcore.api.manager.PetManager.deletePet
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import taboolib.common.platform.function.info
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

class PetAction {

    class ActionPetEntity(val who: WhoType): ScriptAction<Entity>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Entity> {
            val e = if (who == WhoType.OWNER) frame.selectPetOwner() else frame.selectPetEntity()
            return CompletableFuture.completedFuture(e)
        }

        enum class WhoType {
            OWNER, PET
        }
    }

    class ActionBoxMax(val en: ParsedAction<*>): ScriptAction<Vector>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Vector> {
            return frame.newFrame(en).run<Entity>().thenApply {
                it.boundingBox.max
            }
        }
    }

    class ActionDistance(val v1: ParsedAction<*>, val v2: ParsedAction<*>): ScriptAction<Double>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Double> {
            return frame.newFrame(v1).run<Vector>().thenApply { i1 ->
                frame.newFrame(v2).run<Vector>().thenApply { i2 ->
                    i1.distance(i2)
                }.join()
            }
        }
    }

    companion object {
        @KetherParser(["pet"])
        fun parserPet() = scriptParser {
            it.switch {
                case("entity") {
                    ActionPetEntity(ActionPetEntity.WhoType.PET)
                }
                case("owner") {
                    ActionPetEntity(ActionPetEntity.WhoType.OWNER)
                }
                case("select") {
                    val next = it.nextToken()
                    actionNow {
                        variables().set("@PetData", player().getPet(next))
                    }
                }
                case("follow") {
                    when (it.expects("set", "get")) {
                        "set" -> {
                            val a = it.next(ArgTypes.BOOLEAN)
                            actionNow {
                                info("aaaa $a") // info()
                                player().callPet(selectPetData().name, a)
                            }
                        }
                        "get" -> {
                            actionNow {
                                selectPetData().following
                            }
                        }
                        else -> error("pet follow set/get")
                    }
                }
                case("release") {
                    actionNow {
                        player().deletePet(selectPetData().name)
                    }
                }
            }
        }

        @KetherParser(["boxMax"])
        fun parserBox() = scriptParser {
            val en = it.next(ArgTypes.ACTION)
            ActionBoxMax(en)
        }

        @KetherParser(["distance"])
        fun parserDis() = scriptParser {
            val v1 = it.next(ArgTypes.ACTION)
            it.expect("to")
            val v2 = it.next(ArgTypes.ACTION)
            ActionDistance(v1, v2)
        }
    }

}