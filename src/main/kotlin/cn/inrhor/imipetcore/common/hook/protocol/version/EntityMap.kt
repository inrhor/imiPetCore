package cn.inrhor.imipetcore.common.hook.protocol.version

abstract class EntityMap {

    abstract fun getEntityId(entityType: String): Int

}