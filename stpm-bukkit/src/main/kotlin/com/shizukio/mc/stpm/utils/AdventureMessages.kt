package com.shizukio.mc.stpm.utils

import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

import org.bukkit.command.CommandSender

/**
 * Adventure Component message sender.
 *
 * Paper では sender.sendMessage(Component) が使えますが、
 * Bukkit/Spigot API では Component を直接受け取る overload がありません。
 *
 * core は Bukkit 系 loader でも動く必要があるため、
 * 送信直前に Bukkit が確実に受け取れる legacy text へ変換します。
 *
 * 注意:
 * legacy text へ変換すると ClickEvent / HoverEvent のような
 * クリック可能 UI は表現できません。
 * 将来 Paper 専用 UI を作る場合は stpm-paper 側で adapter を分けます。
 */
object AdventureMessages {

    private val serializer =
        LegacyComponentSerializer.legacySection()

    fun send(
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
