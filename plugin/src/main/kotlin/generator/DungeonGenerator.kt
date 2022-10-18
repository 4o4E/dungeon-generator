package top.e404.edungeongenerator.generator

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import top.e404.dungeon_generator.path.Dungeon
import top.e404.dungeon_generator.path.Generator
import top.e404.dungeon_generator.path.Location
import top.e404.dungeon_generator.path.State
import top.e404.edungeongenerator.config.PathDungeonConfig


object DungeonGenerator {
    fun generator(cfg: PathDungeonConfig): Dungeon {
        val g = Generator(
            cfg.dungeonLength,
            cfg.dungeonWidth,
            cfg.roomLength,
            cfg.roomWidth,
            cfg.roomTry,
            cfg.pathTry,
            cfg.pathTurnChance,
            cfg.pathStep,
            cfg.pathStepFailLimit
        )
        g.generator()
        return g.dungeon
    }

    fun Dungeon.toClipboard(cfg: PathDungeonConfig): Clipboard {
        val region = CuboidRegion(
            BlockVector3.at(0, 0, 0),
            BlockVector3.at(cfg.dungeonLength, cfg.dungeonHeight, cfg.dungeonWidth)
        )
        val clipboard = BlockArrayClipboard(region)
        // 底
        for (y in 0 until cfg.dungeonBottom) {
            for (x in 0 until cfg.dungeonLength) for (z in 0 until cfg.dungeonWidth) {
                clipboard.setBlock(
                    BlockVector3.at(x, y, z),
                    BukkitAdapter.asBlockState(ItemStack(Material.MOSSY_STONE_BRICKS))
                )
            }
        }
        // 墙壁
        for (x in 0 until cfg.dungeonLength) for (z in 0 until cfg.dungeonWidth) {
            if (get(Location(x,z)) != State.WALL) continue
            clipboard.setBlock(
                BlockVector3.at(x, cfg.dungeonBottom, z),
                BukkitAdapter.asBlockState(ItemStack(Material.MOSSY_STONE_BRICKS))
            )
            clipboard.setBlock(
                BlockVector3.at(x, cfg.dungeonBottom + 1, z),
                BukkitAdapter.asBlockState(ItemStack(Material.MOSSY_STONE_BRICKS))
            )
        }
        return clipboard
    }
}