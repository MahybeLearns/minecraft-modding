package com.mycraft

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ItemWithTooltip(settings: Settings?) : Item(settings) {
    override fun appendTooltip(stack: ItemStack?, context: TooltipContext?, tooltip: MutableList<Text>?, type: TooltipType?) {
        if (tooltip != null) {
            tooltip.add(Text.literal("Sausages").formatted(Formatting.GOLD))
        }
        super.appendTooltip(stack, context, tooltip, type)
    }
}