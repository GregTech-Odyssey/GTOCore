package com.gto.gtocore.common.block;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Nullable;

@Getter
public class WirelessEnergyUnitBlock extends Block {

    private final BigInteger capacity;
    private final int loss;
    private final int tier;

    public WirelessEnergyUnitBlock(Properties properties, int tier) {
        super(properties);
        this.capacity = BigInteger.valueOf(GTValues.VEX[(tier << 1)]).add(BigInteger.valueOf(262144L * tier)).multiply(BigInteger.valueOf(tier));
        this.loss = (GTValues.MAX - tier) << 3;
        this.tier = tier;
    }

    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (GTUtil.isShiftDown()) {
            tooltip.add(Component.translatable("gtceu.multiblock.power_substation.capacity", FormattingUtil.formatNumbers(capacity)));
            tooltip.add(Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", (double) loss / 10));
        } else {
            tooltip.add(Component.translatable("tooltip.ad_astra.shift_description"));
        }
    }
}
