package com.shizukio.mc.stpm.commands.subcommands

import com.shizukio.mc.stpm.commands.ui.MarkerListUi

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor

/**
 * /stpm list
 *
 * marker list command
 */
object List {

    /**
     * command build
     */
    fun build(): CommandAPICommand {

        return CommandAPICommand("list")

            .executesPlayer(
                PlayerCommandExecutor { player, _ ->

                    MarkerListUi.send(
                        player
                    )
                }
            )
    }
}
