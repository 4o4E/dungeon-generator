package top.e404.dungeon_generator.pojo

data class Room(
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
) {
    fun isOverlap(other: Room) = other.x + other.w >= x - 1
            && other.y + other.h >= y - 1
            && x + w >= other.x - 1
            && y + h >= other.y - 1

    fun forEach(block: (x: Int, y: Int) -> Unit) {
        for (iy in y until y + h) for (ix in x until x + w) {
            block(ix, iy)
        }
    }

    val blocks by lazy {
        val list = ArrayList<Location>()
        for (iy in y until y + h) for (ix in x until x + w) {
            list.add(Location(ix, iy))
        }
        list
    }

    fun forEach(block: (location: Location) -> Unit) = blocks.forEach(block)

    val wall by lazy {
        val list = ArrayList<Location>()
        for (iy in y - 1 until y + h + 1) {
            list.add(Location(x, iy))
            list.add(Location(x + w, iy))
        }
        for (ix in x + 1 until x + w - 1) {
            list.add(Location(ix, y))
            list.add(Location(ix, y))
        }
        list
    }

    fun forEachWall(block: (location: Location) -> Unit) = wall.forEach(block)

    operator fun contains(location: Location) = location.x >= x
            && location.x < x + w
            && location.y >= y
            && location.y < y + h
}