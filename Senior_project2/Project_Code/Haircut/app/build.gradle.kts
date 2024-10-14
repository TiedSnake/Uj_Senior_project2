plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.haircut"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.haircut"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //JSON Web Token
    // https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation(libs.java.jwt)



    // JUnit 5 (JUnit Jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)

    // https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher
    testImplementation(libs.junit.platform.launcher)


    androidTestImplementation(libs.androidx.espresso.core)
    //Junit 4 cannot be replaced with Junit jupiter in this case because of the Android instrumentation testing is included in 4 only
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // https://mvnrepository.com/artifact/dnsjava/dnsjava
    implementation(libs.dnsjava)

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation(libs.slf4j.simple)

}
tasks.withType<Test> {
    useJUnitPlatform()
}
