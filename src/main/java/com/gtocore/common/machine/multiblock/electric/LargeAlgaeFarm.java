package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.api.data.Algae;
import com.gtocore.api.gui.helper.LineChartHelper;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.part.ae.StorageAccessPartMachine;

import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.gregtechceu.gtceu.api.GTValues.V;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Biomass;
import static com.gtolib.api.GTOValues.GLASS_TIER;

public class LargeAlgaeFarm extends ElectricMultiblockMachine implements ITierCasingMachine {

    Map<Algae, List<Long>> statistics = new EnumMap<>(Algae.class);
    static final int statMaxSeconds = 30;
    int lastStatTick = 0;
    boolean statsChanged = false;

    StorageAccessPartMachine.AlgaeAccessHatch algaeAccessHatch;
    final TierCasingTrait tierCasingTrait;
    int lightIntensity = 0;
    float redWeight = 1.0f;
    float greenWeight = 1.0f;
    float blueWeight = 1.0f;

    @DescSynced
    Algae selectedAlgae = Algae.BlueAlge;

    public LargeAlgaeFarm(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        this.tierCasingTrait = new TierCasingTrait(this, GLASS_TIER);
    }

    @Override
    public void onPartScan(@NotNull IMultiPart iMultiPart) {
        super.onPartScan(iMultiPart);
        if (algaeAccessHatch == null && iMultiPart instanceof StorageAccessPartMachine.AlgaeAccessHatch h) {
            this.algaeAccessHatch = h;
            h.setInfinite(true);
            h.setCheck(true);
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe) {

            @Override
            public boolean handleTickRecipe(GTRecipe recipe) {
                if (super.handleTickRecipe(recipe)) {
                    if (getOffsetTimer() % 20 == 0) produceAlgae();
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        algaeAccessHatch.setObserve(true);
        for (Algae algae : Algae.values()) {
            long amount = algaeAccessHatch != null ? algaeAccessHatch.getAvailableStacks().get(algae.aeKey()) : 0;
            list.add(Component.empty().append(algae.getDisplayName())
                    .append(Component.literal(" x " + FormattingUtil.formatNumbers(amount))));
        }
    }

    private Recipe getRecipe() {
        return getRecipeBuilder().duration(200).EUt(V[tier] / 2).buildRawRecipe();
    }

    @Override
    public SoundEntry getSound() {
        return GTSoundEntries.COOLING;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        if (algaeAccessHatch != null) {
            algaeAccessHatch.setInfinite(false);
            algaeAccessHatch.setCheck(false);
            this.algaeAccessHatch = null;
        }
    }

    @Override
    public void onUnload() {
        if (algaeAccessHatch != null) {
            algaeAccessHatch.setCheck(false);
            this.algaeAccessHatch = null;
        }
        super.onUnload();
    }

    private static long getIncreasement(long current, int voltageTier, int tier, double absorption, double weight) {
        if (current <= 0) return 0;
        double r = (voltageTier + 1.0) * absorption;
        double capacity = Math.pow(4.0, tier) * weight;
        double x = (double) current;
        double increaseFactor = 0.1 + 0.9 * Math.exp(-r / 2);

        double numerator = x * (capacity - x) * (1 - increaseFactor);
        double denominator = x + ((capacity - x) * increaseFactor);

        double derivative = numerator / denominator;
        return (long) derivative;
    }

    /**
     * 1.电压决定r内稟增长率
     * 2.玻璃等级决定k值上限
     * 3.启动需输入一定量的藻类（输入什么藻类最终就产什么藻类），将其记录为第一秒的种群数量，之后每秒以s型曲线增长，并根据当前种群数量消耗生物质。
     * 4.根据卤素灯两两组合改变输出藻类的权重，例如输入蓝色和绿色卤素灯就使红藻的权重增加到60%，剩余藻类均分。
     */

    private void produceAlgae() {
        updateLightIntensity();
        EnumMap<Algae, Double> algaeRedAbsorptions = new EnumMap<>(Algae.class);
        EnumMap<Algae, Double> algaeGreenAbsorptions = new EnumMap<>(Algae.class);
        EnumMap<Algae, Double> algaeBlueAbsorptions = new EnumMap<>(Algae.class);
        double totalAbsorptionRed = 1e-6;
        double totalAbsorptionGreen = 1e-6;
        double totalAbsorptionBlue = 1e-6;

        if (lightIntensity == 0) {
            return;
        }
        for (Algae algae : Algae.values()) {
            algaeRedAbsorptions.put(algae, algae.redAbsorption / 255.0 * redWeight);
            totalAbsorptionRed += algae.redAbsorption / 255.0 * redWeight;
            algaeGreenAbsorptions.put(algae, algae.greenAbsorption / 255.0 * greenWeight);
            totalAbsorptionGreen += algae.greenAbsorption / 255.0 * greenWeight;
            algaeBlueAbsorptions.put(algae, algae.blueAbsorption / 255.0 * blueWeight);
            totalAbsorptionBlue += algae.blueAbsorption / 255.0 * blueWeight;
        }

        for (Algae algae : Algae.values()) {
            double algaeWeight = NumberUtils.max(
                    algaeRedAbsorptions.get(algae) / totalAbsorptionRed,
                    algaeGreenAbsorptions.get(algae) / totalAbsorptionGreen,
                    algaeBlueAbsorptions.get(algae) / totalAbsorptionBlue);
            long currentCount = algaeAccessHatch.extract(algae.aeKey(), Long.MAX_VALUE, Actionable.SIMULATE, IActionSource.ofMachine(algaeAccessHatch));
            long increasement = getIncreasement(currentCount, tier, getCasingTier(GLASS_TIER),
                    (algaeRedAbsorptions.get(algae) +
                            algaeGreenAbsorptions.get(algae) +
                            algaeBlueAbsorptions.get(algae)) * lightIntensity / 16,
                    algaeWeight);
            long total = Math.min(MachineUtils.getFluidAmount(this, Biomass.getFluid())[0], currentCount + increasement);
            MachineUtils.inputFluid(this, Biomass.getFluid(), total);
            if (total - currentCount > 0) {
                algaeAccessHatch.insert(algae.aeKey(), total - currentCount, Actionable.MODULATE, IActionSource.ofMachine(algaeAccessHatch));
            } else {
                algaeAccessHatch.extract(algae.aeKey(), currentCount - total, Actionable.MODULATE, IActionSource.ofMachine(algaeAccessHatch));
            }
            updateStatistics(algae, total);
        }
    }

    private long getRedIntensity() {
        return Math.min(MachineUtils.getItemAmount(this, GTOItems.RED_HALIDE_LAMP.get())[0], 16);
    }

    private long getGreenIntensity() {
        return Math.min(MachineUtils.getItemAmount(this, GTOItems.GREEN_HALIDE_LAMP.get())[0], 16);
    }

    private long getBlueIntensity() {
        return Math.min(MachineUtils.getItemAmount(this, GTOItems.BLUE_HALIDE_LAMP.get())[0], 16);
    }

    private void updateLightIntensity() {
        long total = getRedIntensity() + getGreenIntensity() + getBlueIntensity();
        this.lightIntensity = (int) Math.min(16, total);
        this.redWeight = getRedIntensity() / (float) total;
        this.greenWeight = getGreenIntensity() / (float) total;
        this.blueWeight = getBlueIntensity() / (float) total;
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup widget = (WidgetGroup) super.createUIWidget();
        widget.getWidgetsByType(DraggableScrollableWidgetGroup.class).stream().findAny()
                .ifPresent(ds -> ds.setSizeHeight(ds.getSizeHeight() - 55));
        widget.addWidget(new StatisticWidget(8, 69, 174, 50));
        return widget;
    }

    private void updateStatistics(Algae algae, long amount) {
        lastStatTick = getOffsetTimer();
        List<Long> stat = statistics.computeIfAbsent(algae, (a) -> new LongArrayList(31));
        stat.add(amount);
        if (stat.size() > statMaxSeconds) {
            stat.removeFirst();
        }
        statsChanged = true;
    }

    @Override
    public boolean hasBatchConfig() {
        return false;
    }

    @Override
    public boolean hasOverclockConfig() {
        return false;
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }

    private class StatisticWidget extends WidgetGroup {

        ComponentPanelWidget panelWidget;

        public StatisticWidget(int x, int y, int width, int height) {
            super(x, y, width, height);
            addWidget(panelWidget = new ComponentPanelWidget(3, 3, l -> l.add(
                    Component.translatable("config.jade.display_mode").append(" ")
                            .append(Arrays.stream(Algae.values()).map(algae -> {
                                MutableComponent m = ((MutableComponent) algae.getDisplayName());
                                if (algae == selectedAlgae) {
                                    m.withStyle(ChatFormatting.UNDERLINE);
                                }
                                return ComponentPanelWidget.withButton(m, "select_algae_" + algae.ordinal());
                            }).collect(GTOUtils.joiningComponent(Component.literal(" "))))))
                    .clickHandler(this::handleDisplayClick));
        }

        private void handleDisplayClick(String componentData, ClickData clickData) {
            if (componentData.startsWith("select_algae_")) {
                int ordinal = Integer.parseInt(componentData.substring("select_algae_".length()));
                selectedAlgae = Algae.values()[ordinal];
            } else {
                LargeAlgaeFarm.this.handleDisplayClick(componentData, clickData);
            }
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.pose().pushPose();
            graphics.pose().translate(getPositionX(), getPositionY(), 0f);
            LineChartHelper.INSTANCE.drawLineChart(
                    graphics,
                    statistics.getOrDefault(selectedAlgae, List.of()),
                    getSizeWidth(),
                    getSizeHeight(),
                    selectedAlgae.getColor() | 0xFF000000);
            graphics.pose().popPose();
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public void detectAndSendChanges() {
            super.detectAndSendChanges();
            if (statsChanged) {
                statsChanged = false;
                writeUpdateInfo(6, (buf) -> {
                    buf.writeInt(statistics.size());
                    for (Algae algae : statistics.keySet()) {
                        buf.writeInt(algae.ordinal());
                        List<Long> stat = statistics.get(algae);
                        buf.writeInt(stat.size());
                        for (Long v : stat) {
                            buf.writeLong(v);
                        }
                    }
                });
            }
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
            if (id == 6) {
                statistics.clear();
                int mapSize = buffer.readInt();
                for (int i = 0; i < mapSize; i++) {
                    Algae algae = Algae.values()[buffer.readInt()];
                    int statSize = buffer.readInt();
                    LongArrayList stat = new LongArrayList(statSize);
                    for (int j = 0; j < statSize; j++) {
                        stat.add(buffer.readLong());
                    }
                    statistics.put(algae, stat);
                }
                statsChanged = true;
            } else {
                super.readUpdateInfo(id, buffer);
            }
        }
    }
}
