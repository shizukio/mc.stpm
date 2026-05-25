package com.shizukio.mc.stpm.debug

import com.shizukio.mc.stpm.Cfg
import com.shizukio.mc.stpm.StpmBukkitRuntime
import com.shizukio.mc.stpm.utils.Debug

import com.shizukio.mc.stpm.debug.render.MarkerRenderer

import org.bukkit.scheduler.BukkitRunnable

/**
 * debug renderer controller
 *
 * debug rendering 全体の管理を担当
 *
 * 役割:
 * - scheduler 管理
 * - player loop
 * - marker loop
 * - render distance 判定
 * - renderer 呼び出し
 */
object DebugRenderer {

    /**
     * debug renderer 開始
     */
    fun start() {

        object : BukkitRunnable() {

            override fun run() {

                /**
                 * online players loop
                 */
                StpmBukkitRuntime.plugin.server.onlinePlayers

                    .forEach { player ->

                        /**
                         * debug 無効時
                         */
                        if (!Debug.enabled(player)) {
                            return@forEach
                        }

                        /**
                         * world marker 一覧取得
                         */
                        val markers =
                            StpmBukkitRuntime.markerService
                                .getMarkers(player.world)

                        /**
                         * marker loop
                         */
                        markers.forEach { marker ->

                            val distanceSquared =
                                marker.location.distanceSquared(
                                    player.location
                                )

                            /**
                             * render distance 超過
                             */
                            if (
                                distanceSquared >
                                Cfg.renderMaxDistance *
                                Cfg.renderMaxDistance
                            ) {
                                return@forEach
                            }

                            /**
                             * marker render
                             */
                            MarkerRenderer.render(
                                player,
                                marker.location,
                                distanceSquared
                            )
                        }
                    }
            }

        }.runTaskTimer(
            StpmBukkitRuntime.plugin,
            0L,
            Cfg.renderInterval
        )
    }
}
