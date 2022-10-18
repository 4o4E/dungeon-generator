package top.e404.dungeon_generator.test

import org.junit.jupiter.api.Test
import top.e404.dungeon_generator.path.Generator
import java.io.File
import javax.imageio.ImageIO

class TestGenerator {
    @Test
    fun t() {
        val g = Generator(
            dungeonWidth = 100,
            dungeonHeight = 100,
            roomWidth = 10..20,
            roomHeight = 10..20,
            roomTry = 100,
            pathTry = 300
        )
        g.generator()
        ImageIO.write(g.dungeon.toImage(), "png", File("out.png"))
    }
}