package cn.inrhor.imipetcore.api.entity.ai.nms

import org.bukkit.entity.Entity
import taboolib.module.nms.MinecraftVersion

object NmsAiGoal {

    fun Entity.addNmsAi(nmsAi: NmsAi, priority: Int) {
        try {
            val nmsEntity = this::class.java.getMethod("getHandle").invoke(this)
            val v = if (MinecraftVersion.major == 4) "v1_12_R1" else "v1_16_R3"
            val version = "net.minecraft.server.$v"
            val pathSelector = Class.forName("$version.PathfinderGoalSelector")
            val pathGoal = Class.forName("$version.PathfinderGoal")
            val entityInsentient = Class.forName("$version.EntityInsentient")
            val method = pathSelector.getDeclaredMethod("a", Int::class.javaPrimitiveType,
                    pathGoal)
            val field = entityInsentient.getDeclaredField("goalSelector")
            field.isAccessible = true
            val obj = field.get(nmsEntity)
            method.invoke(obj, priority, nmsAi)
        }catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}