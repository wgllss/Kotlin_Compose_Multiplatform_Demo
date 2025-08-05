package com.wx.compose.multiplatform

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"

    override val platformName: String = System.getProperty("os.name").lowercase()

    override val downloadPath: String = JvmConfig.defaultDownloadDir
}

actual fun getPlatform(): Platform = JVMPlatform()