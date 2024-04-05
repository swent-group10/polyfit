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
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        // General dependencies
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

        // Firebase dependencies
        implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-database-ktx:20.3.1")
        implementation("com.google.firebase:firebase-firestore:24.11.0")
        implementation("com.firebaseui:firebase-ui-auth:8.0.2")
        androidTestImplementation("com.google.firebase:firebase-firestore:24.11.0")

        // Test dependencies
        testImplementation("junit:junit:4.13.2")
        testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.22")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.22")
        testImplementation("io.mockk:mockk:1.13.10")
        testImplementation("com.google.dagger:hilt-android-testing:2.51")

        // Android test dependencies
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
        androidTestImplementation("io.mockk:mockk-android:1.13.10")
        androidTestImplementation("org.mockito:mockito-core:5.11.0")
        androidTestImplementation("org.mockito:mockito-android:4.2.0")
        androidTestImplementation("org.mockito:mockito-inline:4.2.0") // For final class mocking
        androidTestImplementation("com.kaspersky.android-components:kaspresso:1.4.3")
        androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.4.3")
        androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:1.4.1")
        androidTestImplementation("org.robolectric:robolectric:4.7.3")
        androidTestImplementation("androidx.test:runner:1.5.2")

        // Debug dependencies
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")

        // Kapt dependencies
        kapt("com.google.dagger:hilt-android-compiler:2.51")
        kapt("com.google.dagger:hilt-compiler:2.51")
        kaptAndroidTest("com.google.dagger:hilt-compiler:2.51")
        kaptTest("com.google.dagger:hilt-compiler:2.51")
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
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
}
