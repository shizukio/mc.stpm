package com.shizukio.mc.warpmarker.utils

import org.bukkit.entity.Player
import com.shizukio.mc.warpmarker.WarpMarkerBukkitRuntime
import com.shizukio.mc.warpmarker.state.PlayerState

object PlayerStates {

    fun get(
        player: Player
    ): PlayerState {

        return WarpMarkerBukkitRuntime.playerStates
            .getOrPut(player.uniqueId) {

                PlayerState()
            }
    }
}
