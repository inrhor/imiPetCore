package cn.inrhor.imipetcore.server

import taboolib.module.nms.MinecraftVersion

object ReadManager {

    /**
     * 主版本号
     */
    val major = MinecraftVersion.major

    /**
     * 次版本号
     */
    val minor = MinecraftVersion.minor

    /**
     * 是否为1.17+
     */
    val isUniversal = MinecraftVersion.isUniversal

}