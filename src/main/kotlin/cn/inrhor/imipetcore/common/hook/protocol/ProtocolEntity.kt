package cn.inrhor.imipetcore.common.hook.protocol

import cn.inrhor.imipetcore.common.hook.protocol.version.EntityMap
import cn.inrhor.imipetcore.common.hook.protocol.version.EntityMap1
import cn.inrhor.imipetcore.common.hook.protocol.version.EntityMap2
import cn.inrhor.imipetcore.common.hook.protocol.version.EntityMap3
import cn.inrhor.imipetcore.server.PluginLoader.protocolLibLoad
import cn.inrhor.imipetcore.server.ReadManager.major
import org.bukkit.entity.EntityType

object ProtocolEntity {

    // 1.8-1.12 只需要"typeId

    var entityMap: EntityMap? = null

    fun initEntityMap() {
        if (protocolLibLoad) {
            when {
                major > 10 -> entityMap = EntityMap3()
                major > 8 -> entityMap = EntityMap2()
                major > 4 -> entityMap = EntityMap1()
            }
        }
    }

    /**
     * @return ProtocolLib实体序号
     */
    fun EntityType.protocolEntityId(): Int {
        return entityMap?.getEntityId(this.name)?: typeId.toInt()
    }

}