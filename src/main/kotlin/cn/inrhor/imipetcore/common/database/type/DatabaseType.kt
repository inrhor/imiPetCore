package cn.inrhor.imipetcore.common.database.type

import cn.inrhor.imipetcore.ImiPetCore
import cn.inrhor.imipetcore.common.database.Database
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * 数据类型
 */
enum class DatabaseType {
    LOCAL, MYSQL
}

/**
 * 数据管理器
 */
object DatabaseManager {

    var type = DatabaseType.LOCAL

    @Awake(LifeCycle.ENABLE)
    fun init() {
        if (ImiPetCore.config.getString("data.type")?.uppercase() == "MYSQL") {
            type = DatabaseType.MYSQL
        }
        Database.initDatabase()
    }

}