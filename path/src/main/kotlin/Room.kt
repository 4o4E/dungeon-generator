package top.e404.dungeon_generator.path

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
        for (ix in x until x + w) {
            list.add(Location(ix, y - 1))
            list.add(Location(ix, y + h))
        }
        for (iy in y until y + h) {
            list.add(Location(x - 1, iy))
            list.add(Location(x + w, iy))
        }
        list
    }

    fun forEachWall(block: (location: Location) -> Unit) = wall.forEach(block)

    operator fun contains(location: Location) = location.x >= x
            && location.x < x + w
            && location.y >= y
            && location.y < y + h
}