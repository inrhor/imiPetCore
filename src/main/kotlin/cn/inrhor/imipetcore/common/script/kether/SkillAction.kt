package cn.inrhor.imipetcore.common.script.kether

import cn.inrhor.imipetcore.api.manager.SkillManager.addCoolDown
import cn.inrhor.imipetcore.api.manager.SkillManager.addSkillPoint
import cn.inrhor.imipetcore.api.manager.SkillManager.coolDown
import cn.inrhor.imipetcore.api.manager.SkillManager.delCoolDown
import cn.inrhor.imipetcore.api.manager.SkillManager.delSkillPoint
import cn.inrhor.imipetcore.api.manager.SkillManager.isCoolDown
import cn.inrhor.imipetcore.api.manager.SkillManager.launchSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.loadSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.replaceSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.setCoolDown
import cn.inrhor.imipetcore.api.manager.SkillManager.setSkillPoint
import cn.inrhor.imipetcore.api.manager.SkillManager.skillData
import cn.inrhor.imipetcore.api.manager.SkillManager.skillOption
import cn.inrhor.imipetcore.api.manager.SkillManager.unloadSkill
import cn.inrhor.imipetcore.api.manager.SkillSelect
import cn.inrhor.imipetcore.api.manager.SkillType
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
                                        selectPetData().setSkillPoint(player(), selectSkillData(), Coerce.toInteger(e))
                                    }
                                }
                            }
                            "add" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectPetData().addSkillPoint(player(), selectSkillData(), Coerce.toInteger(e))
                                    }
                                }
                            }
                            "del" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectPetData().delSkillPoint(player(), selectSkillData(), Coerce.toInteger(e))
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
                        when (it.expects("set", "add", "del", "bool")) {
                            "set" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectPetData().setCoolDown(player(), selectSkillData(), Coerce.toInteger(e))
                                    }
                                }
                            }
                            "add" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectPetData().addCoolDown(player(), selectSkillData(), Coerce.toInteger(e))
                                    }
                                }
                            }
                            "del" -> {
                                val a = it.next(ArgTypes.ACTION)
                                actionNow {
                                    newFrame(a).run<Any>().thenAccept { e ->
                                        selectPetData().delCoolDown(player(), selectSkillData(), Coerce.toInteger(e))
                                    }
                                }
                            }
                            "bool" -> {
                                actionNow {
                                    selectSkillData().isCoolDown()
                                }
                            }
                            else -> error("unknown coolDown ???")
                        }
                    }catch (ex: Exception) {
                        it.reset()
                        actionNow {
                            selectSkillData().coolDown()
                        }
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
                case("update") {
                    actionNow {
                        selectPetData().replaceSkill(player(), selectSkillData(), selectIdSkill())
                    }
                }
                case("launch") {
                    try {
                        it.mark()
                        when (it.expects("mm")) {
                            "mm" -> {
                                val skill = it.nextToken()
                                val type = try {
                                    it.mark()
                                    it.expect("type")
                                    it.nextToken()
                                }catch (ex: Exception) {
                                    it.reset()
                                    "NONE"
                                }
                                actionNow {
                                    selectPetData().launchSkill(SkillType.MYTHIC_MOBS, skill, SkillSelect.valueOf(type.uppercase()))
                                }
                            }
                            else -> error("unknown launch ???")
                        }
                    }catch (ex: Exception) {
                        it.reset()
                        actionNow {
                            selectPetData().launchSkill(player(), selectSkillData())
                        }
                    }
                }
            }
        }
    }
}