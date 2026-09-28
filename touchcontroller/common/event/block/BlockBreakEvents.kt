package top.fifthlight.touchcontroller.common.event.block

import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.common.platform.provider.PlatformProvider
import top.fifthlight.touchcontroller.proxy.message.VibrateMessage

object BlockBreakEvents {

    private val logger = LoggerFactory.getLogger(BlockBreakEvents::class.java)

    fun afterBlockBreak() {
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
}