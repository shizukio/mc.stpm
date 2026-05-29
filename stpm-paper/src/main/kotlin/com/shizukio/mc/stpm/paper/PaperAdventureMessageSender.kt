package com.shizukio.mc.stpm.paper

import com.shizukio.mc.stpm.utils.AdventureMessageSender

import net.kyori.adventure.text.ComponentLike

import org.bukkit.command.CommandSender

/**
 * Paper message sender.
 *
 * Paper API は CommandSender#sendMessage(Component) を持っているため、
 * Adventure Component をそのまま送れます。
 *
 * Component をそのまま送ることで:
 * - ClickEvent.runCommand
 * - ClickEvent.copyToClipboard
 * - HoverEvent.showText
 *
 * などの chat interaction が機能します。
 */
object PaperAdventureMessageSender : AdventureMessageSender {

    override fun send(
        sender: CommandSender,
        message: ComponentLike
    ) {

        sender.sendMessage(
            message.asComponent()
        )
    }
}
