package cn.inrhor.imipetcore.common.hook.hologram

import cn.inrhor.imipetcore.api.entity.PetEntity
import cn.inrhor.imipetcore.common.option.Addon
import cn.inrhor.imipetcore.common.option.AddonSelect
import cn.inrhor.imipetcore.common.script.kether.evalStrPetData
import cn.inrhor.imipetcore.server.ReadManager
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor

class NameHologram(val petEntity: PetEntity, val addon: Addon) {

    /**
     * 全息定时器
     *
     * PlatformExecutor.PlatformTask
     */
    var hologramNameTask: PlatformExecutor.PlatformTask? = null

    /**
     * 全息
     */
    var hologramProvider: HologramProvider<Any>? = null

    /**
     * 销毁
     */
    fun destroy() {
        hologramNameTask?.cancel()
        hologramNameTask = null
        hologramProvider?.delete()
        hologramProvider = null
    }

    /**
     * 显示
     */
    fun display() {
        hologramProvider = when (addon.select) {
            AddonSelect.ADYESHACH -> {
                if (!ReadManager.adyeshachLoad) {
                    null
                }else {
                    AdyeshachProvider()
                }
            }

            AddonSelect.DECENT_HOLOGRAMS -> {
                if (!ReadManager.decentHologramsLoad) {
                    null
                }else {
                    DecentHologramsProvider()
                }
            }

            else -> {
                null
            }
        }
        if (hologramProvider != null) {
            val entity = petEntity.entity?: return
            hologramProvider?.createHologram(entityLoc(entity), lines())
            hologramNameTask = submit(delay = 1L, async = true, period = 1L) {
                if (petEntity.entity == null || petEntity.entity?.isDead == true) {
                    hologramProvider?.delete()
                    cancel()
                    return@submit
                }else {
                    if (entity.hasMetadata("imipetcore_drive")) {
                        hologramProvider?.update(emptyList())
                        return@submit
                    }
                    hologramProvider?.update(lines())
                    hologramProvider?.teleport(entityLoc(entity))
                }

            }
        }
    }

    private fun entityLoc(entity: Entity): Location {
        return entity.location.clone().add(0.0, addon.height, 0.0)
    }

    private fun lines(): List<String> {
        return petEntity.owner.evalStrPetData(addon.lines, petEntity.petData)
    }

}