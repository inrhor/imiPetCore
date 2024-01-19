package cn.inrhor.imipetcore.server

import taboolib.module.configuration.ConfigNode

object ConfigRead {

    @ConfigNode("nms")
    var nms = "taboolib"
        private set

    @ConfigNode("debug")
    var debug = false
        private set

    @ConfigNode("delay.joinSpawnPet")
    var delayJoinSpawnPet = 5
        private set

}