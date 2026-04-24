package com.gtocore.integration.jade.provider;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.DummyEnergyMachine;
import com.gtolib.api.machine.feature.ICustomElectricMachine;
import com.gtolib.api.machine.feature.multiblock.ICrossRecipeMachine;
import com.gtolib.api.machine.mana.feature.IManaEnergyMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeDefinition;
import com.gtolib.utils.NumberUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.PosUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

@DataGeneratorScanned
public final class RecipeLogicProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @RegisterLanguage(cn = "该机器所在区块未强制加载", en = "The chunk the machine is in is not forced loaded")
    private static final String LOADED = "gtocore.machine.forced_loaded";
    @RegisterLanguage(cn = "耗能 %s §cA §a@ %s §f(%s)", en = "Energy Consumption %s §cA §a@ %s §f(%s)")
    private static final String ENERGY_CONSUMPTION = "gtocore.machine.energy_consumption";
    @RegisterLanguage(cn = "产能 %s §cA §a@ %s §f(%s)", en = "Energy Production %s §cA §a@ %s §f(%s)")
    private static final String ENERGY_PRODUCTION = "gtocore.machine.energy_production";

    @Override
    public ResourceLocation getUid() {
        return GTCEu.id("recipe_logic_provider");
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        var capData = blockAccessor.getServerData();
        if (capData.getBoolean("notLoaded")) {
            tooltip.add(Component.translatable(LOADED).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        if (capData.getBoolean("Working")) {
            var recipeInfo = capData.getCompound("Recipe");
            if (!recipeInfo.isEmpty()) {
                double totalEu = recipeInfo.getDouble("totalEu");
                if (totalEu > 0) {
                    long voltage = recipeInfo.contains("voltage") ? recipeInfo.getLong("voltage") : totalEu > Long.MAX_VALUE ? Long.MAX_VALUE : (long) totalEu;
                    tooltip.add(formatEnergyLine(recipeInfo.getBoolean("isGenerator"), totalEu, voltage, NumberUtils.formatDouble(totalEu) + " EU"));
                } else {
                    var EUt = recipeInfo.getLong("EUt");
                    var Manat = recipeInfo.getLong("Manat");
                    boolean isSteam = false;
                    if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity mbe) {
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
                    List<Component> list = new java.util.ArrayList<>();
                    getEUtTooltip(list, EUt, isSteam, recipeInfo.contains("voltage") ? recipeInfo.getLong("voltage") : -1);
                    tooltip.addAll(list);

                    if (Manat != 0) {
                        boolean isInput = Manat > 0;
                        MutableComponent text = Component.literal(FormattingUtil.formatNumbers(Math.abs(Manat))).withStyle(ChatFormatting.AQUA)
                                .append(Component.literal(" Mana/t").withStyle(ChatFormatting.RESET));

                        if (isInput) {
                            tooltip.add(Component.translatable("gtocore.recipe.mana_consumption").append(" ").append(text));
                        } else {
                            tooltip.add(Component.translatable("gtocore.recipe.mana_production").append(" ").append(text));
                        }
                    }
                    if (recipeInfo.contains("Origin")) {
                        long parallel = capData.getLong("parallel");
                        parallel = Math.max(1, parallel);
                        long batchParallel = capData.getLong("batch_parallel");
                        batchParallel = Math.max(1, batchParallel);
                        var origin = recipeInfo.getCompound("Origin");
                        if (!origin.isEmpty()) {
                            var originEUt = origin.getLong("EUt");
                            var originManat = origin.getLong("Manat");
                            double energyEfficiency = 0;
                            if (originEUt != 0 && EUt != 0) {
                                energyEfficiency = (double) EUt * batchParallel / (parallel * originEUt) * 100;
                            }
                            double manaEfficiency = 0;
                            if (originManat != 0 && Manat != 0) {
                                manaEfficiency = (double) Manat * batchParallel / (parallel * originManat) * 100;
                            }
                            if (energyEfficiency != 0) {
                                String key = EUt > 0 ? "gtocore.recipe.efficiency" : "gtocore.recipe.efficiency.o";
                                tooltip.add(Component.translatable(key, Component.literal(
                                        String.format("%s%%", FormattingUtil.formatNumber2Places(Math.abs(energyEfficiency)))).withStyle(ChatFormatting.GOLD)));
                            }
                            if (manaEfficiency != 0) {
                                String key = Manat > 0 ? "gtocore.recipe.mana_efficiency" : "gtocore.recipe.mana_efficiency.o";
                                tooltip.add(Component.translatable(key, Component.literal(
                                        String.format("%s%%", FormattingUtil.formatNumber2Places(Math.abs(manaEfficiency)))).withStyle(ChatFormatting.GOLD)));
                            }
                            if (origin.contains("MaxProgress")) {
                                var originMaxProgress = origin.getInt("MaxProgress");
                                var currentProgress = getCurrentMaxProgress(capData);
                                if (originMaxProgress > 0 && currentProgress > 0) {
                                    double timeCost = currentProgress / (double) batchParallel / (double) originMaxProgress * 100;
                                    tooltip.add(Component.translatable("gtocore.recipe.time_cost_multiplier", Component.literal(
                                            String.format("%s%%", FormattingUtil.formatNumber2Places(timeCost))).withStyle(ChatFormatting.GOLD)));
                                }
                            }
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

    private static int getCurrentMaxProgress(CompoundTag capData) {
        if (capData.contains(GTCEu.id("workable_provider").toString())) {
            var workable = capData.getCompound(GTCEu.id("workable_provider").toString());
            if (workable.contains("null")) {
                var progress = workable.getCompound("null");
                return progress.getInt("MaxProgress");
            }
        }
        return 0;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity machineBlock) {
            if (machineBlock.getLevel() instanceof ServerLevel serverLevel && !serverLevel.getChunkSource().chunkMap.getDistanceManager().shouldForceTicks(PosUtils.getChunkLong(machineBlock.getBlockPos()))) {
                compoundTag.putBoolean("notLoaded", true);
            }
            if (machineBlock.metaMachine instanceof IRecipeLogicMachine recipeLogicMachine) {
                var capability = recipeLogicMachine.getRecipeLogic();
                if (capability instanceof IEnhancedRecipeLogic recipeLogic) {
                    if (capability.isIdle() && recipeLogic.gtolib$getIdleReason() != null) {
                        compoundTag.putString("reason", Component.Serializer.toJson(recipeLogic.gtolib$getIdleReason()));
                    } else if (capability.isWaiting()) {
                        if (!capability.getFancyTooltip().isEmpty()) {
                            compoundTag.putString("reason", Component.Serializer.toJson(capability.getFancyTooltip().get(0)));
                        } else if (recipeLogic.gtolib$getIdleReason() != null) {
                            compoundTag.putString("reason", Component.Serializer.toJson(recipeLogic.gtolib$getIdleReason()));
                        }
                    } else {
                        compoundTag.putBoolean("Working", capability.isWorking());
                        var recipeInfo = getRecipeInfo(capability);
                        compoundTag.put("Recipe", recipeInfo);
                    }
                }
            }
        }
    }

    private static @NotNull CompoundTag getRecipeInfo(RecipeLogic capability) {
        var recipeInfo = new CompoundTag();
        var recipe = capability.getLastRecipe();
        if (recipe != null) {
            var inputEUt = recipe.getInputEUt();
            var outputEUt = recipe.getOutputEUt();
            recipeInfo.putLong("EUt", inputEUt - outputEUt);
            recipeInfo.putLong("Manat", Recipe.of(recipe).manat);
            recipeInfo.putLong("voltage", getVoltage(capability));

            if (capability.machine instanceof ICustomElectricMachine machine && machine.isActivated()) {
                recipeInfo.putDouble("totalEu", machine.getTotalEu());
                if (machine.isGenerator()) {
                    recipeInfo.putBoolean("isGenerator", true);
                }
            }
            var originRecipe = capability.getLastOriginRecipe();
            if (originRecipe == null && capability.machine instanceof ICrossRecipeMachine c) {
                originRecipe = c.getLastRecipes().stream().findFirst().orElse(null);
            }
            if (originRecipe != null) {
                var originInputEUt = originRecipe.getInputEUt();
                var originOutputEUt = originRecipe.getOutputEUt();
                var origin = new CompoundTag();
                if (originInputEUt != inputEUt || originOutputEUt != outputEUt || Recipe.of(recipe).manat != RecipeDefinition.of(originRecipe).manat) {
                    origin.putLong("EUt", originInputEUt - originOutputEUt);
                    origin.putLong("Manat", RecipeDefinition.of(originRecipe).manat);
                }
                var maxProgress = originRecipe.duration;
                if (maxProgress > 0) {
                    origin.putInt("MaxProgress", maxProgress);
                }
                recipeInfo.put("Origin", origin);
            }

        }
        return recipeInfo;
    }

    public static void getEUtTooltip(List<Component> tooltip, long EUt, boolean isSteam, long voltage) {
        if (EUt != 0) {
            MutableComponent text;
            boolean isInput = EUt > 0;
            EUt = Math.abs(EUt);
            if (isSteam) {
                text = Component.literal(FormattingUtil.formatNumbers(EUt)).withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(" mB/t").withStyle(ChatFormatting.RESET));
            } else {
                text = formatEnergyLine(!isInput, EUt, voltage, FormattingUtil.formatNumbers(EUt) + " EU/t");
            }

            tooltip.add(text);
        }
    }

    private static MutableComponent formatEnergyLine(boolean isGenerator, double EUt, long voltage, String eutText) {
        if (voltage <= 0) {
            voltage = (long) EUt;
        }
        byte tier = GTUtil.getOCTierByVoltage(voltage);
        return Component.translatable(isGenerator ? ENERGY_PRODUCTION : ENERGY_CONSUMPTION,
                Component.literal(FormattingUtil.formatNumber2Places(EUt / GTValues.VEX[tier])).withStyle(ChatFormatting.RED),
                getTierText(tier),
                Component.literal(eutText).withStyle(ChatFormatting.WHITE));
    }

    private static MutableComponent getTierText(byte tier) {
        if (tier < GTValues.TIER_COUNT) {
            return Component.literal(GTValues.VNF[tier])
                    .withStyle(style -> style.withColor(GTValues.VC[tier]));
        }
        int speed = tier - 14;
        return Component.literal("MAX")
                .withStyle(style -> style.withColor(TooltipHelper.rainbowColor(speed)))
                .append(Component.literal("+")
                        .withStyle(style -> style.withColor(GTValues.VC[Math.min(14, speed)]))
                        .append(Component.literal(FormattingUtil.formatNumbers(tier - 14)))
                        .withStyle(style -> style.withColor(GTValues.VC[Math.min(14, speed)])));
    }

    public static long getVoltage(RecipeLogic capability) {
        long voltage = -1;
        if (capability.machine instanceof SimpleTieredMachine machine) {
            voltage = GTValues.VEX[machine.getTier()];
        } else if (capability.machine instanceof SimpleGeneratorMachine machine) {
            voltage = GTValues.VEX[machine.getTier()];
        } else if (capability.machine instanceof WorkableElectricMultiblockMachine machine) {
            var handlers = machine.getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP);
            if (handlers.isEmpty()) {
                handlers = machine.getCapabilitiesFlat(IO.OUT, EURecipeCapability.CAP);
            }
            for (IRecipeHandler<?> handler : handlers) {
                if (handler instanceof IEnergyContainer container) {
                    voltage = Math.max(voltage, Math.max(container.getInputVoltage(), container.getOutputVoltage()));
                }
            }
        }
        return voltage;
    }
}
