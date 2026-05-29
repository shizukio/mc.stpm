package com.shizukio.mc.stpm

import com.shizukio.mc.stpm.commands.StpmCommands
import com.shizukio.mc.stpm.debug.DebugRenderer
import com.shizukio.mc.stpm.services.MarkerService
import com.shizukio.mc.stpm.state.PlayerState
import com.shizukio.mc.stpm.utils.AdventureMessageSender
import com.shizukio.mc.stpm.utils.AdventureMessages
import com.shizukio.mc.stpm.utils.Console
import com.shizukio.mc.stpm.utils.LegacyAdventureMessageSender

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig

import org.bukkit.plugin.java.JavaPlugin

import java.util.UUID

/**
 * Bukkit-compatible runtime lifecycle.
 *
 * この class は stpm-bukkit module にあります。
 * つまり Bukkit API / CommandAPI に依存してよい層です。
 *
 * stpm-core は loader 非依存の model / enum だけを持ちます。
 * JavaPlugin、CommandAPI、Bukkit scheduler などは core に置きません。
 *
 * Paper は Bukkit 互換なので、この Bukkit 実装を土台にできます。
 * Paper 固有 API が必要になったら stpm-paper 側へ adapter を追加します。
 */
object StpmBukkitRuntime {

    /**
     * 現在有効化されている loader plugin instance.
     *
     * Bukkit/Paper の設定ファイル、scheduler、server などに触るときに使います。
     * Web 開発でいうと「framework から渡される app context」に近い役割です。
     */
    lateinit var plugin: JavaPlugin
        private set

    /**
     * marker 管理 service.
     *
     * marker の:
     * - 作成
     * - 削除
     * - 検索
     * - 一覧取得
     * を担当します。
     */
    lateinit var markerService: MarkerService
        private set

    /**
     * player state cache.
     *
     * プレイヤーごとの:
     * - debug 状態
     * - 今後追加される設定
     * を保持します。
     */
    val playerStates =
        mutableMapOf<UUID, PlayerState>()

    /**
     * plugin load.
     *
     * Bukkit/Paper の onLoad から呼ばれます。
     * CommandAPI は Bukkit の enable より前に初期化する必要があるため、
     * runtime 側でも onLoad/onEnable を分けています。
     */
    fun onLoad(
        loaderPlugin: JavaPlugin,
        messageSender: AdventureMessageSender =
            LegacyAdventureMessageSender
    ) {

        plugin =
            loaderPlugin

        // Bukkit 版は legacy text sender、Paper 版は Component sender を渡します。
        // /stpm list のクリック・hover などは、この sender が Component を保持できるかに依存します。
        AdventureMessages.sender =
            messageSender

        CommandAPI.onLoad(
            CommandAPIBukkitConfig(loaderPlugin)
        )
    }

    /**
     * plugin enable.
     *
     * Bukkit/Paper の onEnable から呼ばれます。
     */
    fun onEnable() {

        /**
         * jar 内の config.yml を plugin data folder へコピーします。
         *
         * 既に config.yml がある場合は上書きしません。
         * 既存サーバーでは新しい項目が自動追記されないため、
         * 必要に応じて手動で config.yml へ追加してください。
         */
        plugin.saveDefaultConfig()

        Cfg.reload()

        markerService =
            MarkerService(plugin)

        CommandAPI.onEnable()

        StpmCommands.register()

        DebugRenderer.start()

        Console.info(
            "Enabled"
        )
    }

    /**
     * plugin disable.
     *
     * Bukkit/Paper の onDisable から呼ばれます。
     */
    fun onDisable() {

        CommandAPI.onDisable()

        Console.info(
            "Disabled"
        )
    }
}
