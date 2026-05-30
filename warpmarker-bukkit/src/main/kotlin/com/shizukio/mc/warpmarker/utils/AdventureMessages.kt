package com.shizukio.mc.warpmarker.utils

import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

import org.bukkit.command.CommandSender

/**
 * Adventure Component message sender contract.
 *
 * Bukkit と Paper では「Component をどう送れるか」が違います。
 * その差分をこの interface で吸収します。
 */
interface AdventureMessageSender {

    fun send(
        sender: CommandSender,
        message: ComponentLike
    )
}

/**
 * Bukkit/Spigot fallback sender.
 *
 * Bukkit/Spigot API では Component を直接受け取る sendMessage overload がないため、
 * legacy text へ変換して送ります。
 *
 * 注意:
 * legacy text へ変換すると ClickEvent / HoverEvent / copyToClipboard は失われます。
 * Paper jar では warpmarker-paper 側の sender に差し替えるため、クリック可能 UI が維持されます。
 */
object LegacyAdventureMessageSender : AdventureMessageSender {

    private val serializer =
        LegacyComponentSerializer.legacySection()

    override fun send(
        sender: CommandSender,
        message: ComponentLike
    ) {

        sender.sendMessage(
            serializer.serialize(
                message.asComponent()
            )
        )
    }
}

/**
 * Runtime message sender facade.
 *
 * command や UI 側はこの object だけを使います。
 * 実際の送信方法は loader module が起動時に差し替えます。
 */
object AdventureMessages {

    var sender: AdventureMessageSender =
        LegacyAdventureMessageSender

    fun send(
        receiver: CommandSender,
        message: ComponentLike
    ) {

        sender.send(
            receiver,
            message
        )
    }
}
