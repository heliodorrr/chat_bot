@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = BuildConstants.Data.Namespace
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
}

dependencies {
    api(project(":domain"))

    implementation(libs.bundles.networkBundle)

    //region compose
    implementation(libs.bundles.coreBundle)
    //endregion compose

    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    kapt(libs.roomCompiler)

    //region di
    kapt(libs.dagger2Compiler)
    implementation(libs.dagger2)
    //endregion di

    testImplementation(libs.bundles.testBundle)
    androidTestImplementation(libs.bundles.androidTestBundle)
}