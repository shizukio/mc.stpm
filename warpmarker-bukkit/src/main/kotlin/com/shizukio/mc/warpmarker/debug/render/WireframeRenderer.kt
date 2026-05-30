package com.shizukio.mc.warpmarker.debug.render

import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * cube wireframe renderer
 *
 * marker 周囲へ
 * wireframe cube を描画
 */
object WireframeRenderer {

    /**
     * wireframe render
     */
    fun render(
        player: Player,
        location: Location
    ) {

        /**
         * ブロック中央
         */
        val center =
            location.clone().add(
                0.0,
                0.5,
                0.0
            )

        val minX = center.x - 0.5
        val minY = center.y - 0.5
        val minZ = center.z - 0.5

        val maxX = center.x + 0.5
        val maxY = center.y + 0.5
        val maxZ = center.z + 0.5

        /**
         * cube edges
         */
        val edges = listOf(

            // bottom
            arrayOf(minX, minY, minZ, maxX, minY, minZ),
            arrayOf(maxX, minY, minZ, maxX, minY, maxZ),
            arrayOf(maxX, minY, maxZ, minX, minY, maxZ),
            arrayOf(minX, minY, maxZ, minX, minY, minZ),

            // top
            arrayOf(minX, maxY, minZ, maxX, maxY, minZ),
            arrayOf(maxX, maxY, minZ, maxX, maxY, maxZ),
            arrayOf(maxX, maxY, maxZ, minX, maxY, maxZ),
            arrayOf(minX, maxY, maxZ, minX, maxY, minZ),

            // vertical
            arrayOf(minX, minY, minZ, minX, maxY, minZ),
            arrayOf(maxX, minY, minZ, maxX, maxY, minZ),
            arrayOf(maxX, minY, maxZ, maxX, maxY, maxZ),
            arrayOf(minX, minY, maxZ, minX, maxY, maxZ)
        )

        /**
         * edge render
         */
        edges.forEach { edge ->

            ParticleLineRenderer.drawLine(
                player,

                Location(
                    center.world,
                    edge[0],
                    edge[1],
                    edge[2]
                ),

                Location(
                    center.world,
                    edge[3],
                    edge[4],
                    edge[5]
                )
            )
        }
    }
}
