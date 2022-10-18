package top.e404.dungeon_generator.pojo

import java.awt.Color

enum class State(val color: Int) {
    ROOM(Color.ORANGE.rgb), // 房间
    WALL(Color.DARK_GRAY.rgb), // 墙壁
    PATH(Color.WHITE.rgb), // 通道
    TREASURE(Color.RED.rgb), // 宝箱
    SPAWNER(Color.GREEN.rgb), // 宝箱
}