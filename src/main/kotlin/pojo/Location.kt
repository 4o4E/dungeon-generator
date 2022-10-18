package top.e404.dungeon_generator.pojo

data class Location(var x: Int, var y: Int) {
    operator fun plus(l: Location) = plus(l.x, l.y)

    fun plus(x: Int, y: Int) = apply {
        this.x += x
        this.y += y
    }

    operator fun minus(l: Location) = minus(l.x, l.y)

    fun minus(x: Int, y: Int) = apply {
        this.x -= x
        this.y -= y
    }
}
