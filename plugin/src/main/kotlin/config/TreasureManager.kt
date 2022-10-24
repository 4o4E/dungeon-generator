package top.e404.edungeongenerator.config

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import top.e404.edungeongenerator.PL
import top.e404.eplugin.config.EMapConfig
import top.e404.eplugin.config.JarConfig
import top.e404.eplugin.table.choose

object TreasureManager : EMapConfig<String, List<Treasure>>(
    plugin = PL,
    path = "treasure.yml",
    default = JarConfig(PL, "treasure.yml"),
    kSerializer = String.serializer(),
    vSerializer = ListSerializer(Treasure.serializer()),
    format = Yaml(configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)),
    synchronized = false
) {
    fun generator(
        name: String,
        amount: Int,
        repeat: Boolean = true
    ) = get(name)?.choose(amount, repeat)
        ?: throw NoSuchElementException("cannot find treasure config in treasure.yml by name $name")
}