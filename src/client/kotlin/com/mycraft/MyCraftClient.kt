package com.mycraft

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents

import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import org.slf4j.LoggerFactory

object MyCraftClient : ClientModInitializer {

    private val logger = LoggerFactory.getLogger("mycraft")

	override fun onInitializeClient() {
		logger.info("Hello Client!")
	}
}