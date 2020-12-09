package dependencies

object Application {
    const val applicationId = "com.radiostudies.main.ui"
}

object Versions {
    const val compileSdkVersion = 29
    const val buildToolsVersion = "30.0.1"

    const val minSdkVersion = 23
    const val targetSdkVersion = 29

    const val versionCode = 1
    const val versionName = "1.0"

    const val gradle = "4.1.1"

    //Android
    const val kotlin_version = "1.3.72"
    const val core_ktx = "1.3.1"
    const val appcompat = "1.1.0"
    const val constraint_layout = "1.1.3"
    const val navigation = "2.3.0"
    const val material = "1.2.0-alpha03"
    const val gson = "2.8.6"
    const val legacy_support = "1.0.0"
    const val lifecycle = "2.2.0"
    const val room = "2.2.3"

    //Google
    const val dagger = "2.25.3"

    //Test
    const val junit = "4.12"
    const val ext_junit = "1.1.1"
    const val espresso_core = "3.2.0"
}

object Libs {
    private object Gradle {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}"
    }

    private object Android {
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
        const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
        const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacy_support}"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
        const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

        // Navigation
        const val navigationPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
        const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"

        const val materialDesign = "com.google.android.material:material:${Versions.material}"

        const val gson = "com.google.code.gson:gson:${Versions.gson}"
    }

    private object Dagger {
        const val core = "com.google.dagger:dagger:${Versions.dagger}"
        const val android = "com.google.dagger:dagger-android:${Versions.dagger}"
        const val androidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        const val processor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
        const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    private object Room {
        const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"
        const val roomGuava = "androidx.room:room-guava:${Versions.room}"
        const val roomTesting = "androidx.room:room-testing:${Versions.room}"
    }

    private object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val ext_junit = "androidx.test.ext:junit:${Versions.ext_junit}"
        const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    }
}
