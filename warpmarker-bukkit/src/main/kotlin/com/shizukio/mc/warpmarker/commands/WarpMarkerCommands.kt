package com.shizukio.mc.warpmarker.commands

import com.shizukio.mc.warpmarker.Cfg
import com.shizukio.mc.warpmarker.commands.subcommands.Create
import com.shizukio.mc.warpmarker.commands.subcommands.Debug
import com.shizukio.mc.warpmarker.commands.subcommands.List
import com.shizukio.mc.warpmarker.commands.subcommands.Remove
import com.shizukio.mc.warpmarker.commands.subcommands.Tp

import dev.jorel.commandapi.CommandAPICommand

/**
 * WarpMarker root command
 *
 * /warpmarker
 *
 * 各 subcommand の登録のみを担当
 *
 * 実際の処理は:
 * subcommands/
 * 以下へ分離
 */
object WarpMarkerCommands {

    /**
     * command register
     */
    fun register() {

        val command =
            CommandAPICommand("warpmarker")

        /**
         * /warpmarker 全体の permission.
         *
         * CommandAPI は root command に設定した permission を
         * subcommand へも引き継いでくれます。
         */
        if (Cfg.commandPermissionEnabled) {

            command.withPermission(
                Cfg.commandPermissionNode
            )
        }

        command

            /**
             * /warpmarker debug
             */
            .withSubcommand(
                Debug.build()
            )

            /**
             * /warpmarker create
             */
            .withSubcommand(
                Create.build()
            )

            /**
             * /warpmarker remove
             */
            .withSubcommand(
                Remove.build()
            )

            /**
             * /warpmarker tp
             */
            .withSubcommand(
                Tp.build()
            )

            /**
             * /warpmarker list
             */
            .withSubcommand(
                List.build()
            )

            .register()
    }
}
