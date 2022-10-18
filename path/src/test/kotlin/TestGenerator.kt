package top.e404.dungeon_generator.test

import org.junit.jupiter.api.Test
import top.e404.dungeon_generator.path.Generator
import java.io.File
import javax.imageio.ImageIO

class TestGenerator {
    @Test
    fun t() {
        val g = Generator(
            dungeonLength = 400,
            dungeonWidth = 400,
            roomWidth = 10..20,
            roomHeight = 10..20,
            roomTry = 400,
            pathTry = 1600,
            pathTurnChance = 4,
            pathStep = 100,
            pathStepFailLimit = 500
        )
        g.generator()
        ImageIO.write(g.dungeon.toImage(), "png", File("out.png"))
    }
}