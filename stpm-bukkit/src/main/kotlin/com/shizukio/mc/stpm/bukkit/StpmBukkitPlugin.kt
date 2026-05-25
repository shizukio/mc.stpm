package com.shizukio.mc.stpm.bukkit

import com.shizukio.mc.stpm.StpmBukkitRuntime

import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit loader entry point.
 *
 * plugin.yml の main から最初に呼ばれる class です。
 *
 * ここには Bukkit から呼ばれる lifecycle だけを書きます。
 * 実際の処理は stpm-bukkit の StpmBukkitRuntime に渡します。
 * stpm-core には loader 非依存の model / enum だけを残します。
 */
class StpmBukkitPlugin : JavaPlugin() {

    override fun onLoad() {
        StpmBukkitRuntime.onLoad(this)
    }

    override fun onEnable() {
        StpmBukkitRuntime.onEnable()
    }

    override fun onDisable() {
        StpmBukkitRuntime.onDisable()
    }
}
