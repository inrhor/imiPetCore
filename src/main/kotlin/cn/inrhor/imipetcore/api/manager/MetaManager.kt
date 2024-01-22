package cn.inrhor.imipetcore.api.manager

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
import cn.inrhor.imipetcore.common.database.data.PetData
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

/**
 * 宠物标签管理
 */
object MetaManager {

    /**
     * 设置标签数据
     */
    fun Entity.setMeta(meta: String, obj: Any?) {
        setMetadata("imipetcore_$meta", FixedMetadataValue(ImiPetCore.plugin, obj))
    }

    /**
     * @return 标签数值
     */
    fun Entity.getMeta(meta: String): Any? {
        val s = "imipetcore_$meta"
        if (!hasMetadata(s)) return null
        return getMetadata(s)[0].value()
    }

    /**
     * @return 主人
     */
    fun Entity.getOwner(): Player? {
        if (this !is Wolf || owner == null) return null
        return owner?.uniqueId?.let { Bukkit.getPlayer(it) }
    }

    /**
     * 是否是宠物
     */
    fun Entity.isPet(): Boolean = getOwner() != null

    /**
     * @return 宠物数据
     */
    fun Entity.getPetData(owner: Player): PetData? {
        val e = getMeta("entity")?: return null
        return owner.getPet(e.toString()).petEntity?.petData
    }

    /**
     * 删除标签数据
     */
    fun Entity.removeMeta(meta: String) {
        removeMetadata("imipetcore_$meta", ImiPetCore.plugin)
    }

    /**
     * @return 标签数据取实体
     */
    fun Entity.metaEntity(meta: String): Entity? = getMeta(meta) as Entity?

}