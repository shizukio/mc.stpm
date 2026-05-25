package com.shizukio.mc.stpm.utils

import org.bukkit.entity.Player
import com.shizukio.mc.stpm.StpmBukkitRuntime
import com.shizukio.mc.stpm.state.PlayerState

object PlayerStates {

    fun get(
        player: Player
    ): PlayerState {

        return StpmBukkitRuntime.playerStates
            .getOrPut(player.uniqueId) {

                PlayerState()
            }
    }
}
