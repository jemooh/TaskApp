Task Management Application


----------------------------------
- Mobile application to help users manage daily todo tasks. 


## Requirements
Ensure you have your environment set up. Use this checklist to verify.

* JDK Version 1.7 & above
* [Android SDK.](http://developer.android.com/sdk/index.html)
* Android SDK Tools
* Android SDK Build tools 33.0.0
* Android Support Repository
* Android Support library


Contribution guidelines.
--------------------------------

[Project Contribution guidelines](Project Guidelines.md)

### How it's built

* Technologies used
    * [Kotlin](https://kotlinlang.org/)
    * [XML Layout](https://developer.android.com/develop/ui/views/layout/declaring-layout)
    * [Jetpack Compose](https://developer.android.com/jetpack/compose)
    * [Jetpack Compose Material 3](https://developer.android.com/jetpack/androidx/releases/compose-material3)
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    * [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
    * [KOIN Dependency](https://insert-koin.io/)
    * [Retrofit](https://square.github.io/retrofit/)
        * [Jetpack](https://developer.android.com/jetpack)
            * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
            * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Timber](https://github.com/JakeWharton/timber)

* Architecture
    * MVVM - Model View ViewModel

* Tests
    * [JUnit5](https://junit.org/junit5/)
    * [Spek](https://www.spekframework.org/)
    * [MockK](https://github.com/mockk/mockk)
    * [Turbine](https://github.com/cashapp/turbine)

* Gradle
    * [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
    * Plugins
        * [Spotless](https://github.com/diffplug/spotless)
        * [Dokka](https://github.com/Kotlin/dokka)
        * [jacoco](https://github.com/jacoco/jacoco)
        * [Ktlint](https://github.com/JLLeitschuh/ktlint-gradle)
        * [Detekt](https://github.com/detekt/detekt)