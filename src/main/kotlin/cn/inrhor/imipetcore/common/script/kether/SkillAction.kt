package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.api.manager.SkillManager.addNewSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.loadSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.removeSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.skillData
import cn.inrhor.imipetcore.api.manager.SkillManager.skillOption
import cn.inrhor.imipetcore.api.manager.SkillManager.unloadSkill
import taboolib.common5.Coerce
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*

class SkillAction {

    companion object {

        @KetherParser(["skillOption"], shared = true)
        fun parserSkill() = scriptParser {
            it.switch {
                case("select") {
                    val id = it.nextToken()
                    actionNow {
                        variables().set("@IdSkill", id)
                    }
                }
                case("coolDown") {
                    actionNow { selectIdSkill().skillOption()?.coolDown?: 0 }
                }
                case("point") {
                    actionNow { selectIdSkill().skillOption()?.tree?.point?: 0 }
                }
                case("name") {
                    actionNow { selectIdSkill().skillOption()?.name?: "" }
                }
            }
        }

        @KetherParser(["petSkill"], shared = true)
        fun parserPetSkill() = scriptParser {
            it.switch {
                case("select") {
                    val id = it.nextToken()
                    actionNow {
                        variables().set("@PetSkillData", id.skillData(selectPetData()))
                    }
                }
                case("id") {
                    actionNow { selectSkillData().id }
                }
                case("name") {
                    actionNow {
                        selectSkillData().skillName
                    }
                }
                case("point") {
                    try {
                        it.mark()
                        when (it.expects("set", "add", "del")) {
                            "set" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectSkillData().point = Coerce.toInteger(e)
                                    }
                                }
                            }
                            "add" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectSkillData().point += Coerce.toInteger(e)
                                    }
                                }
                            }
                            "del" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectSkillData().point -= Coerce.toInteger(e)
                                    }
                                }
                            }
                            else -> error("unknown point ???")
                        }
                    }catch (ex: Exception) {
                        it.reset()
                        actionNow {
                            selectSkillData().point
                        }
                    }
                }
                case("coolDown") {
                    try {
                        it.mark()
                        when (it.expects("set", "add", "del")) {
                            "set" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectSkillData().coolDown = Coerce.toInteger(e)
                                    }
                                }
                            }
                            "add" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectSkillData().coolDown += Coerce.toInteger(e)
                                    }
                                }
                            }
                            "del" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectSkillData().coolDown -= Coerce.toInteger(e)
                                    }
                                }
                            }
                            else -> error("unknown coolDown ???")
                        }
                    }catch (ex: Exception) {
                        it.reset()
                        actionNow {
                            selectSkillData().coolDown
                        }
                    }
                }
                case("skill") {
                    it.mark()
                    when (it.expects("add", "remove")) {
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
                        else -> error("unknown skill ???")
                    }
                }
                case("load") {
                    actionNow {
                        selectPetData().loadSkill(player(), selectSkillData(), selectIndex())
                    }
                }
                case("unload") {
                    actionNow { selectPetData().unloadSkill(player(), selectIndex()) }
                }
            }
        }
    }
}