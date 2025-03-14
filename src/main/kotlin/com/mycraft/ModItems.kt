package com.mycraft

import com.mycraft.MyCraft.MOD_ID
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FuelRegistryEvents
import net.minecraft.component.type.ConsumableComponents
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.*
import net.minecraft.item.consume.ApplyEffectsConsumeEffect
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


class ModItems {
    companion object {
        fun register(name: String?, itemFactory: (settings: Item.Settings?) -> Item, settings: Item.Settings): Item {
            // Create the item key.
            val itemKey: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MyCraft.MOD_ID, name))

            // Create the item instance.
            val item: Item = itemFactory(settings.registryKey(itemKey))

            // Register the item.
            Registry.register(Registries.ITEM, itemKey, item)

            return item
        }

        fun createSuspiciousSubstance(): Item {
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
            val suspiciousSubstance: Item = register("suspicious_substance", { settings: Item.Settings? -> ItemWithTooltip(settings) }, itemSettings)
            // Will compost at a rate of 0.3
            CompostingChanceRegistry.INSTANCE.add(suspiciousSubstance, 0.3f);

            // Can be burned in a furnace or cauldron
            FuelRegistryEvents.BUILD.register(FuelRegistryEvents.BuildCallback { builder: FuelRegistry.Builder, context: FuelRegistryEvents.Context? ->
                builder.add(suspiciousSubstance, 5 * 20)
            })

            // Allows the item to be used as ingredients for a recipe
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(suspiciousSubstance) }

            return suspiciousSubstance;
        }

        fun createSword(): Item {
            val guiditeToolMaterial: ToolMaterial = ToolMaterial(
                BlockTags.INCORRECT_FOR_WOODEN_TOOL,
                455,
                5.0f,
                1.5f,
                22,
                TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "sausage_repair_items"))
            )

            val guiditeSword: Item = register(
                "guidite_sword",
                { settings -> SwordItem(guiditeToolMaterial, 1f, 1f, settings) },
                Item.Settings()
            )

            ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register{ itemGroup: FabricItemGroupEntries -> itemGroup.add(guiditeSword) }
            return guiditeSword
        }

    }
}