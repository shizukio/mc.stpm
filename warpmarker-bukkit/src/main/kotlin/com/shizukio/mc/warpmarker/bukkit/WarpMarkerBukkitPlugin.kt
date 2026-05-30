package com.shizukio.mc.warpmarker.bukkit

import com.shizukio.mc.warpmarker.WarpMarkerBukkitRuntime

import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit loader entry point.
 *
 * plugin.yml の main から最初に呼ばれる class です。
 *
 * ここには Bukkit から呼ばれる lifecycle だけを書きます。
 * 実際の処理は warpmarker-bukkit の WarpMarkerBukkitRuntime に渡します。
 * warpmarker-core には loader 非依存の model / enum だけを残します。
 */
class WarpMarkerBukkitPlugin : JavaPlugin() {

    override fun onLoad() {
        WarpMarkerBukkitRuntime.onLoad(this)
    }

    override fun onEnable() {
        WarpMarkerBukkitRuntime.onEnable()
    }

    override fun onDisable() {
        WarpMarkerBukkitRuntime.onDisable()
    }
}
