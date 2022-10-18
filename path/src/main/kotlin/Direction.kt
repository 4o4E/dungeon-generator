package top.e404.dungeon_generator.path

enum class Direction(val id: Int, val forward: Location) {
    UP(0, Location(1, 0)),
    DOWN(1, Location(-1, 0)),
    LEFT(2, Location(0, -1)),
    RIGHT(3, Location(0, 1)),
}