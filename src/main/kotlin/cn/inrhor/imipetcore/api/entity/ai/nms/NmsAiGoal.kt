package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAi
import org.bukkit.entity.LivingEntity
import taboolib.module.nms.MinecraftVersion

object NmsAiGoal {

    private val isUniversal = MinecraftVersion.isUniversal

    private val major = MinecraftVersion.major

    private val minor = MinecraftVersion.minor

    fun LivingEntity.addNmsAi(nmsAi: UniversalAi, priority: Int) {
        try {
            val nmsEntity = this::class.java.getMethod("getHandle").invoke(this)
            val version = versionPack
            val versionAi = if (isUniversal)  "$version.ai.goal" else version

            val pathSelector = Class.forName("$versionAi.PathfinderGoalSelector")
            val pathGoal = Class.forName("$versionAi.PathfinderGoal")
            val entityInsentient = Class.forName("$version.EntityInsentient")
            val method = pathSelector.getDeclaredMethod("a", Int::class.javaPrimitiveType, pathGoal)
            val field = entityInsentient.getDeclaredField(goalSelector)
            field.isAccessible = true
            val obj = field.get(nmsEntity)
            val ai: Any = when (major) {
                4 -> Nms112R1Ai(nmsAi)
                8 -> Nms116R3Ai(nmsAi)
                else -> NmsNetAi(nmsAi)
            }
            method.invoke(obj, priority, ai)
        }catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * https://minecraft.fandom.com/zh/wiki/Java%E7%89%881.19
     *
     * 查看服务器混淆映射表
     */
    private val goalSelector = when(major) {
        10 -> "bQ" // 1.18.2
        11 -> "bS" // 1.19.X
        else -> "goalSelector"
    }

    private val versionPack =
        if (isUniversal) {
            "net.minecraft.world.entity"
        }else {
            val v = when {
                major == 4 -> "v1_12_R1"
                major == 8 && minor == 4 -> "v1_16_R3"
                else -> error("Unsupported version -> imiPetCore ProtocolLib: 1.12.2 1.16.5 1.18.2")
            }
            "net.minecraft.server.$v"
        }

}