package com.shizukio.mc.stpm.commands

import com.shizukio.mc.stpm.commands.subcommands.Create
import com.shizukio.mc.stpm.commands.subcommands.Debug
import com.shizukio.mc.stpm.commands.subcommands.List
import com.shizukio.mc.stpm.commands.subcommands.Remove
import com.shizukio.mc.stpm.commands.subcommands.Tp

import dev.jorel.commandapi.CommandAPICommand

/**
 * STPM root command
 *
 * /stpm
 *
 * 各 subcommand の登録のみを担当
 *
 * 実際の処理は:
 * subcommands/
 * 以下へ分離
 */
object StpmCommands {

    /**
     * command register
     */
    fun register() {

        CommandAPICommand("stpm")

            /**
             * /stpm debug
             */
            .withSubcommand(
                Debug.build()
            )

            /**
             * /stpm create
             */
            .withSubcommand(
                Create.build()
            )

            /**
             * /stpm remove
             */
            .withSubcommand(
                Remove.build()
            )

            /**
             * /stpm tp
             */
            .withSubcommand(
                Tp.build()
            )

            /**
             * /stpm list
             */
            .withSubcommand(
                List.build()
            )

            .register()
    }
}
