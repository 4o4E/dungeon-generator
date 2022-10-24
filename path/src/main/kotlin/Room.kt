package top.e404.dungeon_generator.path

data class Room(
    val left: Int,
    val top: Int,
    val length: Int,
    val width: Int
) {
    var height = 0

    fun isOverlap(other: Room) = other.left + other.length >= left - 1
            && other.top + other.width >= top - 1
            && left + length >= other.left - 1
            && top + width >= other.top - 1

    val blocks: List<Location> by lazy {
        val list = ArrayList<Location>()
        for (iy in top until top + width) for (ix in left until left + length) {
            list.add(Location(ix, iy))
        }
        list
    }

    fun forEach(block: (location: Location) -> Unit) = blocks.forEach(block)

    val wall: List<Location> by lazy {
        val list = ArrayList<Location>()
        for (ix in left until left + length) {
            list.add(Location(ix, top - 1))
            list.add(Location(ix, top + width))
        }
        for (iy in top until top + width) {
            list.add(Location(left - 1, iy))
            list.add(Location(left + length, iy))
        }
        list
    }

    fun forEachWall(block: (location: Location) -> Unit) = wall.forEach(block)

    operator fun contains(location: Location) = location.x >= left
            && location.x < left + length
            && location.y >= top
            && location.y < top + width
}