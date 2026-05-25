package com.shizukio.mc.stpm.commands.subcommands

import com.shizukio.mc.stpm.StpmBukkitRuntime
import com.shizukio.mc.stpm.utils.PositionMode
import com.shizukio.mc.stpm.utils.SimpleLog

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.LocationArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor

import org.bukkit.Location

/**
 * /stpm create
 *
 * marker 作成 command
 */
object Create {

    /**
     * command build
     */
    fun build(): CommandAPICommand {

        return CommandAPICommand("create")

            .withArguments(
                StringArgument("id")
            )

            .withOptionalArguments(

                LocationArgument("location"),

                MultiLiteralArgument(
                    "mode",
                    "raw",
                    "block",
                    "block_center",
                    "center"
                )
            )

            .executesPlayer(
                PlayerCommandExecutor { player, args ->

                    /**
                     * marker id
                     */
                    val id =
                        args["id"] as String

                    /**
                     * raw input location
                     */
                    val rawLocation =
                        args["location"] as? Location
                            ?: player.location

                    /**
                     * position mode
                     */
                    val mode =
                        PositionMode.valueOf(

                            (
                                    args["mode"] as? String
                                        ?: "center"
                                    )

                                .uppercase()
                        )

                    val location =
                        mode.transform(rawLocation)

                    /**
                     * duplicate check
                     */
                    if (
                        StpmBukkitRuntime.markerService.exists(
                            player.world,
                            id
                        )
                    ) {

                        SimpleLog.send(
                            player,
                            "Marker already exists: $id"
                        )

                        return@PlayerCommandExecutor
                    }

                    /**
                     * marker create
                     */
                    StpmBukkitRuntime.markerService.create(
                        location,
                        id
                    )

                    SimpleLog.send(
                        player,
                        "Created marker: $id"
                    )
                }
            )
    }
}
