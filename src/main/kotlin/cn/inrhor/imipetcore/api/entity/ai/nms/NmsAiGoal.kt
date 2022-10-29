package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAi
import org.bukkit.entity.Entity
import taboolib.module.nms.MinecraftVersion

object NmsAiGoal {

    fun Entity.addNmsAi(nmsAi: UniversalAi, priority: Int) {
        try {
            val nmsEntity = this::class.java.getMethod("getHandle").invoke(this)
            val major = MinecraftVersion.major
            val v = if (major == 4) "v1_12_R1" else "v1_16_R3"
            val version = "net.minecraft.server.$v"
            val pathSelector = Class.forName("$version.PathfinderGoalSelector")
            val pathGoal = Class.forName("$version.PathfinderGoal")
            val entityInsentient = Class.forName("$version.EntityInsentient")
            val method = pathSelector.getDeclaredMethod("a", Int::class.javaPrimitiveType,
                pathGoal)
            val field = entityInsentient.getDeclaredField("goalSelector")
            field.isAccessible = true
            val obj = field.get(nmsEntity)
            val ai: Any = if (major == 4) Nms112R1Ai(nmsAi) else Nms116R3Ai(nmsAi)
            method.invoke(obj, priority, ai)
        }catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}