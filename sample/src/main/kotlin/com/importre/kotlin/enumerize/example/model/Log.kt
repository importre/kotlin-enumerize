package com.importre.kotlin.enumerize.example.model

import com.importre.kotlin.enumerize.EnumExt

data class Log(
    @EnumExt
    val level: Level = Level.DEBUG
) {
    enum class Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}
