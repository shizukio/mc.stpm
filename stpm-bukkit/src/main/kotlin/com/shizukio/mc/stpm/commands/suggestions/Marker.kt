package com.shizukio.mc.stpm.commands.suggestions

import com.shizukio.mc.stpm.StpmBukkitRuntime

import dev.jorel.commandapi.SuggestionInfo
import dev.jorel.commandapi.arguments.ArgumentSuggestions

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * marker id suggestion utility
 *
 * command auto complete 用
 */
object Marker {

    /**
     * marker id suggestions
     */
    val ids =
        ArgumentSuggestions.strings<CommandSender> {

                info: SuggestionInfo<CommandSender> ->

            val sender =
                info.sender()

            /**
             * player のみ対応
             */
            if (sender !is Player) {

                return@strings emptyArray()
            }

            /**
             * world marker ids
             */
            StpmBukkitRuntime.markerService
                .getIds(sender.world)
                .toTypedArray()
        }
}
