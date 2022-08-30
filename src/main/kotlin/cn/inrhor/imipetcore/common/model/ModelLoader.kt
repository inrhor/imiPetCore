package cn.inrhor.imipetcore.common.model

import org.bukkit.Bukkit

/**
 * 模型加载器
 */
class ModelLoader(var modelEngine: Boolean = false) {

    fun load() {
        if (Bukkit.getPluginManager().getPlugin("ModelEngine") != null) {
            modelEngine = true
        }
    }

}

enum class ModelSelect {
    COMMON, MODEL_ENGINE
}