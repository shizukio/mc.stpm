package com.shizukio.mc.stpm.commands.subcommands

import com.shizukio.mc.stpm.commands.suggestions.Marker
import com.shizukio.mc.stpm.StpmBukkitRuntime
import com.shizukio.mc.stpm.types.TeleportMode
import com.shizukio.mc.stpm.utils.Debug
import com.shizukio.mc.stpm.utils.SimpleLog

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor

import org.bukkit.entity.Player

/**
 * /stpm tp
 *
 * marker teleport command
 */
object Tp {

    /**
     * command build
     */
    fun build(): CommandAPICommand {

        return CommandAPICommand("tp")

            /**
             * teleport targets
             */
            .withArguments(

                EntitySelectorArgument.ManyPlayers(
                    "targets"
                )
            )

            /**
             * marker id
             */
            .withArguments(

                StringArgument("id")

                    .replaceSuggestions(
                        Marker.ids
                    )
            )

            /**
             * teleport mode
             */
            .withOptionalArguments(

                MultiLiteralArgument(
                    "mode",
                    "safe",
                    "exact"
                )
            )

            .executes(
                CommandExecutor { sender, args ->

                    /**
                     * target players
                     */
                    val targets =
                        args["targets"]
                                as Collection<Player>

                    /**
                     * marker id
                     */
                    val id =
                        args["id"] as String

                    /**
                     * teleport mode
                     */
                    val mode =
                        when (
                            args["mode"] as? String
                        ) {

                            "safe" ->
                                TeleportMode.SAFE

                            else ->
                                TeleportMode.EXACT
                        }

                    /**
                     * command base world
                     */
                    val world =
                        when (sender) {

                            is Player ->
                                sender.world

                            else ->
                                targets.firstOrNull()?.world
                        }

                            ?: return@CommandExecutor

                    /**
                     * marker search
                     */
                    val marker =
                        StpmBukkitRuntime.markerService.find(
                            world,
                            id
                        )

                    /**
                     * marker not found
                     */
                    if (marker == null) {

                        SimpleLog.send(
                            sender,
                            "Marker not found: $id"
                        )

                        return@CommandExecutor
                    }

                    /**
                     * teleport loop
                     */
                    targets.forEach { targetPlayer ->

                        val target =
                            marker.location.clone()

                        /**
                         * safe teleport
                         */
                        when (mode) {

                            TeleportMode.SAFE -> {

                                /**
                                 * player rotation 保持
                                 */
                                target.yaw =
                                    targetPlayer.location.yaw

                                target.pitch =
                                    targetPlayer.location.pitch

                                /**
                                 * suffocation 防止
                                 */
                                target.add(
                                    0.0,
                                    1.0,
                                    0.0
                                )
                            }

                            TeleportMode.EXACT -> {}
                        }

                        /**
                         * teleport execute
                         */
                        targetPlayer.teleport(
                            target
                        )

                        Debug.send(
                            sender,
                            SimpleLog.build(
                                "Teleported ${targetPlayer.name} to $id"
                            )
                        )
                    }

                    /**
                     * summary log
                     */
                    Debug.send(
                        sender,
                        SimpleLog.build(
                            "Teleported ${targets.size} player(s)"
                        )
                    )
                }
            )
    }
}
