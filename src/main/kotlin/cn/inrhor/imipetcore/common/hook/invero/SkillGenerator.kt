package cn.inrhor.imipetcore.common.hook.invero

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.geneartor.ContextGenerator

class LoadSkillGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.skillGenerate(context, UiTypeSkill.LOAD)
    }

}

class UnloadSkillGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.skillGenerate(context, UiTypeSkill.UNLOAD)
    }

}

class UpdateSkillGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.skillGenerate(context, UiTypeSkill.UPDATE)
    }

}

class PointSkillGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.skillGenerate(context, UiTypeSkill.POINT)
    }

}

class UpdateSelectSkillGenerator: ContextGenerator() {

    override fun generate(context: Context) {
        generated = InvGenerator.skillTreeGenerate(context, UiTypeSkill.UPDATE_SELECT)
    }

}
