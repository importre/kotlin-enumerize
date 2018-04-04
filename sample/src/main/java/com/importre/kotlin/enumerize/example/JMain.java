package com.importre.kotlin.enumerize.example;

import com.importre.kotlin.enumerize.example.model.Log;
import com.importre.kotlin.enumerize.example.model.LogExt;
import com.importre.kotlin.enumerize.example.model.Trigger;
import com.importre.kotlin.enumerize.example.model.TriggerUtils;

import static java.lang.System.out;

public class JMain {

    public static void main(String[] args) {

        Log log = new Log(Log.Level.ERROR);
        out.println(log);                   // Log(type=ERROR)
        out.println(log.getLevel());        // ERROR
        out.println(LogExt.isVerbose(log)); // false
        out.println(LogExt.isDebug(log));   // false
        out.println(LogExt.isInfo(log));    // false
        out.println(LogExt.isWarn(log));    // false
        out.println(LogExt.isError(log));   // true

        Trigger trigger = new Trigger("normal_message");
        out.println(trigger);                               // Trigger(type=normal_message)
        out.println(TriggerUtils.getEnumType(trigger));     // NORMAL_MESSAGE
        out.println(TriggerUtils.isNone(trigger));          // false
        out.println(TriggerUtils.isNormalMessage(trigger)); // true
        out.println(TriggerUtils.isMainDefault(trigger));   // false
        out.println(TriggerUtils.isWelcomeEvent(trigger));  // false
    }
}
