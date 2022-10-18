package top.e404.dungeon_generator.path

class Generator(
    val dungeonWidth: Int,
    val dungeonHeight: Int,
    val roomWidth: IntRange = 10..25,
    val roomHeight: IntRange = 10..25,
    val roomTry: Int = 200,
    val pathTry: Int = 20
) {
    val dungeon = Dungeon(dungeonWidth, dungeonHeight)

    /**
     * 生成图片
     */
    fun generator() {
        dungeon.genRoom(roomTry, roomWidth, roomHeight)
        dungeon.rooms.forEach { dungeon.fill(it) }
        var i = 0
        repeat(pathTry) {
            println(it)
            PathBuilder(
                dungeon = dungeon,
                turnChance = 2,
                step = 100,
                room = dungeon.rooms[i++ % dungeon.rooms.size]
            ).run()
        }
    }
}