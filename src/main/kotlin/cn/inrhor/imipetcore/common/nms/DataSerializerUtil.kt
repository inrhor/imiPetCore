package cn.inrhor.imipetcore.common.nms

import taboolib.module.nms.DataSerializer
import taboolib.module.nms.DataSerializerFactory

object DataSerUtil {

    fun createDataSerializer(builder: DataSerializer.() -> Unit): DataSerializer {
        return DataSerializerFactory.instance.newSerializer().also(builder)
    }

}