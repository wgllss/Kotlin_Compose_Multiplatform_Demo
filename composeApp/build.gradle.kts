import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("org.openjfx.javafxplugin") version "0.1.0"
    kotlin("plugin.serialization") version "1.9.0" // Kotlin 序列化插件
}

javafx {
    version = "20.0.2"
    modules = listOf("javafx.media", "javafx.graphics", "javafx-swing")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    jvm()

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        outputModuleName.set("composeApp")
//        browser {
//            val rootDirPath = project.rootDir.path
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(rootDirPath)
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //解析html
            implementation("org.jsoup:jsoup:1.15.3")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")//只支持 Android 和 桌面端
            implementation("com.squareup.retrofit2:converter-scalars:2.9.0")//只支持Android 和桌面端
            implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")//只支持Android 和桌面端
            implementation("com.squareup.okhttp3:okhttp:4.9.3")//只支持Android 和桌面端
            implementation("com.squareup.okio:okio:2.10.0")//只支持Android 和桌面端
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")//只支持Android 和桌面端
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("io.coil-kt.coil3:coil-compose:3.0.3") // 核心Compose支持 支持  Android、iOS、JVM（桌面端）、JavaScript 和 WASM 平台
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.3")//支持  Android、iOS、JVM（桌面端）、JavaScript 和 WASM 平台
            implementation("com.russhwolf:multiplatform-settings:1.2.0") // 跨平台版本KV存储 支持 Android/iOS/Desktop/JS/Native
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            //桌面程序中使用ViewModel
            implementation("moe.tlaster:precompose:1.6.2") // 必须≥1.6.0
            implementation("moe.tlaster:precompose-viewmodel:1.6.2") // 新增子模块

            //解析html
            implementation("org.jsoup:jsoup:1.15.3")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")//只支持 Android 和 桌面端
            implementation("com.squareup.retrofit2:converter-scalars:2.9.0")//只支持Android 和桌面端
            implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")//只支持Android 和桌面端
            implementation("com.squareup.okhttp3:okhttp:4.9.3")//只支持Android 和桌面端
            implementation("com.squareup.okio:okio:2.10.0")//只支持Android 和桌面端
            implementation("com.squareup.retrofit2:converter-gson:2.9.0")//只支持Android 和桌面端

            val javafxPlatform = System.getProperty("os.name").lowercase().let {
                when {
                    it.contains("linux") -> "linux"
                    it.contains("mac") -> "mac"
                    else -> "win"
                }
            }

            implementation("org.openjfx:javafx-base:20.0.2:$javafxPlatform") {
                exclude(group = "org.openjfx", module = "javafx-base")
            }  // 必须的基础模块:ml-citation{ref="7" data="citationList"}
            implementation("org.openjfx:javafx-media:20.0.2:$javafxPlatform") {
                exclude(group = "org.openjfx", module = "javafx-media")
            }
            implementation("org.openjfx:javafx-graphics:20.0.2:$javafxPlatform") {
                exclude(group = "org.openjfx", module = "javafx-graphics")
            }
            implementation("org.openjfx:javafx-swing:20.0.2:$javafxPlatform") {
                exclude(group = "org.openjfx", module = "javafx-swing")
            }

            // Kotlinx.Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            // SQLite JDBC 驱动
            implementation("org.xerial:sqlite-jdbc:3.42.0.0")
        }
    }
}

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { properties.load(it) }
}

val javaHomePath = properties.getProperty("jdkDirPath") ?: ""

android {
    namespace = "com.wx.compose.multiplatform"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.wx.compose.multiplatform"
        minSdk = libs.versions.android.minSdk.get().toInt()
        //noinspection OldTargetApi
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}
compose.desktop {
    application {
        mainClass = "com.wx.compose.multiplatform.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            includeAllModules = true // 包含所有依赖
            jvmArgs += listOf("-Dfile.encoding=UTF-8,--add-opens=java.base/java.lang=ALL-UNNAMED")
            // 指定捆绑的 JRE 版本和路径
            javaHome = javaHomePath//配置需要的 jdk 11环境变量 比如: "C:\\Users\\XXX\\.jdks\\openjdk-21.0.1",注意目录路径里面最好不要有中文
            packageName = "multiplatform_compose_demo"
            packageVersion = "1.0.0"
            // 启用桌面快捷方式（Windows/Linux）
            windows {
                iconFile.set(project.file("src/jvmMain/composeResources/drawable/qwer.ico"))
                shortcut = true//安装完带快捷方式
                menu = true//安装后 开始，程序 菜单栏里面有快捷方式
            }
        }

        buildTypes.release.proguard {
            isEnabled = false // 禁用混淆
        }
    }
}
