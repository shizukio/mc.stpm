package com.shizukio.mc.stpm.commands.subcommands

import com.shizukio.mc.stpm.commands.suggestions.Marker
import com.shizukio.mc.stpm.StpmBukkitRuntime
import com.shizukio.mc.stpm.utils.SimpleLog

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor

/**
 * /stpm remove
 *
 * marker 削除 command
 */
object Remove {

    fun build(): CommandAPICommand {

        return CommandAPICommand("remove")

            .withArguments(

                StringArgument("id")

                    .replaceSuggestions(
                        Marker.ids
                    )
            )

            .executesPlayer(
                PlayerCommandExecutor { player, args ->

                    val id =
                        args["id"] as String

                    val removed =
                        StpmBukkitRuntime.markerService.remove(
                            player.world,
                            id
                        )

                    if (!removed) {

                        SimpleLog.send(
                            player,
                            "Marker not found: $id"
                        )

                        return@PlayerCommandExecutor
                    }

                    SimpleLog.send(
                        player,
                        "Removed marker: $id"
                    )
                }
            )
    }
}
