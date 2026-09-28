package top.fifthlight.touchcontroller.fabric.v1_21_11

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.player.ClientPlayerBlockBreakEvents
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.common.event.window.WindowEvents
import top.fifthlight.touchcontroller.common.platform.provider.PlatformProvider
import top.fifthlight.touchcontroller.proxy.message.VibrateMessage

class TouchController : ClientModInitializer {

    private val logger = LoggerFactory.getLogger(TouchController::class.java)

    override fun onInitializeClient() {
        logger.info("TouchController Vibration Edition loading...")

        ClientLifecycleEvents.CLIENT_STARTED.register {
            runCatching {
                PlatformProvider.loadNative()
                WindowEvents.loadPlatformWindow()
                logger.info("TouchController vibration platform initialized")
            }.onFailure { error ->
                logger.warn(
                    "TouchController vibration platform could not be initialized",
                    error
                )
            }
        }

        ClientPlayerBlockBreakEvents.AFTER.register { _, _, _, _ ->
            runCatching {
                PlatformProvider.platform?.sendEvent(
                    VibrateMessage(VibrateMessage.Kind.BLOCK_BROKEN)
                )
            }.onFailure { error ->
                logger.warn(
                    "Failed to send block break vibration",
                    error
                )
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            runCatching {
                PlatformProvider.platform?.close()
            }.onFailure { error ->
                logger.warn(
                    "Failed to close vibration platform",
                    error
                )
            }
        }
    }
}