plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // Adds the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.haircut"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.haircut"
        minSdk = 23
        targetSdk = 34
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
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.database)

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation(libs.slf4j.simple)


    /**
     * Notes: `junit-jupiter` is an aggregate of `junit-jupiter-api`, `junit-jupiter-params` and `junit-jupiter-engine`.
     * So by including this dependency we don't need to include the other ones otherwise it would be redundant.
     * `junit-jupiter-api`: The api that contains assertion methods.
     * junit-jupiter-params: used for making parameterized tests.
     * junit-jupiter-engine`: the engine used to run the junit.
     */
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation(libs.junit.jupiter)



    implementation("com.google.android.gms:play-services-maps:18.1.0") // Ensure this is the latest version
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)

    androidTestImplementation(libs.androidx.espresso.core)
    //Junit 4 cannot be replaced with Junit jupiter in this case because of the Android instrumentation testing is included in 4 only
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)


    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation(libs.slf4j.simple)

}
tasks.withType<Test> {
    useJUnitPlatform()
}
