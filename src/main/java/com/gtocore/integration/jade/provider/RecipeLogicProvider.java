package com.gtocore.integration.jade.provider;

import com.gtolib.api.machine.feature.DummyEnergyMachine;
import com.gtolib.api.machine.mana.feature.IManaEnergyMachine;
import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.RecipeHelper;
import com.gtolib.utils.NumberUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public final class RecipeLogicProvider extends CapabilityBlockProvider<RecipeLogic> {

    public RecipeLogicProvider() {
        super(GTCEu.id("recipe_logic_provider"));
    }

    @Nullable
    @Override
    protected RecipeLogic getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        return GTCapabilityHelper.getRecipeLogic(level, pos, side);
    }

    @Override
    protected void write(CompoundTag data, RecipeLogic capability) {
        if (capability.isIdle() && capability instanceof IEnhancedRecipeLogic recipeLogic && recipeLogic.gtolib$getIdleReason() != null) {
            data.putString("reason", Component.Serializer.toJson(recipeLogic.gtolib$getIdleReason()));
        }
        data.putBoolean("Working", capability.isWorking());
        var recipeInfo = new CompoundTag();
        var recipe = capability.getLastRecipe();
        if (recipe != null) {
            var inputEUt = RecipeHelper.getInputEUt(recipe);
            var outputEUt = RecipeHelper.getOutputEUt(recipe);
            var inputManat = RecipeHelper.getInputMANAt(recipe);
            var outputManat = RecipeHelper.getOutputMANAt(recipe);

            recipeInfo.putLong("EUt", inputEUt - outputEUt);
            recipeInfo.putLong("Manat", inputManat - outputManat);

            if (capability.machine instanceof CrossRecipeMultiblockMachine machine && machine.getEnergyInterfacePartMachine() != null) {
                recipeInfo.putDouble("totalEu", machine.getTotalEu());
            }

        }
        data.put("Recipe", recipeInfo);
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        if (capData.getBoolean("Working")) {
            var recipeInfo = capData.getCompound("Recipe");
            if (!recipeInfo.isEmpty()) {
                double totalEu = recipeInfo.getDouble("totalEu");
                if (totalEu > 0) {
                    var text = Component.translatable("gtceu.top.energy_consumption").append(" ").append(Component.literal(NumberUtils.formatDouble(totalEu)).withStyle(ChatFormatting.RED)).append(Component.literal(" EU").withStyle(ChatFormatting.RESET))
                            .append(Component.literal(" (").withStyle(ChatFormatting.GREEN));
                    var tier = GTUtil.getOCTierByVoltage(totalEu > Long.MAX_VALUE ? Long.MAX_VALUE : (long) totalEu);
                    text = text.append(Component.literal(String.format("%sA",
                            FormattingUtil.formatNumber2Places(totalEu / (float) GTValues.VEX[tier]))));
                    if (tier < GTValues.TIER_COUNT) {
                        text = text.append(Component.literal(GTValues.VNF[tier])
                                .withStyle(style -> style.withColor(GTValues.VC[tier])));
                    } else {
                        int speed = tier - 14;
                        text = text.append(Component
                                .literal("MAX")
                                .withStyle(style -> style.withColor(TooltipHelper.rainbowColor(speed)))
                                .append(Component.literal("+")
                                        .withStyle(style -> style.withColor(GTValues.VC[Math.min(14, speed)]))
                                        .append(Component.literal(FormattingUtil.formatNumbers(tier - 14)))
                                        .withStyle(style -> style.withColor(GTValues.VC[Math.min(14, speed)]))));

                    }
                    text = text.append(Component.literal(")").withStyle(ChatFormatting.GREEN));
                    tooltip.add(text);
                } else {
                    var EUt = recipeInfo.getLong("EUt");
                    var Manat = recipeInfo.getLong("Manat");
                    boolean isSteam = false;
                    if (blockEntity instanceof MetaMachineBlockEntity mbe) {
                        var machine = mbe.getMetaMachine();
                        if (machine instanceof DummyEnergyMachine energyMachine && !energyMachine.jade()) {
                            return;
                        } else if (machine instanceof SimpleSteamMachine ssm) {
                            EUt = (long) (EUt * ssm.getConversionRate());
                            isSteam = true;
                        } else if (machine instanceof SteamParallelMultiblockMachine smb) {
                            EUt = (long) (EUt * smb.getConversionRate());
                            isSteam = true;
                        } else if (EUt > 0 && machine instanceof IManaEnergyMachine) {
                            Manat += EUt;
                            EUt = 0;
                        }
                    }

                    if (EUt != 0) {
                        MutableComponent text;
                        boolean isInput = EUt > 0;
                        EUt = Math.abs(EUt);
                        if (isSteam) {
                            text = Component.literal(FormattingUtil.formatNumbers(EUt)).withStyle(ChatFormatting.GREEN)
                                    .append(Component.literal(" mB/t").withStyle(ChatFormatting.RESET));
                        } else {
                            var tier = GTUtil.getOCTierByVoltage(EUt);

                            text = Component.literal(FormattingUtil.formatNumbers(EUt)).withStyle(ChatFormatting.RED)
                                    .append(Component.literal(" EU/t").withStyle(ChatFormatting.RESET)
                                            .append(Component.literal(" (").withStyle(ChatFormatting.GREEN)));
                            text = text.append(Component.literal(String.format("%sA",
                                    FormattingUtil.formatNumber2Places(EUt / (float) GTValues.VEX[tier]))));
                            if (tier < GTValues.TIER_COUNT) {
                                text = text.append(Component.literal(GTValues.VNF[tier])
                                        .withStyle(style -> style.withColor(GTValues.VC[tier])));
                            } else {
                                int speed = tier - 14;
                                text = text.append(Component
                                        .literal("MAX")
                                        .withStyle(style -> style.withColor(TooltipHelper.rainbowColor(speed)))
                                        .append(Component.literal("+")
                                                .withStyle(style -> style.withColor(GTValues.VC[Math.min(14, speed)]))
                                                .append(Component.literal(FormattingUtil.formatNumbers(tier - 14)))
                                                .withStyle(style -> style.withColor(GTValues.VC[Math.min(14, speed)]))));

                            }
                            text = text.append(Component.literal(")").withStyle(ChatFormatting.GREEN));
                        }

                        if (isInput) {
                            tooltip.add(Component.translatable("gtceu.top.energy_consumption").append(" ").append(text));
                        } else {
                            tooltip.add(Component.translatable("gtceu.top.energy_production").append(" ").append(text));
                        }
                    }
                    if (Manat != 0) {
                        boolean isInput = Manat > 0;
                        Manat = Math.abs(Manat);
                        MutableComponent text = Component.literal(FormattingUtil.formatNumbers(Manat)).withStyle(ChatFormatting.AQUA)
                                .append(Component.literal(" Mana/t").withStyle(ChatFormatting.RESET));

                        if (isInput) {
                            tooltip.add(Component.translatable("gtocore.recipe.mana_consumption").append(" ").append(text));
                        } else {
                            tooltip.add(Component.translatable("gtocore.recipe.mana_production").append(" ").append(text));
                        }
                    }
                }

            }
        } else {
            var reason = capData.getString("reason");
            if (reason.isEmpty()) return;
            var c = Component.Serializer.fromJson(reason);
            if (c == null) return;
            tooltip.add(c.withStyle(ChatFormatting.GRAY));
        }
    }
}
