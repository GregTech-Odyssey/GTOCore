package com.gtocore.common.machine.multiblock.storage;

import com.gtocore.client.hud.Configurator;
import com.gtocore.common.block.WirelessEnergyUnitBlock;

import com.gtolib.api.GTOValues;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.utils.FunctionContainer;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyInfoProvider;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.BigIntegerUtils;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.*;

public final class WirelessEnergySubstationMachine extends NoRecipeLogicMultiblockMachine implements IExtendWirelessEnergyContainerHolder, ITierCasingMachine, IEnergyInfoProvider {

    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private final TierCasingTrait tierCasingTrait;
    private final Multimap<Integer, BlockPos> wirelessEnergyUnitPositions = Multimaps.newMultimap(new Int2ObjectOpenHashMap<>(), ObjectOpenHashSet::new);

    @Persisted
    private ResourceLocation dimension;

    public WirelessEnergySubstationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GTOValues.GLASS_TIER);
    }

    private void loadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        int tier = getCasingTier(GTOValues.GLASS_TIER);
        FunctionContainer<ArrayList<WirelessEnergyUnitBlock.BlockData>, ?> functionContainer = getMultiblockState().getMatchContext().get("wirelessEnergyUnit");
        int loss = 0;
        int i = 0;
        BigInteger capacity = BigInteger.ZERO;
        if (functionContainer != null) {
            ArrayList<WirelessEnergyUnitBlock.BlockData> blocks = functionContainer.getValue();
            for (WirelessEnergyUnitBlock.BlockData block : blocks) {
                if (block.block() == null) {
                    wirelessEnergyUnitPositions.put(0, block.pos());
                    continue;
                }
                if (block.block().getTier() <= tier) {
                    i++;
                    capacity = capacity.add(block.block().getCapacity());
                    loss += block.block().getLoss();
                }
                wirelessEnergyUnitPositions.put(block.block().getTier(), block.pos());
            }
            blocks.clear();
        }
        container.setLoss(i == 0 ? 0 : loss / i);
        container.setCapacity(capacity.multiply(BigInteger.valueOf(Math.max(1, i / 2))));
        dimension = level.dimension().location();
        container.setDimension(dimension, true);
    }

    private void unloadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        wirelessEnergyUnitPositions.clear();
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        container.setCapacity(BigInteger.ZERO);
        container.setLoss(0);
        container.setDimension(level.dimension().location(), false);
    }

    @Override
    public void onStructureInvalid() {
        unloadContainer();
        super.onStructureInvalid();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        loadContainer();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel) {
            var container = getWirelessEnergyContainer();
            if (container == null) return;
            if (dimension != null) container.setDimension(dimension, true);
            if (isFormed()) loadContainer();
        }
    }

    @Override
    public void onUnload() {
        unloadContainer();
        super.onUnload();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (this.getUUID() == null) return;
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(getLevel(), this.getUUID())).withStyle(ChatFormatting.AQUA));
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1", FormattingUtil.formatNumbers(container.getStorage()) + " / " + FormattingUtil.formatNumbers(container.getCapacity())).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.2", FormattingUtil.formatNumbers(container.getRate()), container.getRate() / GTValues.VEX[GTUtil.getFloorTierByVoltage(container.getRate())], Component.literal(GTValues.VNF[GTUtil.getFloorTierByVoltage(container.getRate())])).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", (double) container.getLoss() / 10));
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        Configurator c;
        configuratorPanel.attachConfigurators(
                c = new Configurator(GuiTextures.LIGHT_ON, GuiTextures.LIGHT_OFF));
        if (isRemote()) c.setHudInstance("wireless_energy_hud");
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }

    @Override
    public EnergyInfo getEnergyInfo() {
        var container = getWirelessEnergyContainer();
        if (container == null) {
            return new EnergyInfo(BigInteger.ZERO, BigInteger.ZERO);
        } else {
            return new EnergyInfo(container.getCapacity(), container.getStorage());
        }
    }

    @Override
    public boolean supportsBigIntEnergyValues() {
        return true;
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public void setWirelessEnergyContainerCache(final WirelessEnergyContainer WirelessEnergyContainerCache) {
        this.WirelessEnergyContainerCache = WirelessEnergyContainerCache;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }

    @Override
    public long getInputPerSec() {
        var container = getWirelessEnergyContainer();
        if (container == null) {
            return 0;
        }
        var input = BigIntegerUtils.getLongValue(container.getEnergyStat().getAvgEnergy().toBigInteger());
        return input > 0 ? input : 0;
    }

    @Override
    public long getOutputPerSec() {
        var container = getWirelessEnergyContainer();
        if (container == null) {
            return 0;
        }
        var output = BigIntegerUtils.getLongValue(container.getEnergyStat().getAvgEnergy().toBigInteger().negate());
        return output > 0 ? output : 0;
    }

    /**
     * @return 成功替换的方块数量
     */
    public int substituteBlocks(WirelessEnergyUnitBlock block, int count, ServerPlayer player) {
        if (getLevel() == null || wirelessEnergyUnitPositions.isEmpty() || count <= 0) {
            return 0;
        }
        List<WirelessEnergyUnitBlock.BlockData> candidates = new ArrayList<>();
        int tier = block.getTier();
        for (int t = 1; t < tier; t++) {
            var positionsForTier = wirelessEnergyUnitPositions.get(t);
            for (BlockPos pos : positionsForTier) {
                if (pos != null) {
                    candidates.add(new WirelessEnergyUnitBlock.BlockData(WirelessEnergyUnitBlock.get(t), pos));
                }
            }
        }
        if (candidates.isEmpty()) {
            return 0;
        }
        candidates.sort(Comparator.comparingInt((WirelessEnergyUnitBlock.BlockData data) -> data.pos().getY())
                .thenComparingInt(data -> data.pos().getX())
                .thenComparingInt(data -> data.pos().getZ()));
        int numToReplace = Math.min(count, candidates.size());
        List<WirelessEnergyUnitBlock.BlockData> toReplace = candidates.subList(0, numToReplace);
        int successfulCount = 0;
        for (WirelessEnergyUnitBlock.BlockData data : toReplace) {
            BlockPos pos = data.pos();
            var originBlockDrop = data.block();
            if (getLevel().setBlock(pos, block.defaultBlockState(), 11)) {
                successfulCount++;
                if (originBlockDrop != null && !player.getInventory().add(originBlockDrop.asItem().getDefaultInstance())) {
                    player.drop(originBlockDrop.asItem().getDefaultInstance(), false);
                }
            }
        }
        if (successfulCount > 0) {
            this.requestCheck();
        }
        return successfulCount;
    }
}
