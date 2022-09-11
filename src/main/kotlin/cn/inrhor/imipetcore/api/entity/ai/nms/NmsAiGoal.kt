package cn.inrhor.imipetcore.api.entity.ai

import net.minecraft.server.v1_12_R1.EntityInsentient
import net.minecraft.server.v1_12_R1.PathfinderGoal
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
import org.bukkit.entity.Entity

object NmsAiGoal {

    fun Entity.nmsV112R1Ai(nmsAi: NmsAi) {
        val e = (this as CraftEntity).handle
        try {
            val method = PathfinderGoalSelector::class.java
                .getDeclaredMethod("a", Int::class.javaPrimitiveType,
                    PathfinderGoal::class.javaObjectType)
            val field = EntityInsentient::class.java.getDeclaredField("goalSelector")
            field.isAccessible = true
            val obj = field.get(e)
            method.invoke(obj, 1, nmsAi)
        }catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}