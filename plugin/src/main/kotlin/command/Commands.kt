package top.e404.edungeongenerator.command

import top.e404.edungeongenerator.PL
import top.e404.eplugin.command.ECommandManager

object Commands : ECommandManager(
    PL,
    "edungeongenerator",
    Debug,
    Reload,
    Test,
)