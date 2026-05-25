package com.shizukio.mc.stpm.commands.ui

import com.shizukio.mc.stpm.StpmBukkitRuntime
import com.shizukio.mc.stpm.utils.AdventureMessages
import com.shizukio.mc.stpm.utils.SimpleLog

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

/**
 * marker list ui
 *
 * /stpm list 表示処理
 */
object MarkerListUi {

    /**
     * marker list send
     */
    fun send(
        player: Player
    ) {

        /**
         * marker 一覧取得
         */
        val markers =
            StpmBukkitRuntime.markerService
                .getMarkers(player.world)

        /**
         * marker empty
         */
        if (markers.isEmpty()) {

            SimpleLog.send(
                player,
                Component.text(
                    "No markers",
                    NamedTextColor.DARK_RED
                )
            )

            return
        }

        /**
         * header
         */
        AdventureMessages.send(
            player,
            SimpleLog.build(
                Component.text(
                    "${markers.count()} marker(s) found\n",
                    NamedTextColor.GRAY
                )
            )
        )

        /**
         * marker loop
         */
        markers.forEach { marker ->

            val id =
                StpmBukkitRuntime.markerService
                    .getId(marker)
                    ?: "unknown"

            val location =
                marker.location

            val x = location.blockX
            val y = location.blockY
            val z = location.blockZ

            /**
             * spacing
             */
            val space =
                Component.text(" ")

            /**
             * teleport button
             */
            val tpButton =
                Component.text(
                    "[TP]",
                    NamedTextColor.AQUA
                )

                    .clickEvent(
                        ClickEvent.runCommand(
                            "/stpm tp @s $id"
                        )
                    )

                    .hoverEvent(
                        HoverEvent.showText(

                            Component.text(
                                "Teleport to $id"
                            )
                        )
                    )

            /**
             * remove button
             */
            val removeButton =
                Component.text(
                    "[REMOVE]",
                    NamedTextColor.RED
                )

                    .clickEvent(
                        ClickEvent.runCommand(
                            "/stpm remove $id"
                        )
                    )

                    .hoverEvent(
                        HoverEvent.showText(

                            Component.text(
                                "Remove marker $id"
                            )
                        )
                    )

            /**
             * marker id text
             */
            val markerIdText =
                Component.text(
                    id,
                    NamedTextColor.GRAY
                )

                    .decorate(
                        TextDecoration.UNDERLINED
                    )

                    .clickEvent(
                        ClickEvent.copyToClipboard(id)
                    )

                    .hoverEvent(
                        HoverEvent.showText(

                            Component.text(
                                "Click to copy"
                            )
                        )
                    )

            /**
             * location text
             */
            val markerLocationText =
                Component.text(
                    "($x, $y, $z)",
                    NamedTextColor.AQUA
                )

                    .decorate(
                        TextDecoration.UNDERLINED
                    )

                    .clickEvent(
                        ClickEvent.copyToClipboard(
                            "${location.x} ${location.y} ${location.z}"
                        )
                    )

                    .hoverEvent(
                        HoverEvent.showText(

                            Component.text(
                                "Click to copy location"
                            )
                        )
                    )

            /**
             * final line
             */
            AdventureMessages.send(
                player,

                Component.text(
                    " - ",
                    NamedTextColor.GRAY
                )

                    .append(tpButton)

                    .append(space)

                    .append(removeButton)

                    .append(space)

                    .append(markerIdText)

                    .append(space)

                    .append(markerLocationText)
            )
        }

        AdventureMessages.send(
            player,
            Component.text("\n")
        )
    }
}
