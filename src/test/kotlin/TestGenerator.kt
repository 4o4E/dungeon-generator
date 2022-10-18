package top.e404.dungeon_generator.test

import org.junit.jupiter.api.Test
import top.e404.dungeon_generator.Dungeon
import java.io.File
import javax.imageio.ImageIO

class TestGenerator {
    @Test
    fun test() {
        val dungeon = Dungeon(
            width = 200,
            height = 200,
            roomWidth = 10..20,
            roomHeight = 10..20,
            roomPadding = 5,
            roomTry = 100,
            fillChance = 2,
            fillPadding = 3,
            smooth = 2,
            deadEndLimit = 20,
            deadEndRecursionLimit = 180,
            loopLimit = 50
        )
        dungeon.generator()
        ImageIO.write(dungeon.toImage(), "png", File("out.png"))
    }
}