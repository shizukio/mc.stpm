package com.shizukio.mc.warpmarker.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Debug {

    fun enabled(
        sender: CommandSender
    ): Boolean {

        if (sender !is Player) {
            return true
        }

        return PlayerStates
            .get(sender)
            .debug
    }

    fun set(
        player: Player,
        enabled: Boolean
    ) {

        PlayerStates
            .get(player)
            .debug = enabled
    }

    fun send(
        sender: CommandSender,
        message: ComponentLike
    ) {

        if (!enabled(sender)) {
            return
        }

        val prefix =
            Component.text(
                "[DEBUG]",
                NamedTextColor.DARK_GRAY
            )

        AdventureMessages.send(
            sender,

            prefix.append(
                Component.text(" ")
            )

                .append(
                    message.asComponent()
                )
        )
    }
}
