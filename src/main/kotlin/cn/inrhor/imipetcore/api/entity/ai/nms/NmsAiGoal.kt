package cn.inrhor.imipetcore.api.entity.ai.nms

import cn.inrhor.imipetcore.api.entity.ai.universal.UniversalAi
import cn.inrhor.imipetcore.server.ReadManager.isUniversal
import cn.inrhor.imipetcore.server.ReadManager.major
import cn.inrhor.imipetcore.server.ReadManager.minor
import org.bukkit.entity.LivingEntity

object NmsAiGoal {

    /**
     * 添加AI
     */
    fun LivingEntity.addAi(ai: Any, priority: Int) {
        try {
            val nmsEntity = this::class.java.getMethod("getHandle").invoke(this)
            val version = versionPack
            val versionAi = if (isUniversal)  "$version.ai.goal" else version
            val pathSelector = Class.forName("$versionAi.PathfinderGoalSelector")
            val pathGoal = Class.forName("$versionAi.PathfinderGoal")
            val entityInsentient = Class.forName("$version.EntityInsentient")
            val method = pathSelector.getDeclaredMethod("a", Int::class.javaPrimitiveType, pathGoal)
            val field = entityInsentient.getDeclaredField(goalSelector)
            val obj = field.get(nmsEntity)
            method.invoke(obj, priority, ai)
        }catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * 添加跨版本的AI
     */
    fun LivingEntity.addNmsAi(nmsAi: UniversalAi, priority: Int) {
        val ai: Any = when (major) {
            4 -> Nms112R1Ai(nmsAi)
            8 -> Nms116R3Ai(nmsAi)
            else -> NmsNetAi(nmsAi)
        }
        addAi(ai, priority)
    }

    /**
     * https://minecraft.fandom.com/zh/wiki/Java%E7%89%881.19
     *
     * 查看服务器混淆映射表
     */
    private val goalSelector = when(major) {
        10 -> "bQ" // 1.18.2
        11 -> "bS" // 1.19.X
        12 -> "bO" // 1.20.X
        else -> "goalSelector"
    }

    val versionPack =
        if (isUniversal) {
            "net.minecraft.world.entity"
        }else {
            val v = when {
                major == 4 -> "v1_12_R1"
                major == 8 && minor == 4 -> "v1_16_R3"
                else -> error("Unsupported version -> imiPetCore ProtocolLib: 1.12.2 1.16.5 1.17+")
            }
            "net.minecraft.server.$v"
        }

}