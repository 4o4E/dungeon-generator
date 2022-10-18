package top.e404.dungeon_generator.path

import kotlin.random.Random

/**
 * 路径生成器
 *
 * @property dungeon 属于的地牢
 * @property turnChance 转弯的几率 实际几率为 1 / (turnChance + 1)
 * @property step 最大步数
 * @property stepFailLimit 每一步允许的最多失败次数 *
 * @property location 开始的点
 * @property magic 神奇的参数
 */
class PathBuilder(
    val dungeon: Dungeon,
    val turnChance: Int,
    var step: Int,
    val stepFailLimit: Int = 500,
    val location: Location,
    val magic: Int = 5
) {
    var direction = Direction.RIGHT
    var lastDirection = Direction.RIGHT
    var fail = false
    var path = mutableListOf(location.copy())

    fun start() {
        while (!fail && step > 0) run()
    }

    fun undo() {
        location - direction.forward
        direction = lastDirection
    }

    fun run() {
        val usable = Direction.values()
            .filterNot { (it.id - direction.id) % 2 == 0 }
            .toMutableList()
        var failCount = 0
        while (true) {
            // 所有可用方向都失败了
            if (usable.isEmpty()) {
                fail = true
                return
            }
            // 尝试转向
            if (Random.nextInt(turnChance) == 0) {
                lastDirection = direction
                direction = usable.random().also {
                    usable.remove(it)
                }
            }
            location + direction.forward
            if (dungeon.containsInternal(location)
                && dungeon.countRound8(location) {
                    it == State.WALL
                } > magic
            ) break
            undo()
            if (failCount++ > stepFailLimit) return
            continue
        }
        //println(location)
        dungeon[location] = State.PATH
        path.add(location.copy())
        step--
    }

    fun check() {
        if (path.size < 10) path.forEach {
            dungeon[it] = State.WALL
        }
    }
}