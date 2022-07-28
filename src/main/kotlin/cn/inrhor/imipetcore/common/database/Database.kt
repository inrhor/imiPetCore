package cn.inrhor.imipetcore.common.database

import cn.inrhor.imipetcore.common.database.data.PetData
import cn.inrhor.imipetcore.common.database.type.*
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

/**
 * 数据抽象类
 */
abstract class Database {

    /**
     * 更新宠物数据
     * @param uuid
     * @param petData
     */
    abstract fun updatePet(uuid: UUID, petData: PetData)

    /**
     * 删除宠物数据
     */
    abstract fun deletePet(uuid: UUID, pUUID: UUID)

    /**
     * 拉取数据
     * @param uuid
     */
    abstract fun pull(uuid: UUID)

    companion object {

        lateinit var database: Database

        fun initDatabase() {
            database = when (DatabaseManager.type) {
                DatabaseType.MYSQL -> DatabaseSQL()
                else -> DatabaseLocal()
            }
        }

        @SubscribeEvent
        fun join(ev: PlayerJoinEvent) {
            database.pull(ev.player.uniqueId)
        }

    }

}