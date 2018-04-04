@file:JvmName("Main")

package com.importre.kotlin.enumerize.example

import com.importre.kotlin.enumerize.example.model.Log
import com.importre.kotlin.enumerize.example.model.Trigger
import com.importre.kotlin.enumerize.example.model.enumType
import com.importre.kotlin.enumerize.example.model.isDebug
import com.importre.kotlin.enumerize.example.model.isError
import com.importre.kotlin.enumerize.example.model.isInfo
import com.importre.kotlin.enumerize.example.model.isMainDefault
import com.importre.kotlin.enumerize.example.model.isNone
import com.importre.kotlin.enumerize.example.model.isNormalMessage
import com.importre.kotlin.enumerize.example.model.isVerbose
import com.importre.kotlin.enumerize.example.model.isWarn
import com.importre.kotlin.enumerize.example.model.isWelcomeEvent

fun main(args: Array<String>) {

    Log(level = Log.Level.ERROR).let { log ->
        println(log)           // Log(type=ERROR)
        println(log.level)     // ERROR
        println(log.isVerbose) // false
        println(log.isDebug)   // false
        println(log.isInfo)    // false
        println(log.isWarn)    // false
        println(log.isError)   // true
    }

    Trigger(type = "normal_message").let { trigger ->
        println(trigger)                 // Trigger(type=normal_message)
        println(trigger.enumType)        // NORMAL_MESSAGE
        println(trigger.isNone)          // false
        println(trigger.isNormalMessage) // true
        println(trigger.isMainDefault)   // false
        println(trigger.isWelcomeEvent)  // false
    }

    JMain.main(args)
}
