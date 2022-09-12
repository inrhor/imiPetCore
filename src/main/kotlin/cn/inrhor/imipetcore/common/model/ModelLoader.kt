package cn.inrhor.imipetcore.common.model

import ltd.icecold.orangeengine.api.OrangeEngineAPI
import org.bukkit.Bukkit

/**
 * 模型加载器
 */
class ModelLoader(
    var modelEngine: Boolean = false,
    var orangeEngine: Boolean = false,
    var germEngine: Boolean = false) {

    fun load() {
        if (Bukkit.getPluginManager().getPlugin("ModelEngine") != null) {
            modelEngine = true
        }
        if (Bukkit.getPluginManager().getPlugin("OrangeEngine") != null) {
            orangeEngine = OrangeEngineAPI.getModelManager() != null
        }
        if (Bukkit.getPluginManager().getPlugin("GermEngine") != null) {
            germEngine = true
        }
    }

}

enum class ModelSelect {
    COMMON, MODEL_ENGINE, ORANGE_ENGINE, GERM_ENGINE
}