package cn.inrhor.imipetcore.common.hook.hologram

import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import org.bukkit.Location

class DecentHologramsProvider : HologramProvider<Hologram>() {

    override lateinit var holo: Hologram

    override fun createHologram(location: Location, lines: List<String>, id: String): Hologram {
        val hologram = DHAPI.createHologram(id, location, lines)
        holo = hologram
        return hologram
    }

    override fun delete() {
        holo.destroy()
    }

    override fun teleport(location: Location) {
        DHAPI.moveHologram(holo, location)
    }

    override fun update(lines: List<String>) {
        DHAPI.setHologramLines(holo, lines)
    }
}