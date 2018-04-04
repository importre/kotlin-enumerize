# Enumerize

> Generate `enum class` and some extensions in Kotlin

| module              | version         |
| ------------------- | --------------- |
| enumerize           | [ ![Download][enumerize-svg] ][enumerize-version] |
| enumerize-processor | [ ![Download][enumerize-processor-svg] ][enumerize-processor-version] |

## Settings

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

## Usage

### @Enumerize

<img src="https://user-images.githubusercontent.com/1744446/38358906-a0caf17e-3901-11e8-9592-3932b8c5d725.png" width=1066>

Add `@Enumerize` to `String` property with enum constants.  
Then `enum class` and some `extension properties`

- `enum<AnnotatedProperty>`
- `is<EnumConstatnt>`

will be generated.

### @EnumExt

<img src="https://user-images.githubusercontent.com/1744446/38358907-a0fb87bc-3901-11e8-804e-935deb91c5fa.png" width=1066>

Add `@EnumExt` to `Enum` property.
Then some `extension properties`(`is<EnumConstatnt>`) will be generated.

## Examples

```sh
$ # run Main.kt
$ ./gradlew clean run
```
```sh
$ # check generated files
$ tree sample/build/generated/source/kaptKotlin
sample/build/generated/source/kaptKotlin
└── main
    └── com
        └── importre
            └── kotlin
                └── enumerize
                    └── example
                        └── model
                            ├── LogExt.kt
                            └── TriggerType.kt

7 directories, 2 files
```

- Definition
    - [Log.kt]
    - [Trigger.kt]
- Usage:
    - [In Kotlin][Main.kt]
    - [In Java][JMain.java]


## License

[MIT © Jaewe Heo](license)




[Log.kt]: sample/src/main/kotlin/com/importre/kotlin/enumerize/example/model/Log.kt
[Trigger.kt]: sample/src/main/kotlin/com/importre/kotlin/enumerize/example/model/Trigger.kt
[JMain.java]: sample/src/main/java/com/importre/kotlin/enumerize/example/JMain.java
[Main.kt]: sample/src/main/kotlin/com/importre/kotlin/enumerize/example/Main.kt

[enumerize-svg]: https://api.bintray.com/packages/importre/maven/enumerize/images/download.svg
[enumerize-processor-svg]: https://api.bintray.com/packages/importre/maven/enumerize-processor/images/download.svg
[enumerize-version]: https://bintray.com/importre/maven/enumerize/_latestVersion
[enumerize-processor-version]: https://bintray.com/importre/maven/enumerize-processor/_latestVersion
