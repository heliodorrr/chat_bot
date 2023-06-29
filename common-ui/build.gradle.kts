@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = BuildConstants.Common.Namespace
    compileSdk = BuildConstants.TargetSdk

    defaultConfig {
        minSdk = BuildConstants.MinSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = BuildConstants.JAVA_VERSION
        targetCompatibility = BuildConstants.JAVA_VERSION
    }
    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=com.aallam.openai.api.BetaOpenAI"
        jvmTarget = BuildConstants.JAVA_VERSION.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    api(project(":common"))


    implementation(platform(libs.composeBom))

    //region compose
    implementation(libs.bundles.composeBundle)
    implementation(libs.bundles.coreBundle)
    debugImplementation(libs.bundles.composeDebugBundle)
    //endregion compose

    //region di
    kapt(libs.dagger2Compiler)
    implementation(libs.dagger2)
    //endregion di

    testImplementation(libs.bundles.testBundle)
    androidTestImplementation(libs.bundles.androidTestBundle)

    androidTestImplementation(platform(libs.composeBom))
}