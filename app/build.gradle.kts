import java.io.FileReader
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.sonarqube") version "4.4.1.3373"
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("com.ncorti.ktfmt.gradle") version "0.16.0"
    id("jacoco")

}

sonar {

    properties {
        property("sonar.projectKey", "swent-group10_polyfit")
        property("sonar.organization", "swent-group10")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )
        //add other useful properties


        property("sonar.qualitygate.wait", "true")


    }
}
android {
    namespace = "com.github.se.polyfit"
    compileSdk = 34
    buildFeatures.buildConfig = true // to enable custom build config keys


    defaultConfig {
        applicationId = "com.github.se.polyfit"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(FileReader(project.rootProject.file("local.properties")))
        buildConfigField("String", "X_RapidAPI_Key", "\"${properties["X_RapidAPI_Key"]}\"")
        buildConfigField("String", "X_RapidAPI_Host", "\"${properties["X_RapidAPI_Host"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )

        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/*.md"
            excludes += "**/libmockkjvmtiagent.so"
            excludes += "**/mockito-extensions/org.mockito.plugins.MockMaker"
        }
    }
    testOptions {
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }


    dependencies {
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        androidTestImplementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
        implementation("androidx.activity:activity-compose:1.8.2")
        implementation(platform("androidx.compose:compose-bom:2024.04.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.navigation:navigation-compose:2.7.7")
        implementation("com.google.android.play:core-ktx:1.8.1")
        implementation("org.json:json:20210307")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        implementation("com.google.dagger:hilt-android:2.51")
        implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("androidx.compose.material3:material3:1.2.1")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")

        // Firebase dependencies
        implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-database-ktx:20.3.1")
        implementation("com.google.firebase:firebase-firestore:24.11.0")
        implementation("com.google.android.play:core-ktx:1.8.1")
        implementation("com.firebaseui:firebase-ui-auth:8.0.2")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-database")
        implementation("com.google.android.play:core-ktx:1.8.1")
        implementation("com.google.firebase:firebase-auth-ktx:22.3.1")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        implementation("org.json:json:20210307")
        implementation("com.google.code.gson:gson:2.10.1")

        androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
        androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

        androidTestImplementation("io.mockk:mockk:1.13.10")
        androidTestImplementation("io.mockk:mockk-android:1.13.10")
        androidTestImplementation("io.mockk:mockk-agent:1.13.10")

        androidTestImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test:runner:1.5.2")
        androidTestImplementation("androidx.test:rules:1.5.0")

        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.5")
        debugImplementation("androidx.compose.ui:ui-tooling:1.6.5")
        debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.5")
        androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
        androidTestImplementation("org.jetbrains.kotlin:kotlin-test:1.9.0")
        androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.0")
        // For the tests
        androidTestImplementation("com.kaspersky.android-components:kaspresso:1.4.3")
        androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.4.3")
        androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:1.4.1")
        testImplementation("org.mockito:mockito-inline:4.2.0")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.0")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.0")
        // Hilts
        implementation("com.google.dagger:hilt-android:2.51")
        annotationProcessor("com.google.dagger:hilt-compiler:2.51")
        // Debug dependencies
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
        // Kapt dependencies
        kapt("com.google.dagger:hilt-android-compiler:2.51")
        kapt("com.google.dagger:hilt-compiler:2.51")
        kaptAndroidTest("com.google.dagger:hilt-compiler:2.51")
        kaptTest("com.google.dagger:hilt-compiler:2.51")
        // Mockito
        androidTestImplementation("org.mockito:mockito-core:5.11.0")

        testImplementation("io.mockk:mockk:1.13.10")
        androidTestImplementation("io.mockk:mockk:1.13.10")
        androidTestImplementation("io.mockk:mockk-android:1.13.10")
        androidTestImplementation("io.mockk:mockk-agent:1.13.10")
    }

// Allow references to generated code
    kapt {
        correctErrorTypes = true
    }


}
tasks.register("jacocoTestReport", JacocoReport::class) {

    mustRunAfter("testDebugUnitTest", "connectedDebugAndroidTest")

    reports {
        xml.required = true
        html.required = true
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
    )
    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(project.buildDir) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        include("outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec")
    })
}
dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("test"))
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
}
