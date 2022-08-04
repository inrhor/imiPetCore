package cn.inrhor.imipetcore.command

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.data.DataContainer.petOptionMap
import cn.inrhor.imipetcore.api.manager.PetManager.addPet
import cn.inrhor.imipetcore.api.manager.PetManager.deletePet
import cn.inrhor.imipetcore.common.script.kether.eval
import cn.inrhor.imipetcore.common.ui.UiData.homePetUi
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper

/**
 * /imiPetCore
 * /pet
 */
@CommandHeader(name = "imiPetCore", aliases = ["pet"], permission = "imipetcore.command")
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody(permission = "imipetcore.use.open")
    val open = subCommand {
        execute<Player> { sender, _, _ ->
            homePetUi.open(sender)
        }
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

    @CommandBody(permission = "imipetcore.admin.remove")
    val remove = subCommand {
        dynamic(commit = "player") {
            suggestion<CommandSender> { _, _ -> Bukkit.getOnlinePlayers().map { it.name } }
            dynamic(commit = "name") {
                suggestion<CommandSender> { _, context ->
                    Bukkit.getPlayer(context.argument(-1))?.getData()?.petDataList?.map { it.name }
                }
                execute<CommandSender> { sender, context, argument ->
                    val player = Bukkit.getPlayer(context.argument(-1))?: return@execute run {
                        // lang
                    }
                    val args = argument.split(" ")
                    player.deletePet(args[0])
                    // lang
                }
            }
        }
    }

    @CommandBody(permission = "imipetcore.admin.eval")
    val eval = subCommand {
        dynamic {
            execute<Player> { sender, _, argument ->
                sender.eval(argument)
            }
        }
    }

}