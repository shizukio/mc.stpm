package com.shizukio.mc.stpm

import org.bukkit.Color
import org.bukkit.configuration.file.FileConfiguration

/**
 * STPM config manager
 *
 * config.yml の値を:
 * - 起動時
 * - reload時
 *
 * に memory cache する
 *
 * runtime 中は:
 * config.yml を毎回読まず、
 * memory 上の値を参照する
 */
object Cfg {

    /**
     * Bukkit config shortcut
     */
    private val bukkitConfig: FileConfiguration

        get() = StpmBukkitRuntime.plugin.config

    // ----------------------------------------------------------------
    // debug
    // ----------------------------------------------------------------

    /**
     * debug default enabled
     */
    var debugEnabled: Boolean =
        false
        private set

    // ----------------------------------------------------------------
    // render
    // ----------------------------------------------------------------

    /**
     * renderer update interval
     */
    var renderInterval: Long =
        10L
        private set

    /**
     * render color
     */
    lateinit var renderColor: Color
        private set

    /**
     * max render distance
     */
    var renderMaxDistance: Double =
        100.0
        private set

    /**
     * detail render distance
     */
    var renderDetailDistance: Double =
        10.0
        private set

    // ----------------------------------------------------------------
    // lifecycle
    // ----------------------------------------------------------------

    /**
     * config reload
     *
     * config.yml の内容を
     * memory cache へ反映
     */
    fun reload() {

        /**
         * 最新 config 読み込み
         */
        StpmBukkitRuntime.plugin.reloadConfig()

        // shortcut
        val config =
            bukkitConfig

        // ------------------------------------------------------------
        // debug
        // ------------------------------------------------------------

        debugEnabled =
            config.getBoolean(
                "debug.enabled"
            )

        // ------------------------------------------------------------
        // render
        // ------------------------------------------------------------

        renderInterval =
            config.getLong(
                "render.interval"
            )

        renderMaxDistance =
            config.getDouble(
                "render.max-distance"
            )

        renderDetailDistance =
            config.getDouble(
                "render.detail-distance"
            )

        /**
         * render color
         */
        renderColor =
            parseColor(

                config.getString(
                    "render.color"
                ) ?: "#ffffff"
            )
    }

    // ----------------------------------------------------------------
    // utility
    // ----------------------------------------------------------------

    /**
     * HEX color parser
     *
     * "#00ffff"
     * ↓
     * Bukkit Color
     */
    private fun parseColor(
        hex: String
    ): Color {

        val rawHex =
            hex.removePrefix("#")

        val rgb =
            Integer.parseInt(
                rawHex,
                16
            )

        return Color.fromRGB(rgb)
    }
}
