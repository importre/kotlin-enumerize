package com.importre.kotlin.enumerize.example.model

import com.importre.kotlin.enumerize.Enumerize

data class Log(
    @Enumerize(
        "verbose",
        "debug",
        "info",
        "wran",
        "error"
    )
    val level: String = ""
)
