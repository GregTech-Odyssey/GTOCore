package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import dev.shadowsoffire.placebo.color.GradientColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public final class TarotArcanum extends ComponentItem {

    private final int itemColor;
    private final int serialNumber;
    private final List<Consumer<List<Component>>> tooltipConsumers;

    private TarotArcanum(Properties properties, int color, int serialNumber,
                         List<Consumer<List<Component>>> tooltipConsumers) {
        super(properties);
        this.itemColor = color;
        this.serialNumber = serialNumber;
        this.tooltipConsumers = tooltipConsumers;
    }

    public static TarotArcanum create(Properties properties, int color, int serialNumber,
                                      List<Consumer<List<Component>>> tooltipConsumers) {
        return new TarotArcanum(properties, color, serialNumber, tooltipConsumers);
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor color() {
        return (itemStack, index) -> {
            if (!(itemStack.getItem() instanceof TarotArcanum tarot) || index >= 1) {
                return -1;
            }
            return tarot.itemColor;
        };
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        return Component.translatable(this.getDescriptionId(pStack)).withStyle(s -> s.withColor(GradientColor.RAINBOW));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 8;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (tooltipConsumers == null || tooltipConsumers.isEmpty()) return;
        int stateIndex = 0;
        if (GTUtil.isAltDown()) stateIndex = 3;
        else if (GTUtil.isCtrlDown()) stateIndex = 2;
        else if (GTUtil.isShiftDown()) stateIndex = 1;
        if (tooltipConsumers.size() <= stateIndex) stateIndex = 0;
        tooltipConsumers.get(stateIndex).accept(tooltip);
        if (stateIndex == 0 && tooltipConsumers.size() > 1) {
            String keys = switch (tooltipConsumers.size()) {
                case 2 -> "SHIFT";
                case 3 -> "SHIFT CTRL";
                case 4 -> "SHIFT CTRL ALT";
                default -> "";
            };
            tooltip.add(Component.literal(I18n.get("tooltip.gtocore.hold_for_more", keys)));
        }
    }
}
