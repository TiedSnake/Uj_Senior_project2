// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
// FIXME: below code can be replaced with the new version
//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//
//    dependencies {
//        // Add the classpath for Google services here
//        classpath("com.google.gms:google-services:4.4.2")
//    }
//}
//
//allprojects {
//    repositories {
//    }
//}