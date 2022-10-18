package top.e404.dungeon_generator.path

class Generator(
    val dungeonWidth: Int,
    val dungeonHeight: Int,
    val roomWidth: IntRange = 10..25,
    val roomHeight: IntRange = 10..25,
    val roomTry: Int = 200,
    val pathTry: Int = 200
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
            val builder = PathBuilder(
                dungeon = dungeon,
                turnChance = 4,
                step = 100,
                stepFailLimit = 500,
                location = dungeon.rooms[i++ % dungeon.rooms.size].wall.random(),
                magic = 4
            )
            dungeon[builder.location] = State.PATH
            builder.start()
            builder.check()
            val success = if (builder.fail) "fail" else "success"
            val paths = builder.path.joinToString(", ") { (x, y) -> "$x-$y" }
            println("$success: $it [$paths]")
        }
        dungeon.removeDeadEnd()
    }
}