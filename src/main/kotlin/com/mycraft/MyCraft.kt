package com.mycraft

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object MyCraft : ModInitializer {
    const val MOD_ID = "mycraft"
    private val logger = LoggerFactory.getLogger("mycraft")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

        ModItems.createSuspiciousSubstance();
        ModItems.createSword()
    }
}