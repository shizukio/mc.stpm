package com.shizukio.mc.warpmarker.utils

object Console {

    private const val RESET =
        "\u001B[0m"

    private const val CYAN =
        "\u001B[36m"

    private const val RED =
        "\u001B[31m"

    fun info(
        message: String
    ) {

        println(
            "$CYAN[WarpMarker]$RESET $message"
        )
    }

    fun error(
        message: String
    ) {

        println(
            "$RED[WarpMarker]$RESET $message"
        )
    }
}
