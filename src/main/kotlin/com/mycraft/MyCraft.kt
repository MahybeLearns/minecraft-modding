package com.mycraft

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FuelRegistryEvents
import net.minecraft.item.FuelRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import org.slf4j.LoggerFactory


object MyCraft : ModInitializer {
    const val MOD_ID = "mycraft"
    private val logger = LoggerFactory.getLogger("mycraft")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

        val suspiciousSubstance: Item = ModItems.register("suspicious_substance", { settings: Item.Settings? -> ItemWithTooltip(settings) }, Item.Settings())
        CompostingChanceRegistry.INSTANCE.add(suspiciousSubstance, 0.3f);

        FuelRegistryEvents.BUILD.register(FuelRegistryEvents.BuildCallback { builder: FuelRegistry.Builder, context: FuelRegistryEvents.Context? ->
            builder.add(suspiciousSubstance, 5 * 20)
        })

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
            .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(suspiciousSubstance) }
	}
}