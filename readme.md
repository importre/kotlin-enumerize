# Enumerize

> Generate `enum class` and some extensions in Kotlin

| module              | version         |
| ------------------- | --------------- |
| enumerize           | [ ![Download][enumerize-svg] ][enumerize-version] |
| enumerize-processor | [ ![Download][enumerize-processor-svg] ][enumerize-processor-version] |

### Settings

```groovy
repositories {
    jcenter()
}
```

```groovy
dependencies {
    implementation "com.importre.kotlin:enumerize:${VERSION_NAME}"
    kapt "com.importre.kotlin:enumerize-processor:${VERSION_NAME}"
}
```

> :warning: It works on Kotlin project only.
> But you can use it in Java.

### Usage

Add `@Enumerize` to `String` property with enum constants.

```kotlin
data class Trigger(
    @Enumerize(
        "none",
        "normal_message",
        "main_default",
        "welcome_event"
    )
    val type: String = ""
)
```

Then `enum class` and some `extension properties`

- `enum<AnnotatedProperty>`
- `is<EnumConstatnt>`

will be generated.

#### [In Kotlin][Main.kt]

```kotlin
Trigger("normal_message").let { trigger ->
    println(trigger)                 // Trigger(type=normal_message)
    println(trigger.enumType)        // NORMAL_MESSAGE
    println(trigger.isNone)          // false
    println(trigger.isNormalMessage) // true
    println(trigger.isMainDefault)   // false
    println(trigger.isWelcomeEvent)  // false
}
```

#### [In Java][JMain.java]

```java
Log log = new Log("error");
out.println(log);                        // Log(level=error)
out.println(LogUtils.getEnumLevel(log)); // ERROR
out.println(LogUtils.isVerbose(log));    // false
out.println(LogUtils.isDebug(log));      // false
out.println(LogUtils.isInfo(log));       // false
out.println(LogUtils.isWran(log));       // false
out.println(LogUtils.isError(log));      // true
```

[JMain.java]: sample/src/main/java/com/importre/kotlin/enumerize/example/JMain.java
[Main.kt]: sample/src/main/kotlin/com/importre/kotlin/enumerize/example/Main.kt

[enumerize-svg]: https://api.bintray.com/packages/importre/maven/enumerize/images/download.svg
[enumerize-processor-svg]: https://api.bintray.com/packages/importre/maven/enumerize-processor/images/download.svg
[enumerize-version]: https://bintray.com/importre/maven/enumerize/_latestVersion
[enumerize-processor-version]: https://bintray.com/importre/maven/enumerize-processor/_latestVersion
