package top.e404.dungeon_generator.path

import java.awt.image.BufferedImage
import kotlin.random.Random

class Dungeon(
    val width: Int,
    val height: Int,
    val padding: Int = 5,
    val deadEndLimit: Int = 20,
    val deadEndRecursionLimit: Int = 10,
) {
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

    fun containsInternal(l: Location) = l.x in padding until width - padding
            && l.y in padding until height - padding

    fun forEach(block: (x: Int, y: Int, s: State) -> Unit) {
        for (y in 0 until height) for (x in 0 until width) {
            block(x, y, get(x, y))
        }
    }

    fun forEachInternal(block: (x: Int, y: Int, s: State) -> Unit) {
        for (y in padding until height - padding) for (x in padding until width - padding) {
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

    fun removeConnectionlessRoom() {
        rooms.filter { room ->
            room.wall.all { get(it) == State.WALL }
        }.forEach {
            rooms.remove(it)
            it.forEach { l ->
                set(l, State.WALL)
            }
        }
    }

    private fun removeDeadEndFun(l: Location, limit: Data<Int>): Boolean? {
        if (l.x < 1
            || l.y < 1
            || l.x >= width - 1
            || l.y >= height - 1
        ) return false
        if (countRound4(l) { it == State.WALL } < 3) return false
        limit.data++
        if (limit.data > deadEndRecursionLimit) return null
        this[l] = State.WALL
        if (removeDeadEndFun(l.copy().plus(-1, 0), limit) == null) return null
        if (removeDeadEndFun(l.copy().plus(1, 0), limit) == null) return null
        if (removeDeadEndFun(l.copy().plus(0, -1), limit) == null) return null
        if (removeDeadEndFun(l.copy().plus(0, 1), limit) == null) return null
        return true
    }

    /**
     * 移除死路
     */
    fun removeDeadEnd() {
        repeat(deadEndLimit) {
            forEachInternal { x, y, s ->
                if (s != State.PATH) return@forEachInternal
                val l = Location(x, y)
                if (countRound4(l) { it == State.WALL } >= 3) {
                    removeDeadEndFun(l, Data(0))

                }
                //set(l, State.WALL)
            }
        }
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