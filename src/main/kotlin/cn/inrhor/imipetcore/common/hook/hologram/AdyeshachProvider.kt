package cn.inrhor.imipetcore.common.hook.hologram

import ink.ptms.adyeshach.api.AdyeshachAPI
import ink.ptms.adyeshach.api.Hologram
import org.bukkit.Location

class AdyeshachProvider : HologramProvider<Hologram<*>>() {

    override lateinit var holo: Hologram<*>

    override fun createHologram(location: Location, lines: List<String>, id: String): Hologram<*> {
        val holo = AdyeshachAPI.createHologram(location, lines)
        this.holo = holo
        return holo
    }

    override fun delete() {
        holo.delete()
    }

    override fun teleport(location: Location) {
        holo.teleport(location)
    }

    override fun update(lines: List<String>) {
        holo.update(lines)
    }
}