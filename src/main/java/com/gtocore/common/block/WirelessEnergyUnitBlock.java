package com.gtocore.common.block;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
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
        var sec_base = 60 << 4 - GTOCore.difficulty;
        this.capacity = BigInteger.valueOf(GTValues.VEX[tier << 1]).divide(BigInteger.valueOf(GTOCore.difficulty))
                .add(BigInteger.valueOf(GTValues.VEX[tier] * 20 * sec_base)).multiply(BigInteger.valueOf(tier));
        int loss_tmp = GTOCore.isEasy() ? 0 : (GTValues.MAX - tier) << GTOCore.difficulty;
        if (tier < 6) {
            loss_tmp += 10 * (GTOCore.difficulty << 2) / tier;
        }

        this.loss = loss_tmp;
        this.tier = tier;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (GTUtil.isShiftDown()) {
            tooltip.add(Component.translatable("gtceu.multiblock.power_substation.capacity", FormattingUtil.formatNumbers(capacity)));
            tooltip.add(Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", (double) loss / 10));
        } else {
            tooltip.add(Component.translatable("tooltip.ad_astra.shift_description"));
        }
        tooltip.add(Component.translatable("gtocore.machine.wireless_energy_unit.tooltip"));
    }

    @Nullable
    public static WirelessEnergyUnitBlock get(int tier) {
        if (tier < 1 || tier > BlockMap.WIRELESS_ENERGY_UNIT.length + 1) {
            return null;
        }
        return (WirelessEnergyUnitBlock) BlockMap.WIRELESS_ENERGY_UNIT[tier - 1];
    }

    public record BlockData(@Nullable WirelessEnergyUnitBlock block, BlockPos pos) {}
}
