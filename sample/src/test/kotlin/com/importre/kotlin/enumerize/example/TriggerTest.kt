package com.importre.kotlin.enumerize.example

import com.importre.kotlin.enumerize.example.model.Trigger
import com.importre.kotlin.enumerize.example.model.isMainDefault
import com.importre.kotlin.enumerize.example.model.isNone
import com.importre.kotlin.enumerize.example.model.isNormalMessage
import com.importre.kotlin.enumerize.example.model.isWelcomeEvent
import org.junit.Test
import kotlin.test.assertTrue

class TriggerTest {

    @Test
    fun testDefaultTrigger() {
        val trigger = Trigger()
        assertTrue(trigger.isNone)
    }

    @Test
    fun testNoneTrigger() {
        val trigger = Trigger(type = "none")
        assertTrue(trigger.isNone)
    }

    @Test
    fun testNormalMessageTrigger() {
        val trigger = Trigger(type = "normal_message")
        assertTrue(trigger.isNormalMessage)
    }

    @Test
    fun testMainDefaultTrigger() {
        val trigger = Trigger(type = "main_default")
        assertTrue(trigger.isMainDefault)
    }

    @Test
    fun testWelcomeEventTrigger() {
        val trigger = Trigger(type = "welcome_event")
        assertTrue(trigger.isWelcomeEvent)
    }
}
