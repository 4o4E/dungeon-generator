package top.e404.edungeongenerator.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import top.e404.eplugin.config.EMapConfig
import top.e404.eplugin.config.JarConfig
import top.e404.edungeongenerator.PL

object MonsterManager : EMapConfig<String, List<Monster>>(
    PL,
    "monster.yml",
    JarConfig(PL, "monster.yml"),
    String.serializer(),
    ListSerializer(Monster.serializer()),
    Yaml.default,
    false
)