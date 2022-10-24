package top.e404.edungeongenerator.hook

import top.e404.edungeongenerator.PL
import top.e404.eplugin.hook.EHookManager
import top.e404.eplugin.hook.bentobox.ItemsAdderHook
import top.e404.eplugin.hook.mmoitems.MmoitemsHook
import top.e404.eplugin.hook.mmoitems.MythicMobsHook

object HookManager : EHookManager(
    PL,
    MiHook,
    MmHook,
    IaHook,
)

object MiHook : MmoitemsHook(PL)
object MmHook : MythicMobsHook(PL)
object IaHook : ItemsAdderHook(PL)