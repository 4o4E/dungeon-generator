package top.e404.dungeon_generator.path

/**
 * 地牢生成器, 填充地牢
 *
 * @property dungeonWidth 地牢宽度
 * @property dungeonHeight 地牢高度
 * @property roomWidth 房间宽度范围
 * @property roomHeight 房间高度范围
 * @property roomTry 生成房间的尝试次数
 * @property pathTry 生成路径的尝试次数
 * @property pathTurnChance 路径生成时尝试转弯的几率
 * @property pathStep 路径生成时的最大步数
 * @property pathStepFailLimit 路径生成时每一步的最大失败次数
 * @property pathMagic 路径生成时的参数
 */
class Generator(
    val dungeonWidth: Int,
    val dungeonHeight: Int,
    val roomWidth: IntRange = 10..25,
    val roomHeight: IntRange = 10..25,
    val roomTry: Int = 200,
    val pathTry: Int = 200,
    val pathTurnChance: Int = 4,
    val pathStep: Int = 100,
    val pathStepFailLimit: Int = 500,
    val pathMagic: Int = 4
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
                turnChance = pathTurnChance,
                step = pathStep,
                stepFailLimit = pathStepFailLimit,
                location = dungeon.rooms[i++ % dungeon.rooms.size].wall.random(),
                magic = pathMagic
            )
            dungeon[builder.location] = State.PATH
            builder.start()
            builder.check()
        }
        dungeon.removeDeadEnd()
        dungeon.removeConnectionlessRoom()
    }
}