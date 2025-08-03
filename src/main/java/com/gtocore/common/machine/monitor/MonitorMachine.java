package com.gtocore.common.machine.monitor;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.mana.feature.IManaEnergyMachine;
import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.utils.FluidUtils;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.NumberUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableBiMap;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.impl.WailaCommonRegistration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class MonitorMachine extends AbstractInfoProviderMonitor {

    @DescSynced
    private Component[] bufferCache = new Component[0];
    private List<FormattedCharSequence> textListCache;
    private BlockPos pos;
    private BlockEntity tile;
    private BlockAccessor accessor;
    private List<IServerDataProvider<BlockAccessor>> providers;
    private static int index = 0;
    private static final ImmutableBiMap<Integer, DisplayRegistry> DISPLAY_REGISTRY = ImmutableBiMap.<Integer, DisplayRegistry>builder()
            .put(index++, DisplayRegistry.MACHINE_NAME)
            .put(index++, DisplayRegistry.MACHINE_ENERGY)
            .put(index++, DisplayRegistry.MACHINE_PROGRESS)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_LOGIC_EU)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_LOGIC_MANA)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT_ITEM_1)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT_ITEM_2)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT_ITEM_3)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT_FLUID_1)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT_FLUID_2)
            .put(index++, DisplayRegistry.MACHINE_RECIPE_OUTPUT_FLUID_3)
            .put(index++, DisplayRegistry.MACHINE_MANTENANCE)
            .build();
    @Persisted
    private final NotifiableItemStackHandler inventory;
    private boolean isCardChange;

    public MonitorMachine(IMachineBlockEntity holder) {
        super(holder);
        inventory = createInventoryItemHandler();
        isCardChange = true;
    }

    public MonitorMachine(Object o) {
        this((IMachineBlockEntity) o);
    }

    private NotifiableItemStackHandler createInventoryItemHandler() {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.NONE);
        storage.setFilter(i -> {
            var tag = i.getTag();
            return tag != null && tag.getBoolean("machine");
        });
        return storage;
    }

    @Override
    public DisplayComponentList provideInformation() {
        var informationList = super.provideInformation();
        if (bufferCache.length == DISPLAY_REGISTRY.size()) {
            for (int i = 0; i < bufferCache.length; i++) {
                if (DISPLAY_REGISTRY.containsKey(i) && bufferCache[i] != null && !bufferCache[i].equals(Component.empty())) {
                    informationList.addIfAbsent(
                            DISPLAY_REGISTRY.get(i).id(),
                            bufferCache[i].getVisualOrderText());
                }
            }
        }
        return informationList;
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getOffsetTimer() % 10 == 0 && bufferCache != null) {
            textListCache = Stream.of(bufferCache)
                    .map(component -> Objects.requireNonNullElse(component, Component.empty()))
                    .map(Component::getVisualOrderText)
                    .toList();

        }
    }

    @Override
    public void syncInfoFromServer() {
        if (textListCache == null) {
            bufferCache = getComponentArray();
        }
    }

    public Component[] getComponentArray() {
        if (isCardChange) {
            ItemStack card = inventory.storage.getStackInSlot(0);
            if (card.isEmpty()) {
                return new Component[0];
            }
            CompoundTag posTags = card.getTag();
            if (posTags == null || !posTags.contains("posX") || !posTags.contains("posY") || !posTags.contains("posZ")) {
                return new Component[0];
            }
            pos = new BlockPos(posTags.getInt("posX"), posTags.getInt("posY"), posTags.getInt("posZ"));
            isCardChange = false;
        }
        if (pos == null) {
            return new Component[0];
        }
        Level level = getLevel();
        CompoundTag tags = new CompoundTag();
        tile = Objects.requireNonNull(getLevel()).getBlockEntity(pos);
        if (tile != null) {
            if (accessor == null) {
                accessor = new BlockAccessor() {

                    @Override
                    public Level getLevel() {
                        return level;
                    }

                    @Override
                    public Player getPlayer() {
                        return null;
                    }

                    @Override
                    public @NotNull CompoundTag getServerData() {
                        return tags;
                    }

                    @Override
                    public BlockHitResult getHitResult() {
                        return null;
                    }

                    @Override
                    public boolean isServerConnected() {
                        return false;
                    }

                    @Override
                    public ItemStack getPickedResult() {
                        return null;
                    }

                    @Override
                    public boolean showDetails() {
                        return false;
                    }

                    @Override
                    public Object getTarget() {
                        return null;
                    }

                    @Override
                    public void toNetwork(FriendlyByteBuf friendlyByteBuf) {}

                    @Override
                    public boolean verifyData(CompoundTag compoundTag) {
                        return false;
                    }

                    @Override
                    public Block getBlock() {
                        return null;
                    }

                    @Override
                    public BlockState getBlockState() {
                        return null;
                    }

                    @Override
                    public BlockEntity getBlockEntity() {
                        return tile;
                    }

                    @Override
                    public BlockPos getPosition() {
                        return pos;
                    }

                    @Override
                    public Direction getSide() {
                        return null;
                    }

                    @Override
                    public boolean isFakeBlock() {
                        return false;
                    }

                    @Override
                    public ItemStack getFakeBlock() {
                        return null;
                    }
                };
            }
            if (providers == null) {
                providers = WailaCommonRegistration.INSTANCE.getBlockNBTProviders(tile);
            }
            if (!providers.isEmpty()) {

                providers.forEach(provider -> {
                    try {
                        provider.appendServerData(tags, accessor);
                    } catch (Exception ignored) {}
                });
                Component[] textListCache = new Component[DISPLAY_REGISTRY.size()];
                // 机器名
                textListCache[IndexOf(DisplayRegistry.MACHINE_NAME)] = tile.getBlockState().getBlock().getName().append("[").append(pos.toShortString()).append("]");
                // 能量
                textListCache[IndexOf(DisplayRegistry.MACHINE_ENERGY)] = getEnergyComponent(tags);
                // 进度
                textListCache[IndexOf(DisplayRegistry.MACHINE_PROGRESS)] = getProgressComponent(tags);

                var recipeLogic = getRecipeLogicComponents(tags);
                // 能耗/产能(EU)
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_LOGIC_EU)] = recipeLogic[0];
                // 能耗/产能(Mana)
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_LOGIC_MANA)] = recipeLogic[1];
                // 产物
                var recipeOut = getRecipeOutputComponents(tags);
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT)] = recipeOut[0];
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT_ITEM_1)] = recipeOut[1];
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT_ITEM_2)] = recipeOut[2];
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT_ITEM_3)] = recipeOut[3];
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT_FLUID_1)] = recipeOut[4];
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT_FLUID_2)] = recipeOut[5];
                textListCache[IndexOf(DisplayRegistry.MACHINE_RECIPE_OUTPUT_FLUID_3)] = recipeOut[6];
                // 维护
                textListCache[IndexOf(DisplayRegistry.MACHINE_MANTENANCE)] = getMantenanceComponent(tags);

                return textListCache;
            }
        }
        return new Component[0];
    }

    private Component getEnergyComponent(CompoundTag tags) {
        Component component = Component.empty();
        CompoundTag capData = tags.getCompound(GTCEu.id("electric_container_provider").toString()).getCompound("null");
        if (capData.contains("Energy") || capData.contains("MaxEnergy")) {
            BigInteger energy = new BigInteger(capData.getByteArray("Energy"));
            BigInteger maxEnergy = new BigInteger(capData.getByteArray("MaxEnergy"));
            if (maxEnergy.compareTo(BigInteger.ZERO) > 0) {
                BigInteger threshold = BigInteger.valueOf(1000000000000L);
                String energyStr = FormattingUtil.formatNumberOrSic(energy, threshold);
                String maxEnergyStr = FormattingUtil.formatNumberOrSic(maxEnergy, threshold);
                component = Component.translatable("gtceu.jade.energy_stored", energyStr, maxEnergyStr);
            }
        }
        return component;
    }

    private Component getProgressComponent(CompoundTag tags) {
        Component component = Component.empty();
        CompoundTag capData = tags.getCompound(GTCEu.id("workable_provider").toString()).getCompound("null");
        if (capData.getBoolean("Active")) {
            int currentProgress = capData.getInt("Progress");
            int maxProgress = capData.getInt("MaxProgress");
            if (capData.getBoolean("Research")) {
                String current = FormattingUtil.formatNumberReadable(currentProgress);
                String max = FormattingUtil.formatNumberReadable(maxProgress);
                component = Component.translatable("gtceu.jade.progress_computation", current, max);
            } else {
                Component text;
                if (maxProgress < 20) {
                    text = Component.translatable("gtceu.jade.progress_tick", currentProgress, maxProgress);
                } else {
                    text = Component.translatable("gtceu.jade.progress_sec", Math.round((float) currentProgress / 20.0F), Math.round((float) maxProgress / 20.0F));
                }
                if (maxProgress > 0) {
                    component = text;
                }
            }
        }
        return component;
    }

    private Component[] getRecipeLogicComponents(CompoundTag tags) {
        Component[] components = new Component[] { Component.empty(), Component.empty() };
        CompoundTag capData = tags.getCompound(GTCEu.id("recipe_logic_provider").toString()).getCompound("null");
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
                    components[0] = text;
                } else {
                    var EUt = recipeInfo.getLong("EUt");
                    var Manat = recipeInfo.getLong("Manat");
                    boolean isSteam = false;
                    if (tile instanceof MetaMachineBlockEntity mbe) {
                        var machine = mbe.getMetaMachine();
                        if (machine instanceof SimpleSteamMachine ssm) {
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
                            components[0] = Component.translatable("gtceu.top.energy_consumption").append(" ").append(text);
                        } else {
                            components[0] = Component.translatable("gtceu.top.energy_production").append(" ").append(text);
                        }
                    }
                    if (Manat != 0) {
                        boolean isInput = Manat > 0;
                        Manat = Math.abs(Manat);
                        MutableComponent text = Component.literal(FormattingUtil.formatNumbers(Manat)).withStyle(ChatFormatting.AQUA)
                                .append(Component.literal(" Mana/t").withStyle(ChatFormatting.RESET));

                        if (isInput) {
                            components[1] = Component.translatable("gtocore.recipe.mana_consumption").append(" ").append(text);
                        } else {
                            components[1] = Component.translatable("gtocore.recipe.mana_production").append(" ").append(text);
                        }
                    }
                }

            }
        } else {
            var reason = capData.getString("reason");
            if (!reason.isEmpty()) {
                var c = Component.Serializer.fromJson(reason);
                if (c != null) {
                    components[0] = c.withStyle(ChatFormatting.GRAY);
                }
            }
        }
        return components;
    }

    private Component[] getRecipeOutputComponents(CompoundTag tags) {
        Component[] components = new Component[] { Component.empty(), Component.empty(), Component.empty(), Component.empty(), Component.empty(), Component.empty(), Component.empty() };

        CompoundTag capData = tags.getCompound(GTCEu.id("recipe_output_info").toString()).getCompound("null");
        if (capData.getBoolean("Working")) {
            List<CompoundTag> outputItems = new ArrayList<>();
            if (capData.contains("OutputItems", Tag.TAG_LIST)) {
                ListTag itemTags = capData.getList("OutputItems", Tag.TAG_COMPOUND);
                if (!itemTags.isEmpty()) {
                    for (Tag tag : itemTags) {
                        if (tag instanceof CompoundTag tCompoundTag) {
                            outputItems.add(tCompoundTag);
                        }
                    }
                }
            }
            List<CompoundTag> outputFluids = new ArrayList<>();
            if (capData.contains("OutputFluids", Tag.TAG_LIST)) {
                ListTag fluidTags = capData.getList("OutputFluids", Tag.TAG_COMPOUND);
                for (Tag tag : fluidTags) {
                    if (tag instanceof CompoundTag tCompoundTag) {
                        outputFluids.add(tCompoundTag);
                    }
                }
            }
            if (!outputItems.isEmpty() || !outputFluids.isEmpty()) {
                components[0] = Component.translatable("gtceu.top.recipe_output");
            }

            for (int i = 0; i < Math.min(outputItems.size(), 3); i++) {
                var tag = outputItems.get(i);
                if (tag != null && !tag.isEmpty()) {
                    ItemStack stack = GTOUtils.loadItemStack(tag);
                    int chance = tag.getInt("c");
                    boolean estimated = chance < ContentBuilder.maxChance;
                    long count = tag.getLong("a");
                    if (estimated) {
                        count = Math.max(1, count * tag.getLong("p") * chance / ContentBuilder.maxChance);
                    }

                    components[i + 1] = Component.literal(" ")
                            .append((estimated ? "~" : "") + count)
                            .append("× ")
                            .append(getItemName(stack))
                            .withStyle(ChatFormatting.WHITE);

                }
            }
            for (int i = 0; i < Math.min(outputFluids.size(), 3); i++) {
                var tag = outputFluids.get(i);
                if (tag != null && !tag.isEmpty()) {
                    FluidStack stack = GTOUtils.loadFluidStack(tag);
                    int chance = tag.getInt("c");
                    boolean estimated = chance < ContentBuilder.maxChance;
                    long count = tag.getLong("a");
                    if (estimated) {
                        count = Math.max(1, count * tag.getLong("p") * chance / ContentBuilder.maxChance);
                    }

                    components[i + 4] = Component.literal(" ")
                            .append((estimated ? "~" : "") + FluidUtils.getUnicodeMillibuckets(count))
                            .append(" ")
                            .append(getFluidName(stack))
                            .withStyle(ChatFormatting.WHITE);
                }
            }
        }
        return components;
    }

    private Component getMantenanceComponent(CompoundTag tags) {
        Component component = Component.empty();
        CompoundTag capData = tags.getCompound(GTCEu.id("maintenance_info").toString()).getCompound("null");
        if (capData.contains("hasProblems", 1)) {
            if (capData.getBoolean("hasProblems")) {
                component = Component.translatable("gtceu.top.maintenance_broken").withStyle(ChatFormatting.RED);
            } else {
                component = Component.translatable("gtceu.top.maintenance_fixed").withStyle(ChatFormatting.GREEN);
            }
        }
        return component;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = super.getAvailableRLs();
        rls.addAll(DISPLAY_REGISTRY.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(DisplayRegistry::id)
                .toList());
        return rls;
    }

    private Component getItemName(ItemStack stack) {
        return ComponentUtils.wrapInSquareBrackets(stack.getItem().getDescription()).withStyle(ChatFormatting.WHITE);
    }

    private Component getFluidName(FluidStack stack) {
        return ComponentUtils.wrapInSquareBrackets(stack.getDisplayName()).withStyle(ChatFormatting.WHITE);
    }

    @SuppressWarnings("ConstantConditions")
    private int IndexOf(DisplayRegistry name) {
        return DISPLAY_REGISTRY.inverse().get(name);
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup baseWidget = (WidgetGroup) super.createUIWidget();
        SlotWidget slot = new SlotWidget(inventory.storage, 0, 16, 144, true, true);
        slot.appendHoverTooltips(Component.translatable("gtocore.machine.machine_monitor.slot"));
        slot.setChangeListener(() -> isCardChange = true);
        slot.setBackground(GuiTextures.SLOT.copy().scale(18 / 16f), MACHINE_COORDS_OVERLAY);
        baseWidget.addWidget(slot);
        return baseWidget;
    }

    private static final IGuiTexture MACHINE_COORDS_OVERLAY = new ResourceTexture(GTOCore.id("textures/gui/machine_coords_overlay.png")).scale(15 / 16f);
}
