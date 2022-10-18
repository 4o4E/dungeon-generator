package top.e404.dungeon_generator.path

import java.awt.image.BufferedImage
import kotlin.random.Random

class Dungeon(val width: Int, val height: Int) {
    val array = Array(width * height) { State.WALL }
    val rooms = mutableListOf<Room>()

    operator fun get(location: Location) = get(location.x, location.y)

    operator fun get(x: Int, y: Int) = array[x + y * width]

    operator fun set(location: Location, state: State) = set(location.x, location.y, state)

    operator fun set(x: Int, y: Int, state: State) {
        array[x + y * width] = state
    }

    operator fun contains(location: Location) = location.x in 0 until width
            && location.y in 0 until height

    fun containsInternal(l: Location) = l.x in 1 until width - 1
            && l.y in 1 until height - 1

    fun forEach(block: (x: Int, y: Int, s: State) -> Unit) {
        for (y in 0 until height) for (x in 0 until width) {
            block(x, y, get(x, y))
        }
    }

    private val list4 = listOf(
        Location(0, -1),
        Location(-1, 0),
        Location(1, 0),
        Location(0, 1),
    )

    fun <T> round4(
        location: Location,
        block: (l: Location, state: State) -> T
    ) = list4.map {
        val l = location.copy().plus(it)
        block(l, get(l))
    }

    fun countRound4(
        location: Location,
        condition: (state: State) -> Boolean
    ) = list4.count {
        condition(get(location.copy().plus(it)))
    }

    private val list8 = listOf(
        Location(-1, -1),
        Location(0, -1),
        Location(1, -1),
        Location(-1, 0),
        Location(1, 0),
        Location(-1, 1),
        Location(0, 1),
        Location(1, 1),
    )

    fun <T> round8(
        location: Location,
        block: (l: Location, state: State) -> T
    ) = list8.map {
        val l = location.copy().plus(it)
        block(l, get(l))
    }

    fun countRound8(
        location: Location,
        condition: (state: State) -> Boolean
    ) = list8.count {
        condition(get(location.copy().plus(it)))
    }

    private val list9 = listOf(
        Location(-1, -1),
        Location(0, -1),
        Location(1, -1),
        Location(-1, 0),
        Location(0, 0),
        Location(1, 0),
        Location(-1, 1),
        Location(0, 1),
        Location(1, 1),
    )

    fun <T> round9(
        location: Location,
        block: (l: Location, state: State) -> T
    ) = list9.map {
        val l = location.copy().plus(it)
        block(l, get(l))
    }

    fun countRound9(
        location: Location,
        condition: (state: State) -> Boolean
    ) = list9.count {
        condition(get(location.copy().plus(it)))
    }

    /**
     * 填充房间
     */
    fun fill(room: Room) {
        room.forEach { (x, y) ->
            this[x, y] = State.ROOM
        }
    }

    /**
     * 生成新房间
     */
    private fun newRoom(roomWidth: IntRange, roomHeight: IntRange): Room {
        val w = roomWidth.random()
        val h = roomHeight.random()
        var x: Int
        var y: Int
        do {
            x = Random.nextInt(1, width - w - 1)
            y = Random.nextInt(1, height - h - 1)
        } while (x < 3 && y < 3)
        return Room(x, y, w, h)
    }

    /**
     * 生成房间
     */
    fun genRoom(roomTry: Int, roomWidth: IntRange, roomHeight: IntRange) = repeat(roomTry) {
        val room = newRoom(roomWidth, roomHeight)
        if (rooms.any { it.isOverlap(room) }) return@repeat
        rooms.add(room)
    }

    /**
     * 生成图片
     */
    fun toImage(): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until height) for (x in 0 until width) {
            image.setRGB(x, y, get(x, y).color)
        }
        return image
    }
}