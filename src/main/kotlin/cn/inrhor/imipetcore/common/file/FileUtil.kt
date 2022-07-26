package cn.inrhor.imipetcore.common.file

import cn.inrhor.imipetcore.ImiPetCore
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

/**
 * 返回文件夹的内容
 *
 * @param child 路径
 * @param outStr 文件不存在时输出Lang
 * @param mkdirs 是否创建 example.yml
 */
fun getFile(child: String, outStr: String, mkdirs: Boolean, example: String = "example"): File {
    val file = File(ImiPetCore.plugin.dataFolder, child)
    if (!file.exists() && mkdirs) { // 如果 <child> 文件夹不存在就给示例配置
        if (outStr.isNotEmpty()) {
            console().sendLang(outStr)
        }
        ImiPetCore.resource.releaseResourceFile("$child/$example.yml", true)
    }
    return file
}

fun getFileList(file: File): List<File> =
    mutableListOf<File>().let { files ->
        if (file.isDirectory) {
            file.listFiles()!!.forEach { files.addAll(getFileList(it)) }
        }else if (file.name.endsWith(".yml", true)) {
            files.add(file)
        }
        return@let files
    }