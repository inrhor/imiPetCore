package cn.inrhor.imipetcore.command

import cn.inrhor.imipetcore.api.data.DataContainer.petOptionMap
import cn.inrhor.imipetcore.api.manager.PetManager.addPet
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

/**
 * /imiPetCore
 * /pet
 */
@CommandHeader(name = "imiPetCore", aliases = ["pet"], permission = "imipetcore.command")
object PetCommand {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody(permission = "imipetcore.admin.send")
    val send = subCommand {
        dynamic(commit = "player") {
            suggestion<CommandSender> { _, _ -> Bukkit.getOnlinePlayers().map { it.name } }
            dynamic(commit = "id") {
                suggestion<CommandSender> { _, _ -> petOptionMap.keys.map { it } }
                dynamic(commit = "name") {
                    execute<CommandSender> { sender, context, argument ->
                        val player = Bukkit.getPlayer(context.argument(-2))?: return@execute run {
                            // lang
                        }
                        val args = argument.split(" ")
                        player.addPet(args[0], context.argument(-1))
                        // lang
                    }
                }
            }
        }
    }

}