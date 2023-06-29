@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = BuildConstants.Domain.Namespace
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

    api(project(":common"))

    //implementation (libs.itextpdf)
    implementation(libs.bundles.networkBundle)

    implementation(libs.roomRuntime)
    kapt(libs.roomCompiler)

    implementation(libs.bundles.coreBundle)
    //endregion compose


    testImplementation(libs.bundles.testBundle)
    androidTestImplementation(libs.bundles.androidTestBundle)
}