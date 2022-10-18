package top.e404.edungeongenerator

import top.e404.eplugin.EPlugin
import top.e404.edungeongenerator.command.Commands
import top.e404.edungeongenerator.config.Config
import top.e404.edungeongenerator.config.DungeonManager
import top.e404.edungeongenerator.config.Lang

class EDungeonGenerator : EPlugin() {
    override val debugPrefix: String
        get() = langManager.getOrElse("debug_prefix") { "&7[&bDungeonGeneratorDebug&7]" }
    override val prefix: String
        get() = langManager.getOrElse("prefix") { "&7[&6DungeonGenerator&7]" }

    override var debug: Boolean
        get() = Config.debug
        set(value) {
            Config.debug = value
        }
    override val langManager by lazy { Lang }

    override fun onEnable() {
        PL = this
        Config.load(null)
        Lang.load(null)
        DungeonManager.load(null)
        Commands.register()
        info("&a加载完成")
    }

    override fun onDisable() {
        cancelAllTask()
        info("&a卸载完成")
    }
}

lateinit var PL: EDungeonGenerator
    private set