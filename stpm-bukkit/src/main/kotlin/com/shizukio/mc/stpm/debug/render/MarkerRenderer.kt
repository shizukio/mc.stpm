package com.shizukio.mc.stpm.debug.render

import com.shizukio.mc.stpm.Cfg

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

/**
 * marker renderer
 *
 * marker の:
 * - 簡易描画
 * - 詳細描画
 *
 * の切り替えを担当
 */
object MarkerRenderer {

    /**
     * marker render
     */
    fun render(
        player: Player,
        location: Location,
        distanceSquared: Double
    ) {

        /**
         * 近距離:
         * wireframe render
         */
        if (
            distanceSquared <=
            Cfg.renderDetailDistance *
            Cfg.renderDetailDistance
        ) {

            WireframeRenderer.render(
                player,
                location
            )

            return
        }

        /**
         * 遠距離:
         * simple particle
         */
        renderSimple(
            player,
            location
        )
    }

    /**
     * 遠距離用軽量描画
     */
    private fun renderSimple(
        player: Player,
        location: Location
    ) {

        player.spawnParticle(
            Particle.END_ROD,

            location.clone().add(
                0.0,
                0.5,
                0.0
            ),

            2,
            0.0,
            0.0,
            0.0,
            0.0
        )
    }
}
