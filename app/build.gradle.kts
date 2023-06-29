@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
   // alias(libs.plugins.ksp)
    alias(libs.plugins.app)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = BuildConstants.PackageName
    compileSdk = BuildConstants.TargetSdk

    defaultConfig {
        applicationId = namespace

        targetSdk = BuildConstants.TargetSdk
        minSdk = BuildConstants.MinSdk

        versionCode = BuildConstants.VersionCode
        versionName = BuildConstants.VersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        getByName("debug") {
            keyAlias = "debugkey"
            keyPassword = "androiddebug"
            storeFile = project.file("./debugkeystore")
            storePassword = "androiddebug"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    api(project(":domain"))
    api(project(":data"))
    api(project(":chat-ui"))

    implementation(libs.bundles.networkBundle)
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