package com.importre.kotlin.enumerize.example;

import com.importre.kotlin.enumerize.example.model.LogUtils;
import com.importre.kotlin.enumerize.example.model.Log;

import static java.lang.System.out;

public class JMain {

    public static void main(String[] args) {
        Log log = new Log("error");
        out.println(log);                        // Log(level=error)
        out.println(LogUtils.getEnumLevel(log)); // ERROR
        out.println(LogUtils.isVerbose(log));    // false
        out.println(LogUtils.isDebug(log));      // false
        out.println(LogUtils.isInfo(log));       // false
        out.println(LogUtils.isWran(log));       // false
        out.println(LogUtils.isError(log));      // true
    }
}
