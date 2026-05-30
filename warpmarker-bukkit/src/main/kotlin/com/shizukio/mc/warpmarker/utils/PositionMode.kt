package com.shizukio.mc.warpmarker.utils

import org.bukkit.Location
import kotlin.math.floor

/**
 * marker 座標補正モード
 *
 * create 時の:
 * - 座標丸め
 * - 中央補正
 *
 * を制御する
 */
enum class PositionMode {

    /**
     * 入力座標そのまま
     *
     * 小数維持
     */
    RAW,

    /**
     * block 座標へ丸め
     *
     * 例:
     * 10.8 -> 10.0
     */
    BLOCK,

    /**
     * block 中央へ補正
     *
     * x,z:
     * +0.5
     *
     * y:
     * block 基準
     */
    CENTER,

    /**
     * 完全 block center
     *
     * x,y,z:
     * +0.5
     */
    BLOCK_CENTER;

    /**
     * location transform
     *
     * mode に応じて
     * location を補正
     */
    fun transform(
        location: Location
    ): Location {

        val world =
            location.world

        return when (this) {

            /**
             * 入力そのまま
             */
            RAW -> {

                location.clone()
            }

            /**
             * block 座標化
             */
            BLOCK -> {

                Location(
                    world,

                    floor(location.x),
                    floor(location.y),
                    floor(location.z),

                    location.yaw,
                    location.pitch
                )
            }

            /**
             * x,z center
             */
            CENTER -> {

                Location(
                    world,

                    floor(location.x) + 0.5,

                    floor(location.y).toDouble(),

                    floor(location.z) + 0.5,

                    location.yaw,
                    location.pitch
                )
            }

            /**
             * 完全 center
             */
            BLOCK_CENTER -> {

                Location(
                    world,

                    floor(location.x) + 0.5,

                    floor(location.y) + 0.5,

                    floor(location.z) + 0.5,

                    location.yaw,
                    location.pitch
                )
            }
        }
    }
}
