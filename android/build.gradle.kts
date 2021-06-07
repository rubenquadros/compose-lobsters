import java.util.Properties

plugins {
  id("org.jetbrains.compose") version "0.4.0"
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
}

dependencies {
  kapt(libs.dagger.hilt.compiler)
  implementation(projects.api)
  implementation(projects.common)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.paging.compose)
  implementation(libs.dagger.hilt.android)
  implementation(libs.retrofit.moshiConverter)
  implementation(libs.moshix.metadatareflect)
}

android {
  compileSdkVersion(30)
  defaultConfig {
    applicationId = "dev.msfjarvis.claw.android"
    minSdkVersion(23)
    targetSdkVersion(30)
    versionCode = 1
    versionName = "1.0"
  }
  val keystoreConfigFile = rootProject.layout.projectDirectory.file("keystore.properties")
  if (keystoreConfigFile.asFile.exists()) {
    val contents = providers.fileContents(keystoreConfigFile).asText.forUseAtConfigurationTime()
    val keystoreProperties = Properties()
    keystoreProperties.load(contents.get().byteInputStream())
    signingConfigs {
      register("release") {
        keyAlias = keystoreProperties["keyAlias"] as String
        keyPassword = keystoreProperties["keyPassword"] as String
        storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
        storePassword = keystoreProperties["storePassword"] as String
      }
    }
    buildTypes.all { signingConfig = signingConfigs.getByName("release") }
  }
  buildTypes { getByName("release") { isMinifyEnabled = false } }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}
