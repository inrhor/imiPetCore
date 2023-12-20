package cn.inrhor.imipetcore.common.hook.hologram

import org.bukkit.Location

abstract class HologramProvider<out T> {

    protected abstract val holo: T

    abstract fun createHologram(location: Location, lines: List<String>, id: String = ""): T

    abstract fun delete()

    abstract fun teleport(location: Location)

    abstract fun update(lines: List<String>)
}


