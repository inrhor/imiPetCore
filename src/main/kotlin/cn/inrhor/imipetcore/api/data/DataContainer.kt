package cn.inrhor.imipetcore.api.data

import cn.inrhor.imipetcore.common.database.data.PlayerData
import cn.inrhor.imipetcore.common.option.ActionOption
import cn.inrhor.imipetcore.common.option.IconOption
import cn.inrhor.imipetcore.common.option.PetOption
import cn.inrhor.imipetcore.common.option.SkillOption
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 容器存储管理器
 */
object DataContainer {

    /**
     * 玩家容器
     */
    val playerContainer = ConcurrentHashMap<UUID, PlayerData>()

    /**
     * 宠物配置容器
     */
    val petOptionMap = ConcurrentHashMap<String, PetOption>()

    /**
     * 行为配置容器
     */
    val actionOptionMap = ConcurrentHashMap<String, ActionOption>()

    /**
     * 技能配置容器
     */
    val skillOptionMap = ConcurrentHashMap<String, SkillOption>()

    /**
     * 技能图标容器
     */
    val iconOptionMap = ConcurrentHashMap<String, IconOption>()

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