package top.e404.edungeongenerator.config

import io.lumine.mythic.api.mobs.MythicMob
import io.lumine.mythic.bukkit.adapters.BukkitItemStack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack
import top.e404.edungeongenerator.hook.IaHook
import top.e404.edungeongenerator.hook.MiHook
import top.e404.edungeongenerator.hook.MmHook
import top.e404.eplugin.config.serialization.IntRangeSerialization
import top.e404.eplugin.table.Tableable
import top.e404.eplugin.util.materialOf
import top.e404.eplugin.util.toItemStack
import java.util.NoSuchElementException

@Serializable
sealed class Treasure : Tableable<ItemStack> {
    abstract override val weight: Int
    abstract val count: IntRange
    abstract override fun generator(): ItemStack
}

@Serializable
@SerialName("mc")
data class McTreasure(
    val id: String,
    override val weight: Int,
    @Serializable(IntRangeSerialization::class)
    override val count: IntRange
) : Treasure() {
    override fun generator() = materialOf(id)?.toItemStack()
        ?: throw Exception("invalid item type: $id")
}

@Serializable
@SerialName("mi")
data class MiTreasure(
    val category: String,
    val id: String,
    override val weight: Int,
    @Serializable(IntRangeSerialization::class)
    override val count: IntRange
) : Treasure() {
    override fun generator() = MiHook.getItem(category, id)?.newBuilder()?.build()
        ?: throw Exception("invalid mmoitems item(type: $category, id: $id)")
}

@Serializable
@SerialName("mm")
data class MmTreasure(
    val id: String,
    override val weight: Int,
    @Serializable(IntRangeSerialization::class)
    override val count: IntRange
) : Treasure() {
    @ExperimentalStdlibApi
    override fun generator() = (MmHook.getItem(id)?.generateItemStack(1) as BukkitItemStack?)?.build()
        ?: throw Exception("invalid mythicmobs item(id: $id)")
}

@Serializable
@SerialName("ia")
data class IaTreasure(
    val namespaceId: String,
    override val weight: Int,
    @Serializable(IntRangeSerialization::class)
    override val count: IntRange
) : Treasure() {
    override fun generator() = IaHook.getIaItem(namespaceId)?.itemStack
        ?: throw Exception("invalid itemsadder item $namespaceId")
}

@Serializable
data class Monster(
    val type: String,
    @Serializable(IntRangeSerialization::class)
    val amount: IntRange,
    override val weight: Int
) : Tableable<MythicMob> {
    override fun generator(): MythicMob = MmHook.getMythicMob(type)
        ?: throw NoSuchElementException("cannot find mythicmobs mob by type $type")
}