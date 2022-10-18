package top.e404.dungeon_generator.path

import kotlin.random.Random

class PathBuilder(
    val dungeon: Dungeon,
    val turnChance: Int,
    var step: Int,
    room: Room? = null
) {
    /**
     * 当前位置
     */
    val location = Location(0, 0).also { l ->
        room?.let { r ->
            l.plus(r.wall.random())
        }
    }

    /**
     * 当前方向
     */
    var direction = Direction.RIGHT

    /**
     * 转向
     */
    fun turn() {
        if (Random.nextInt(turnChance) == 1) {
            direction = Direction.values().toMutableList().apply {
                remove(direction)
            }.random()
        }
    }

    /**
     * 前进
     */
    fun run() {
        var fail = 0
        while (true) {
            turn()
            location + direction.forward
            if (dungeon.containsInternal(location)
                && dungeon.countRound8(location) {
                    it == State.WALL
                } > 5
            ) break
            location - direction.forward
            fail++
            if (fail > 500) return
            continue
        }
        //println(location)
        if (dungeon[location] == State.ROOM) return
        dungeon[location] = State.PATH
        step--
        if (step <= 0) return
        run()
    }
}

enum class Direction(val forward: Location) {
    UP(Location(1, 0)),
    DOWN(Location(-1, 0)),
    LEFT(Location(0, -1)),
    RIGHT(Location(0, 1)),
}