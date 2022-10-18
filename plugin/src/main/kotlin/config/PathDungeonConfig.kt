package top.e404.edungeongenerator.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.e404.eplugin.config.serialization.IntRangeSerialization

/**
 * 地牢生成器参数
 *
 * @property dungeonLength 地牢长度
 * @property dungeonWidth 地牢宽度
 * @property dungeonHeight 地牢高度
 * @property dungeonTop 地牢顶部预留厚度
 * @property dungeonBottom 地牢底部预留厚度
 * @property roomLength 房间宽度范围
 * @property roomWidth 房间高度范围
 * @property roomTry 生成房间的尝试次数
 * @property pathTry 生成路径的尝试次数
 * @property pathTurnChance 路径生成时尝试转弯的几率
 * @property pathStep 路径生成时的最大步数
 * @property pathStepFailLimit 路径生成时每一步的最大失败次数
 */
@Serializable
data class PathDungeonConfig(
    @SerialName("dungeon_length")
    val dungeonLength: Int,
    @SerialName("dungeon_width")
    val dungeonWidth: Int,
    @SerialName("dungeon_height")
    val dungeonHeight: Int,
    @SerialName("dungeon_top")
    val dungeonTop: Int,
    @SerialName("dungeon_bottom")
    val dungeonBottom: Int,
    @Serializable(IntRangeSerialization::class)
    @SerialName("room_length")
    val roomLength: IntRange = 10..25,
    @Serializable(IntRangeSerialization::class)
    @SerialName("room_width")
    val roomWidth: IntRange = 10..25,
    @Serializable(IntRangeSerialization::class)
    @SerialName("room_height")
    val roomHeight: IntRange = 10..25,
    @SerialName("room_try")
    val roomTry: Int = 200,
    @SerialName("path_try")
    val pathTry: Int = 200,
    @SerialName("path_turn_chance")
    val pathTurnChance: Int = 4,
    @SerialName("path_step")
    val pathStep: Int = 100,
    @SerialName("path_step_fail_limit")
    val pathStepFailLimit: Int = 500
)