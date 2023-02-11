package cn.inrhor.imipetcore.command

import cn.inrhor.imipetcore.api.data.DataContainer.getData
import cn.inrhor.imipetcore.api.data.DataContainer.petOptionMap
import cn.inrhor.imipetcore.api.data.DataContainer.skillOptionMap
import cn.inrhor.imipetcore.api.manager.PetManager.addPet
import cn.inrhor.imipetcore.api.manager.PetManager.deletePet
import cn.inrhor.imipetcore.api.manager.PetManager.getPet
import cn.inrhor.imipetcore.api.manager.SkillManager.addNewSkill
import cn.inrhor.imipetcore.api.manager.SkillManager.getAllSkills
import cn.inrhor.imipetcore.api.manager.SkillManager.removeSkill
import cn.inrhor.imipetcore.common.script.kether.eval
import cn.inrhor.imipetcore.server.ConfigRead
import cn.inrhor.imipetcore.server.PluginLoader.loadTask
import cn.inrhor.imipetcore.server.PluginLoader.logo
import cn.inrhor.imipetcore.server.PluginLoader.unloadTask
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.platform.util.sendLang

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

    @CommandBody(permission = "imipetcore.admin.send")
    val send = subCommand {
        dynamic("player") {
            suggestion<CommandSender> { _, _ -> Bukkit.getOnlinePlayers().map { it.name } }
            dynamic("id") {
                suggestion<CommandSender> { _, _ -> petOptionMap.keys.map { it } }
                dynamic("name") {
                    execute<CommandSender> { sender, context, argument ->
                        val playerName = context["player"]
                        val player = Bukkit.getPlayer(playerName)?: return@execute run {
                            if (ConfigRead.debug) {
                                sender.sendLang("PLAYER_NOT_ONLINE", playerName)
                            }
                        }
                        val args = argument.split(" ")
                        player.addPet(args[0], context["id"])
                        if (ConfigRead.debug) {
                            sender.sendLang("SEND_PET_TO_PLAYER", playerName, args[0])
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "imipetcore.admin.remove")
    val remove = subCommand {
        dynamic("player") {
            suggestion<CommandSender> { _, _ -> Bukkit.getOnlinePlayers().map { it.name } }
            dynamic("name") {
                suggestion<CommandSender> { _, context ->
                    Bukkit.getPlayer(context["player"])?.getData()?.petDataList?.map { it.name }
                }
                execute<CommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayer(context["player"])?: return@execute run {
                        // lang
                    }
                    val args = argument.split(" ")
                    player.deletePet(args[0])
                    // lang
                }
            }
        }
    }

    @CommandBody(permission = "imipetcore.admin.skill")
    val skill = subCommand {
        dynamic("player") {
            suggestion<CommandSender> { _, _ -> Bukkit.getOnlinePlayers().map { it.name } }
            dynamic("name") {
                suggestion<CommandSender> { _, context ->
                    Bukkit.getPlayer(context["player"])?.getData()?.petDataList?.map { it.name }
                }
                literal("add") {
                    dynamic("skill") {
                        suggestion<CommandSender> { _, _ -> skillOptionMap.keys.map { it } }
                        execute<CommandSender> { _, context, argument ->
                            val player = Bukkit.getPlayer(context["player"])?: return@execute run {
                                // lang
                            }
                            val args = argument.split(" ")
                            player.getPet(context["name"]).addNewSkill(player, args[0])
                        }
                    }
                }
                literal("remove") {
                    dynamic("skill") {
                        suggestion<CommandSender> { _, context ->
                            Bukkit.getPlayer(context["player"])?.getPet(context["name"])?.getAllSkills()?.map { it.id }
                        }
                        execute<CommandSender> { _, context, argument ->
                            val player = Bukkit.getPlayer(context["player"])?: return@execute run {
                                // lang
                            }
                            val args = argument.split(" ")
                            player.getPet(context["name"]).removeSkill(player, args[0])
                        }
                    }
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

    @CommandBody(permission = "imipetcore.admin.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            logo()
            unloadTask()
            loadTask()
            sender.sendLang("COMMAND_RELOAD")
        }
    }

}