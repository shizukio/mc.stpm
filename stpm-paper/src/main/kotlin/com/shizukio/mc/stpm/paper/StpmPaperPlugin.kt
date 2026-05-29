package com.shizukio.mc.stpm.paper

import com.shizukio.mc.stpm.StpmBukkitRuntime

import org.bukkit.plugin.java.JavaPlugin

/**
 * Paper loader entry point.
 *
 * 今の STPM は Bukkit API だけで実装できているため、
 * Paper 版は Bukkit runtime を呼び出す薄い adapter です。
 *
 * 将来 Paper 固有 API を使う場合は、この module に Paper 専用 adapter を追加し、
 * stpm-bukkit の実装を差し替えるか、Paper 専用の実装をここから呼びます。
 */
class StpmPaperPlugin : JavaPlugin() {

    override fun onLoad() {
        StpmBukkitRuntime.onLoad(
            this,
            PaperAdventureMessageSender
        )
    }

    override fun onEnable() {
        StpmBukkitRuntime.onEnable()
    }

    override fun onDisable() {
        StpmBukkitRuntime.onDisable()
    }
}
