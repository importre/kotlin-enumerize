@file:JvmName("Main")

package com.importre.kotlin.enumerize.example

import com.importre.kotlin.enumerize.example.model.Trigger
import com.importre.kotlin.enumerize.example.model.enumType
import com.importre.kotlin.enumerize.example.model.isMainDefault
import com.importre.kotlin.enumerize.example.model.isNone
import com.importre.kotlin.enumerize.example.model.isNormalMessage
import com.importre.kotlin.enumerize.example.model.isWelcomeEvent

fun main(args: Array<String>) {
    Trigger("normal_message").let { trigger ->
        println(trigger)                 // Trigger(type=normal_message)
        println(trigger.enumType)        // NORMAL_MESSAGE
        println(trigger.isNone)          // false
        println(trigger.isNormalMessage) // true
        println(trigger.isMainDefault)   // false
        println(trigger.isWelcomeEvent)  // false
    }

    JMain.main(args)
}
