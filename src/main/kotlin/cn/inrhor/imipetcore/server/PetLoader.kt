package cn.inrhor.imipetcore.server

import cn.inrhor.imipetcore.api.data.DataContainer
import cn.inrhor.imipetcore.api.entity.state.ActiveState
import taboolib.common.LifeCycle
import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake

object PetLoader {

    @Suppress("UNCHECKED_CAST")
    @Awake(LifeCycle.ENABLE)
    fun registerState() {
        runningClasses.forEach {
            if (ActiveState::class.java.isAssignableFrom(it) && ActiveState::class.java != it) {
                val i = it.getInstance(true)?.get()
                val a = i as? ActiveState
                if (a != null) {
                    DataContainer.actionMap[a.action] = it
                }
            }
        }
    }

}