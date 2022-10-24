package top.e404.edungeongenerator.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import top.e404.eplugin.config.ESerializationConfig
import top.e404.eplugin.config.JarConfig
import top.e404.edungeongenerator.PL

object Config : ESerializationConfig<ConfigData>(
    plugin = PL,
    path = "config.yml",
    default = JarConfig(PL, "config.yml"),
    serializer = ConfigData.serializer(),
    format = Yaml.default
) {
    var debug: Boolean
        get() = config.debug
        set(value) {
            config.debug = value
        }
    val world: String
        get() = config.world
    val radius: Int
        get() = config.radius
}

@Serializable
data class ConfigData(
    var debug: Boolean,
    val world: String,
    val radius: Int,
)