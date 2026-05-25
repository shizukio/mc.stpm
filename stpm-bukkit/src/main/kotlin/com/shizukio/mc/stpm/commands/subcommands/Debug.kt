package com.shizukio.mc.stpm.commands.subcommands

import com.shizukio.mc.stpm.utils.Debug

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor

/**
 * /stpm debug
 *
 * debug mode toggle command
 */
object Debug {

    /**
     * command build
     */
    fun build(): CommandAPICommand {

        return CommandAPICommand("debug")

            .withArguments(
                BooleanArgument("enabled")
            )

            .executesPlayer(
                PlayerCommandExecutor { player, args ->

                    val enabled =
                        args["enabled"] as Boolean

                    /**
                     * debug state 更新
                     */
                    Debug.set(
                        player,
                        enabled
                    )

                    player.sendMessage(
                        "debug=$enabled"
                    )
                }
            )
    }
}
