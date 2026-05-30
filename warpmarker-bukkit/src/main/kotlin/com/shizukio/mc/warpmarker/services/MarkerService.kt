package com.shizukio.mc.warpmarker.services

import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World

import org.bukkit.entity.EntityType
import org.bukkit.entity.Marker

import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

/**
 * WarpMarker marker service
 *
 * marker の:
 * - 作成
 * - 削除
 * - 検索
 * - 一覧取得
 * を担当する service
 *
 * marker entity を直接扱う層
 */
class MarkerService(

    /**
     * plugin instance
     *
     * NamespacedKey 作成などに利用
     */
    private val plugin: JavaPlugin
) {

    // ----------------------------------------------------------------
    // keys / tags
    // ----------------------------------------------------------------

    /**
     * marker id 保存用 PDC key
     *
     * 実際には:
     * warpmarker:id
     *
     * のような namespace 付き key になる
     */
    private val idKey =
        NamespacedKey(
            plugin,
            "id"
        )

    /**
     * plugin 管理 marker 判定用 tag
     *
     * 他 plugin の marker と区別するために使用
     */
    private val markerTag =
        "warpmarker"

    // ----------------------------------------------------------------
    // internal utility
    // ----------------------------------------------------------------

    /**
     * plugin 管理 marker 一覧取得
     *
     * 共通処理を一箇所へ集約
     *
     * 対象:
     * - Marker entity
     * - warpmarker tag 保持 entity
     */
    private fun getManagedMarkers(
        world: World
    ): List<Marker> {

        return world.entities

            // Marker entity のみ対象
            .filterIsInstance<Marker>()

            // WarpMarker 管理 marker のみ対象
            .filter { marker ->

                marker.scoreboardTags.contains(
                    markerTag
                )
            }
    }

    // ----------------------------------------------------------------
    // exists
    // ----------------------------------------------------------------

    /**
     * marker 存在確認
     *
     * true:
     *   既に存在
     *
     * false:
     *   存在しない
     */
    fun exists(
        world: World,
        id: String
    ): Boolean {

        return getManagedMarkers(world)

            .any { marker ->

                getId(marker) == id
            }
    }

    // ----------------------------------------------------------------
    // get ids
    // ----------------------------------------------------------------

    /**
     * marker id 一覧取得
     *
     * 返却例:
     * [
     *   "home",
     *   "spawn",
     *   "shop"
     * ]
     */
    fun getIds(
        world: World
    ): List<String> {

        return getManagedMarkers(world)

            .mapNotNull { marker ->

                getId(marker)
            }
    }

    // ----------------------------------------------------------------
    // get markers
    // ----------------------------------------------------------------

    /**
     * marker entity 一覧取得
     *
     * debug renderer などで利用
     */
    fun getMarkers(
        world: World
    ): List<Marker> {

        return getManagedMarkers(world)
    }

    // ----------------------------------------------------------------
    // find
    // ----------------------------------------------------------------

    /**
     * ID から marker 検索
     *
     * 見つからない場合:
     * null
     */
    fun find(
        world: World,
        id: String
    ): Marker? {

        return getManagedMarkers(world)

            .find { marker ->

                getId(marker) == id
            }
    }

    // ----------------------------------------------------------------
    // create
    // ----------------------------------------------------------------

    /**
     * marker 作成
     *
     * 作成内容:
     * - minecraft:marker spawn
     * - scoreboard tag 追加
     * - PDC id 保存
     *
     * 重複時:
     * IllegalStateException
     */
    fun create(
        location: Location,
        id: String
    ): Marker {

        /**
         * world null 対策
         *
         * 基本発生しないが safety 用
         */
        val world =
            location.world
                ?: error(
                    "world is null"
                )

        /**
         * marker id 重複防止
         */
        if (exists(world, id)) {

            error(
                "marker already exists: $id"
            )
        }

        /**
         * marker entity 作成
         */
        val marker =
            world.spawnEntity(
                location,
                EntityType.MARKER
            ) as Marker

        /**
         * WarpMarker 管理 tag 追加
         *
         * marker 判定高速化用
         */
        marker.addScoreboardTag(
            markerTag
        )

        /**
         * marker id 保存
         *
         * scoreboard tag ではなく
         * PDC を利用
         */
        marker.persistentDataContainer.set(
            idKey,
            PersistentDataType.STRING,
            id
        )

        return marker
    }

    // ----------------------------------------------------------------
    // remove
    // ----------------------------------------------------------------

    /**
     * marker 削除
     *
     * true:
     *   削除成功
     *
     * false:
     *   marker 不存在
     */
    fun remove(
        world: World,
        id: String
    ): Boolean {

        /**
         * marker 検索
         */
        val marker =
            find(world, id)
                ?: return false

        /**
         * entity 削除
         */
        marker.remove()

        return true
    }

    // ----------------------------------------------------------------
    // get id
    // ----------------------------------------------------------------

    /**
     * marker の id 取得
     *
     * PDC 未設定時:
     * null
     */
    fun getId(
        marker: Marker
    ): String? {

        return marker
            .persistentDataContainer
            .get(
                idKey,
                PersistentDataType.STRING
            )
    }
}
