package com.mycraft

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FuelRegistryEvents
import net.minecraft.component.type.ConsumableComponents
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.FuelRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.consume.ApplyEffectsConsumeEffect
import org.slf4j.LoggerFactory

object MyCraft : ModInitializer {
    const val MOD_ID = "mycraft"
    private val logger = LoggerFactory.getLogger("mycraft")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")


        val itemSettings = Item.Settings();

        val statusEffect = StatusEffectInstance(StatusEffects.LEVITATION, 6 * 20, 1);

        val consumableComponent = ConsumableComponents.food()
        consumableComponent.consumeEffect(ApplyEffectsConsumeEffect(statusEffect, 1.0f))

        // Make edible
        val foodComponent = FoodComponent.Builder();
        foodComponent.alwaysEdible()
        foodComponent.nutrition(1);
        itemSettings.food(foodComponent.build(), consumableComponent.build())

        /* Create item with tooltip */
        val suspiciousSubstance: Item = ModItems.register("suspicious_substance", { settings: Item.Settings? -> ItemWithTooltip(settings) }, itemSettings)
        // Will compost at a rate of 0.3
        CompostingChanceRegistry.INSTANCE.add(suspiciousSubstance, 0.3f);

        // Can be burned in a furnace or cauldron
        FuelRegistryEvents.BUILD.register(FuelRegistryEvents.BuildCallback { builder: FuelRegistry.Builder, context: FuelRegistryEvents.Context? ->
            builder.add(suspiciousSubstance, 5 * 20)
        })

        // Allows the item to be used as ingredients for a recipe
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
            .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(suspiciousSubstance) }

    }
}