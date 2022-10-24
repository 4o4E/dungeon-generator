package top.e404.edungeongenerator.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.serializer
import org.bukkit.command.CommandSender
import top.e404.eplugin.config.EMapConfig
import top.e404.eplugin.config.JarConfig
import top.e404.edungeongenerator.PL

object RoomManager : EMapConfig<String, PathDungeonConfig>(
    PL,
    "room.yml",
    JarConfig(PL, "room.yml"),
    String.serializer(),
    PathDungeonConfig.serializer(),
    Yaml.default,
    false
) {
    override fun onLoad(config: MutableMap<String, PathDungeonConfig>, sender: CommandSender?) {
        super.onLoad(config, sender)
    }
}