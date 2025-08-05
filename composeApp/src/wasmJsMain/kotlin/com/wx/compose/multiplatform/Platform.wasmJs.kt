package com.wx.compose.multiplatform

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"

    override val platformName: String = "Web"
}

actual fun getPlatform(): Platform = WasmPlatform()