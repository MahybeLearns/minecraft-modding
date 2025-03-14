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
import net.minecraft.item.equipment.ArmorMaterial
import net.minecraft.item.equipment.ArmorMaterials
import net.minecraft.item.equipment.EquipmentAssetKeys
import net.minecraft.item.equipment.EquipmentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import java.util.Map


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
            val suspiciousSubstance: Item =
                register("suspicious_substance", { settings: Item.Settings? -> ItemWithTooltip(settings) }, itemSettings)
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
                TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "guidite_repair_items"))
            )

            val guiditeSword: Item = register(
                "guidite_sword",
                { settings -> SwordItem(guiditeToolMaterial, 1f, 1f, settings) },
                Item.Settings()
            )

            ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(guiditeSword) }
            return guiditeSword
        }

        fun createArmourMaterial(): ArmorMaterial {
            val baseDurability = 15
            val defence = Map.of<EquipmentType, Int>(
                EquipmentType.HELMET, 3,
                EquipmentType.CHESTPLATE, 8,
                EquipmentType.LEGGINGS, 6,
                EquipmentType.BOOTS, 3
            )
            val enchantmentValue = 5
            val toughness = 0.0f
            val knockbackResistance = 0.0f


            val materialKey =
                RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(MOD_ID, "guidite"))

            return ArmorMaterial(
                baseDurability,
                defence,
                enchantmentValue,
                SoundEvents.ITEM_ARMOR_EQUIP_IRON,
                toughness,
                knockbackResistance,
                TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "guidite_repair_items")),
                materialKey
            )
        }

        fun createHelmet(): Item {
            val material = createArmourMaterial()
            val itemSettings = Item.Settings().maxDamage(10000).fireproof()
            val item = register("guidite_helmet", { settings -> ArmorItem(material, EquipmentType.HELMET, settings) }, itemSettings)
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(item) }
            return item;
        }

        fun createChestplate(): Item {
            val material = createArmourMaterial()
            //EquipmentType.CHESTPLATE.getMaxDamage(material.durability)
            val itemSettings = Item.Settings().maxDamage(10000).fireproof()
            val item = register("guidite_chestplate", { settings -> ArmorItem(material, EquipmentType.CHESTPLATE, settings) }, itemSettings)
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(item) }
            return item;
        }

        fun createLeggings(): Item {
            val material = createArmourMaterial()
            val itemSettings = Item.Settings().maxDamage(10000).fireproof()
            val item = register("guidite_leggings", { settings -> ArmorItem(material, EquipmentType.LEGGINGS, settings) }, itemSettings)
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(item) }
            return item;
        }

        fun createBoots(): Item {
            val material = createArmourMaterial()
            val itemSettings = Item.Settings().maxDamage(10000).fireproof();
            val item = register("guidite_boots", { settings -> ArmorItem(material, EquipmentType.BOOTS, settings) }, itemSettings)
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register { itemGroup: FabricItemGroupEntries -> itemGroup.add(item) }
            return item;
        }

    }
}