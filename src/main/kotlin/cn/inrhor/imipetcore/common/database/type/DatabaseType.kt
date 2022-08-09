package cn.inrhor.imipetcore.common.database.type

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

}