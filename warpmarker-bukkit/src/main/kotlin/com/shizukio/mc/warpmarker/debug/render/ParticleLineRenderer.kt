package com.shizukio.mc.warpmarker.debug.render

import com.shizukio.mc.warpmarker.Cfg

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

/**
 * particle line renderer
 *
 * 2点間へ
 * particle line を描画
 */
object ParticleLineRenderer {

    /**
     * line render
     */
    fun drawLine(
        player: Player,
        start: Location,
        end: Location
    ) {

        val distance =
            start.distance(end)

        /**
         * particle 間隔
         *
         * 小さいほど綺麗
         * ただし重くなる
         */
        val step =
            0.08

        val points =
            (distance / step).toInt()

        /**
         * dust particle option
         */
        val dust =
            Particle.DustOptions(
                Cfg.renderColor,
                0.2f
            )

        /**
         * line interpolation
         */
        for (i in 0..points) {

            val t =
                i.toDouble() / points.toDouble()

            val x =
                lerp(start.x, end.x, t)

            val y =
                lerp(start.y, end.y, t)

            val z =
                lerp(start.z, end.z, t)

            player.spawnParticle(
                Particle.DUST,

                x,
                y,
                z,

                1,
                dust
            )
        }
    }

    /**
     * linear interpolation
     *
     * start → end を
     * t割合で補間
     */
    private fun lerp(
        start: Double,
        end: Double,
        t: Double
    ): Double {

        return start + (
                (end - start) * t
                )
    }
}
