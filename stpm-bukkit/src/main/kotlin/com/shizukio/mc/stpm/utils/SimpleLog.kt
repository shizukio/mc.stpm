package com.shizukio.mc.stpm.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.command.CommandSender

object SimpleLog {

    /**
     * String -> Component
     */
    fun build(
        message: String
    ): Component {

        return build(
            Component.text(
                message,
                NamedTextColor.GRAY
            )
        )
    }

    /**
     * Component -> prefixed Component
     */
    fun build(
        message: ComponentLike
    ): Component {

        return Component.text(
            "[STPM]",
            NamedTextColor.DARK_GREEN
        )

            .append(
                Component.text(
                    " ",
                    NamedTextColor.GRAY
                )
            )

            .append(
                message.asComponent()
            )
    }

    /**
     * String send
     */
    fun send(
        sender: CommandSender,
        message: String
    ) {

        AdventureMessages.send(
            sender,
            build(message)
        )
    }

    /**
     * Component send
     */
    fun send(
        sender: CommandSender,
        message: ComponentLike
    ) {

        AdventureMessages.send(
            sender,
            build(message)
        )
    }
}
