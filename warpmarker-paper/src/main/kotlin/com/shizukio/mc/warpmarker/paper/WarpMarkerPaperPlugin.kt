package com.shizukio.mc.warpmarker.paper

import com.shizukio.mc.warpmarker.WarpMarkerBukkitRuntime

import org.bukkit.plugin.java.JavaPlugin

/**
 * Paper loader entry point.
 *
 * 今の WarpMarker は Bukkit API だけで実装できているため、
 * Paper 版は Bukkit runtime を呼び出す薄い adapter です。
 *
 * 将来 Paper 固有 API を使う場合は、この module に Paper 専用 adapter を追加し、
 * warpmarker-bukkit の実装を差し替えるか、Paper 専用の実装をここから呼びます。
 */
class WarpMarkerPaperPlugin : JavaPlugin() {

    override fun onLoad() {
        WarpMarkerBukkitRuntime.onLoad(
            this,
            PaperAdventureMessageSender
        )
    }

    override fun onEnable() {
        WarpMarkerBukkitRuntime.onEnable()
    }

    override fun onDisable() {
        WarpMarkerBukkitRuntime.onDisable()
    }
}
