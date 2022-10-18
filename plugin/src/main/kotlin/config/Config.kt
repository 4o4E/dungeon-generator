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
    val duration: Long
        get() = config.duration
    val push: Long
        get() = config.push
    val url: String
        get() = config.url
    val papi: List<String>
        get() = config.papi
}

@Serializable
data class ConfigData(
    var debug: Boolean,
    val duration: Long,
    val push: Long,
    val url: String,
    val papi: List<String>,
)