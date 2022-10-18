package top.e404.edungeongenerator.command

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.eplugin.EPlugin.Companion.color
import top.e404.eplugin.command.ECommand
import top.e404.edungeongenerator.PL
import top.e404.edungeongenerator.config.DungeonManager
import top.e404.edungeongenerator.config.Lang
import top.e404.edungeongenerator.generator.DungeonGenerator
import top.e404.edungeongenerator.generator.DungeonGenerator.toClipboard


object Test : ECommand(
    PL,
    "test",
    "(?i)test|t",
    false,
    "edungeongenerator.admin"
) {
    override val usage: String
        get() = Lang["command.usage.test"].color()

    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        if (!plugin.isPlayer(sender, true)) return
        plugin.runTaskAsync {
            val cfg = DungeonManager["example"]!!
            val clipboard = DungeonGenerator.generator(cfg).toClipboard(cfg)
            sender as Player
            val weWorld = BukkitAdapter.adapt(sender.world)
            WorldEdit.getInstance().newEditSession(weWorld).use { session ->
                val l = sender.location
                val operation = ClipboardHolder(clipboard)
                    .createPaste(session)
                    .to(BlockVector3.at(l.blockX, l.blockY, l.blockZ))
                    .build()
                Operations.complete(operation)
            }
            plugin.sendMsgWithPrefix(sender, "&a完成")
        }
    }
}