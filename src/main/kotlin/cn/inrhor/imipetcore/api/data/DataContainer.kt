package cn.inrhor.imipetcore.api.data

import cn.inrhor.imipetcore.api.entity.state.ActiveState
import cn.inrhor.imipetcore.common.database.data.PlayerData
import cn.inrhor.imipetcore.common.option.ActionOption
import cn.inrhor.imipetcore.common.option.PetOption
import org.bukkit.entity.Player
import taboolib.common.io.getInstance
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 容器存储管理器
 */
object DataContainer {

    /**
     * 玩家容器
     */
    private val playerContainer = ConcurrentHashMap<UUID, PlayerData>()

    /**
     * 宠物配置容器
     */
    val petOptionMap = ConcurrentHashMap<String, PetOption>()

    /**
     * 行为容器
     */
    val actionMap = ConcurrentHashMap<String, Class<*>>()

    val actionOptionMap = ConcurrentHashMap<String, ActionOption>()

    /**
     * @return 取实例化行为
     */
    fun String.getAction(): ActiveState? {
        if (!actionMap.containsKey(this)) return null
        return actionMap[this]?.getInstance(true)?.get() as ActiveState
    }

    /**
     * @return 获得玩家数据
     */
    fun Player.getData() = playerContainer[uniqueId]?: error("null player playerData")

    /**
     * @return 获得玩家数据
     */
    fun UUID.playerData() = playerContainer[this]?: error("null uuid playerData")

    /**
     * 初始化玩家数据
     */
    fun Player.initData() {
        uniqueId.initData()
    }

    /**
     * 初始化玩家数据
     */
    fun UUID.initData() {
        playerContainer[this] = PlayerData()
    }

}