package com.importre.kotlin.enumerize.example.model

import com.importre.kotlin.enumerize.Enumerize

data class Trigger(
    @Enumerize(
        "none",
        "normal_message",
        "main_default",
        "welcome_event"
    )
    val type: String = ""
)
