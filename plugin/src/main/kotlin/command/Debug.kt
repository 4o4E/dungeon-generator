package top.e404.edungeongenerator.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.eplugin.EPlugin.Companion.color
import top.e404.eplugin.command.AbstractDebugCommand
import top.e404.edungeongenerator.PL
import top.e404.edungeongenerator.config.Config
import top.e404.edungeongenerator.config.Lang

object Debug : AbstractDebugCommand(
    PL,
    "edungeongenerator.admin"
) {
    override val usage: String
        get() = Lang["command.usage.debug"].color()

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>
    ) {
        if (sender !is Player) {
            if (Config.debug) {
                Config.debug = false
                plugin.sendMsgWithPrefix(sender, Lang["debug.console_disable"])
            } else {
                Config.debug = true
                plugin.sendMsgWithPrefix(sender, Lang["debug.console_enable"])
            }
            return
        }
        val senderName = sender.name
        if (senderName in plugin.debuggers) {
            plugin.debuggers.remove(senderName)
            plugin.sendMsgWithPrefix(sender, Lang["debug.player_disable"])
        } else {
            plugin.debuggers.add(senderName)
            plugin.sendMsgWithPrefix(sender, Lang["debug.player_enable"])
        }
    }
}