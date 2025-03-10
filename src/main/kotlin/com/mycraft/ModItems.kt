package com.mycraft

import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
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
    }
}