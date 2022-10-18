package top.e404.dungeon_generator

import top.e404.dungeon_generator.pojo.Data
import top.e404.dungeon_generator.pojo.Location
import top.e404.dungeon_generator.pojo.Room
import top.e404.dungeon_generator.pojo.State
import java.awt.image.BufferedImage
import kotlin.random.Random

class Dungeon(
    val width: Int,
    val height: Int,
    val roomWidth: IntRange = 10..25,
    val roomHeight: IntRange = 10..25,
    val roomPadding: Int = 2,
    val roomTry: Int = 200,
    val fillChance: Int = 2,
    val fillPadding: Int = 4,
    val smooth: Int = 2,
    val deadEndLimit: Int = 20,
    val deadEndRecursionLimit: Int = 10,
    val loopLimit: Int = 10,
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

    fun forEach(block: (x: Int, y: Int, s: State) -> Unit) {
        for (y in 0 until height) for (x in 0 until width) {
            block(x, y, get(x, y))
        }
    }

    fun forEachInternal(block: (x: Int, y: Int, s: State) -> Unit) {
        for (y in 1 until height - 1) for (x in 1 until width - 1) {
            block(x, y, get(x, y))
        }
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

    fun <T> round9(location: Location, block: (l: Location, state: State) -> T) = list9.map {
        val l = location.copy().plus(it)
        block(l, get(l))
    }

    fun countRound9(location: Location, condition: (state: State) -> Boolean) = list9.count {
        val l = location.copy().plus(it)
        condition(get(l))
    }

    private val list4 = listOf(
        Location(0, -1),
        Location(-1, 0),
        Location(1, 0),
        Location(0, 1),
    )

    fun <T> round4(location: Location, block: (l: Location, state: State) -> T) = list4.map {
        val l = location.copy().plus(it)
        block(l, get(l))
    }

    fun countRound4(location: Location, condition: (state: State) -> Boolean) = list4.count {
        val l = location.copy().plus(it)
        condition(get(l))
    }

    fun smooth(location: Location) = round9(location) { _, s -> s }.count {
        it == State.WALL || it == State.ROOM
    } < 4

    /**
     * 生成新房间
     */
    private fun newRoom(): Room {
        val w = roomWidth.random()
        val h = roomHeight.random()
        var x: Int
        var y: Int
        do {
            x = Random.nextInt(roomPadding, width - w - roomPadding)
            y = Random.nextInt(roomPadding, height - h - roomPadding)
        } while (x < 3 && y < 3)
        return Room(x, y, w, h)
    }

    /**
     * 生成房间
     */
    fun genRoom() = repeat(roomTry) {
        val room = newRoom()
        if (rooms.any { it.isOverlap(room) }) return@repeat
        rooms.add(room)
        room.forEach { l ->
            set(l, State.ROOM)
        }
        //room.forEachWall {l ->
        //    set(l, State.WALL)
        //}
    }

    /**
     * 生成路径
     */
    fun genPath() {
        for (y in fillPadding until height - fillPadding) for (x in fillPadding until width - fillPadding) {
            this[x, y] = if (Random.nextInt(fillChance) == 1) State.WALL else State.PATH
        }
        repeat(smooth) {
            for (y in 1 until height - 1) for (x in 1 until width - 1) {
                this[x, y] = if (smooth(Location(x, y))) State.WALL else State.PATH
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

    private fun removeBoxFun(x: Int, y: Int) {
        if (this[x, y] == State.PATH
            && this[x + 1, y] == State.PATH
            && this[x, y + 1] == State.PATH
            && this[x + 1, y + 1] == State.PATH
        ) {
            this[x, y] = State.WALL
            this[x + 1, y] = State.WALL
            this[x, y + 1] = State.WALL
            this[x + 1, y + 1] = State.WALL
        }
    }

    /**
     * 移除环形道路
     */
    fun removeBox() {
        for (y in 1 until height - 1) for (x in 1 until width - 1) {
            removeBoxFun(x, y)
        }
    }

    private fun loop(
        x: Int,
        y: Int,
        end: Data<Boolean>,
        limit: Data<Int>,
        set: MutableSet<Location>
    ) {
        if (this[x, y] == State.ROOM) {
            end.data = true
            return
        }
        if (end.data || this[x, y] != State.PATH) return
        if (set.size > loopLimit) {
            end.data = true
            return
        }
        limit.data++
        if (!set.add(Location(x, y))) return
        loop(x + 1, y, end, limit, set)
        if (end.data) return
        loop(x - 1, y, end, limit, set)
        if (end.data) return
        loop(x, y + 1, end, limit, set)
        if (end.data) return
        loop(x, y - 1, end, limit, set)
    }

    fun removeLoopFun(x: Int, y: Int, ignore: MutableList<Location>) {
        if (this[x, y] != State.PATH
            || Location(x, y) in ignore
            || x >= width
            || y >= height
        ) return
        val set = hashSetOf<Location>()
        val end = Data(false)
        loop(x, y, end, Data(0), set)
        if (end.data) {
            ignore.addAll(set)
            //println("end: $set")
            return
        }
        //println("rm: $set")
        set.forEach { (x, y) ->
            this[x, y] = State.WALL
        }
    }

    fun removeLoop() {
        val ignore = mutableListOf<Location>()
        for (y in 1 until height - 1) for (x in 1 until width - 1) {
            removeLoopFun(x, y, ignore)
        }
    }

    fun generator() {
        genPath()
        genRoom()
        removeDeadEnd()
        removeBox()
        removeLoop()
        removeBox()
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