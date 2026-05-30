package com.shizukio.mc.warpmarker.commands.subcommands

import com.shizukio.mc.warpmarker.commands.suggestions.Marker
import com.shizukio.mc.warpmarker.WarpMarkerBukkitRuntime
import com.shizukio.mc.warpmarker.utils.SimpleLog

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor

/**
 * /warpmarker remove
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
                        WarpMarkerBukkitRuntime.markerService.remove(
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
