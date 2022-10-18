package top.e404.edungeongenerator.command

import org.bukkit.command.CommandSender
import top.e404.eplugin.EPlugin.Companion.color
import top.e404.eplugin.command.ECommand
import top.e404.edungeongenerator.PL
import top.e404.edungeongenerator.config.Config
import top.e404.edungeongenerator.config.DungeonManager
import top.e404.edungeongenerator.config.Lang

object Reload : ECommand(
    PL,
    "reload",
    "(?i)reload|r",
    false,
    "edungeongenerator.admin"
) {
    override val usage: String
        get() = Lang["command.usage.reload"].color()

    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        plugin.runTaskAsync {
            Config.load(sender)
            Lang.load(sender)
            DungeonManager.load(sender)
            plugin.sendMsgWithPrefix(sender, Lang["command.reload_done"])
        }
    }
}