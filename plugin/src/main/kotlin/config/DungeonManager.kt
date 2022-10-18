package top.e404.edungeongenerator.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.serializer
import top.e404.eplugin.config.EMapConfig
import top.e404.eplugin.config.JarConfig
import top.e404.edungeongenerator.PL

object DungeonManager : EMapConfig<String, PathDungeonConfig>(
    PL,
    "dungeon.yml",
    JarConfig(PL, "dungeon.yml"),
    String.serializer(),
    PathDungeonConfig.serializer(),
    Yaml.default,
    false
)