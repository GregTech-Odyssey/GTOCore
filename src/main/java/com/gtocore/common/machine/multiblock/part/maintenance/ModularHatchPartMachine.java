package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.ICleanroomReceiver;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.SingleCustomItemStackHandler;
import com.gtocore.common.data.GTOMachines;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

@DataGeneratorScanned
public class ModularHatchPartMachine extends ACMHatchPartMachine implements IModularMaintenance, IMachineModifyDrops {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ModularHatchPartMachine.class, ACMHatchPartMachine.MANAGED_FIELD_HOLDER);

    private TickableSubscription tickSubs;

    @Persisted
    private final NotifiableItemStackHandler temperatureModuleInv;
    @Persisted
    private final NotifiableItemStackHandler gravityModuleInv;
    @Persisted
    private final NotifiableItemStackHandler vacuumModuleInv;
    @Persisted
    private final NotifiableItemStackHandler cleanroomModuleInv;
    @Persisted
    private int temperature = 293;
    @Persisted
    private int activeTemperature = 293;
    @Persisted
    @DescSynced
    private int currentGravity = 0;
    @Persisted
    @DescSynced
    private boolean vacuumMode = false;
    @Persisted
    @DescSynced
    private boolean gravityMode = false;
    /// indicates whether player could set temperature
    /// notice that even if temperatureMode is false, the temperature could be set passively by other machines
    @Persisted
    @DescSynced
    private boolean temperatureMode = false;

    private IntInputWidget gravityWidget;
    private IntInputWidget temperatureWidget;

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public ModularHatchPartMachine(MetaMachineBlockEntity metaTileEntityId) {
        super(metaTileEntityId);

        temperatureModuleInv = new NotifiableItemStackHandler(this, 1, IO.IN, IO.BOTH, SingleCustomItemStackHandler::new);
        temperatureModuleInv.setFilter(stack -> stack.getItem() == Wrapper.TEMPERATURE_CHECK);
        temperatureModuleInv.addChangedListener(this::onConditionChange);

        gravityModuleInv = new NotifiableItemStackHandler(this, 1, IO.IN, IO.BOTH, SingleCustomItemStackHandler::new);
        gravityModuleInv.setFilter(stack -> stack.getItem() == Wrapper.GRAVITY_CHECK);
        gravityModuleInv.addChangedListener(this::onConditionChange);

        vacuumModuleInv = new NotifiableItemStackHandler(this, 1, IO.IN, IO.BOTH, SingleCustomItemStackHandler::new);
        vacuumModuleInv.setFilter(stack -> stack.getItem() == Wrapper.VACUUM_CHECK);
        vacuumModuleInv.addChangedListener(this::onConditionChange);

        cleanroomModuleInv = new NotifiableItemStackHandler(this, 1, IO.IN, IO.BOTH, SingleCustomItemStackHandler::new);
        cleanroomModuleInv.setFilter(stack -> Wrapper.CLEAN_CHECK.containsKey(stack.getItem()));
        cleanroomModuleInv.addChangedListener(this::onConditionChange);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    @Override
    public int getCurrentGravity() {
        return gravityMode ? currentGravity : 1;
    }

    @Override
    public int getTemperature() {
        return temperatureMode ? activeTemperature : temperature;
    }

    @Override
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getVacuumTier() {
        return vacuumMode ? 4 : IModularMaintenance.super.getVacuumTier();
    }

    @Override
    public @NotNull MetaMachine self() {
        return this;
    }

    private static final int xlabel = 6;
    private static final int xslot = 160;
    private static final int ylabel = 4;

    private static final int MIN_TEMPERATURE = 273;
    private static final int MAX_TEMPERATURE = 2273;
    private static final int MIN_GRAVITY = 0;
    private static final int MAX_GRAVITY = 100;

    public boolean isVacuumMode() {
        return vacuumMode;
    }

    public boolean isGravityMode() {
        return gravityMode;
    }

    public boolean isTemperatureMode() {
        return temperatureMode;
    }

    @Override
    public @NotNull Widget createUIWidget() {
        WidgetGroup group;
        int y = 0;
        group = new DraggableScrollableWidgetGroup(0, 0, 200, 100);
        group.addWidget(new WidgetGroup(4, 4, 192, 140)
                // Duration Multiplier
                .addWidget(getConfigPanel(xlabel, ylabel + y++ * 22,
                        () -> getTextWidgetText(this::getDurationMultiplier),
                        () -> Component.translatable("gtceu.maintenance.configurable_duration.modify"),
                        this::incInternalMultiplier, this::decInternalMultiplier, () -> true, getMIN_DURATION_MULTIPLIER(), getMAX_DURATION_MULTIPLIER()))
                // Temperature
                .addWidget(new SlotWidget(temperatureModuleInv.storage, 0, xslot, ylabel + y * 22, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(TOOLTIP_KEY, Wrapper.TEMPERATURE_CHECK.getDefaultInstance().getDisplayName(), Component.translatable(TEMPERATURE_FUNC))))
                .addWidget(getConfigPanel(xlabel, ylabel + y++ * 22,
                        () -> Component.translatable("gtocore.machine.current_temperature", getTemperature()),
                        () -> temperatureMode ?
                                Component.translatable(TEMPERATURE_CONFIG) :
                                Component.translatable(TOOLTIP_REQUIRED_KEY, Wrapper.TEMPERATURE_CHECK.getDefaultInstance().getDisplayName()),
                        v -> setActiveTemperature(activeTemperature + v),
                        v -> setActiveTemperature(activeTemperature - v),
                        () -> temperatureMode, MIN_TEMPERATURE, MAX_TEMPERATURE))
                // Gravity
                .addWidget(new SlotWidget(gravityModuleInv.storage, 0, xslot, ylabel + y * 22, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(TOOLTIP_KEY, Wrapper.GRAVITY_CHECK.getDefaultInstance().getDisplayName(), Component.translatable(GRAVITY_FUNC))))
                .addWidget(getConfigPanel(xlabel, ylabel + y++ * 22,
                        () -> Component.translatable("forge.entity_gravity").append(": %s".formatted(getCurrentGravity())),
                        () -> gravityMode ?
                                Component.translatable(GRAVITY_CONFIG) :
                                Component.translatable(TOOLTIP_REQUIRED_KEY, Wrapper.GRAVITY_CHECK.getDefaultInstance().getDisplayName()),
                        v -> setCurrentGravity(getCurrentGravity() + v),
                        v -> setCurrentGravity(getCurrentGravity() - v),
                        () -> gravityMode, MIN_GRAVITY, MAX_GRAVITY))
                // Vacuum
                .addWidget(new SlotWidget(vacuumModuleInv.storage, 0, xslot, ylabel + y * 22, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(TOOLTIP_KEY, Wrapper.VACUUM_CHECK.getDefaultInstance().getDisplayName(), Component.translatable(VACUUM_TIER_4))))
                .addWidget(new ComponentPanelWidget(xlabel, ylabel + y++ * 22 + 11, (list) -> list.add(Component.translatable("gtocore.recipe.vacuum.tier", getVacuumTier()))))
                // Cleanroom
                .addWidget(new SlotWidget(cleanroomModuleInv.storage, 0, xslot, ylabel + y * 22, true, true)
                        .setBackground(GuiTextures.SLOT)
                        .setHoverTooltips(Component.translatable(TOOLTIP_KEY_CLEANROOM)))
                .addWidget(new ComponentPanelWidget(xlabel, ylabel + y++ * 22, (list) -> {
                    list.add(Component.translatable(CURRENT_CLEANROOM));
                    list.add(getCurrentCleanroom().withStyle(ChatFormatting.GREEN));
                }))
                .setBackground(GuiTextures.BACKGROUND_INVERSE));
        return group;
    }

    private static ComponentPanelWidget getConfigPanel(int x, int y,
                                                       Supplier<Component> firstLine, Supplier<Component> secondLineTitle,
                                                       IntConsumer onAdd, IntConsumer onSub,
                                                       BooleanSupplier enableWrite,
                                                       float min, float max) {
        return new ComponentPanelWidget(x, y, list -> {
            list.add(firstLine.get());
            MutableComponent buttonText = secondLineTitle.get().copy();
            if (enableWrite.getAsBoolean()) {
                buttonText.append(" ");
                buttonText.append(ComponentPanelWidget.withButton(Component.literal("[-]").withStyle(ChatFormatting.RED), "sub"));
                buttonText.append(" ");
                buttonText.append(ComponentPanelWidget.withButton(Component.literal("[+]").withStyle(ChatFormatting.GREEN), "add"));
            }
            list.add(buttonText.setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    Component.translatable(RANGE_LIMIT, String.format("%.2f", min), String.format("%.2f", max))))));
        }).setMaxWidthLimit(150 - 8 - 8 - 4).clickHandler((componentData, clickData) -> {
            if (!clickData.isRemote && enableWrite.getAsBoolean()) {
                int multiplier = clickData.isCtrlClick ? 100 : clickData.isShiftClick ? 10 : 1;
                if ("sub".equals(componentData)) {
                    onSub.accept(multiplier);
                } else if ("add".equals(componentData)) {
                    onAdd.accept(multiplier);
                }
            }
        });
    }

    private MutableComponent getCurrentCleanroom() {
        if (!getControllers().isEmpty() &&
                getControllers().first() instanceof ICleanroomReceiver receiver) {
            if (receiver.getCleanroom() != null) {
                List<MutableComponent> cleanroomTypes = receiver.getCleanroom().getTypes().stream()
                        .map(type -> Component.translatable(type.getTranslationKey()))
                        .toList();
                if (cleanroomTypes.isEmpty()) {
                    return Component.translatable(CLEANROOM_NOT_SET);
                }
                MutableComponent result = Component.empty();
                for (int i = 0; i < cleanroomTypes.size(); i++) {
                    result.append(cleanroomTypes.get(i));
                    if (i < cleanroomTypes.size() - 1) {
                        result.append(", ");
                    }
                }
                return result;

            } else {
                return Component.translatable(CLEANROOM_NOT_SET);
            }
        }
        return Component.translatable(CLEANROOM_NOT_APPLICABLE);
    }

    private int getActiveTemperature() {
        return activeTemperature;
    }

    private void setActiveTemperature(int activeTemperature) {
        this.activeTemperature = Mth.clamp(activeTemperature, MIN_TEMPERATURE, MAX_TEMPERATURE);
    }

    @Override
    public void onDrops(List<ItemStack> drops) {
        clearInventory(temperatureModuleInv);
        clearInventory(gravityModuleInv);
        clearInventory(vacuumModuleInv);
        clearInventory(cleanroomModuleInv);
    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        onConditionChange();
    }

    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        if (controller instanceof ICleanroomReceiver receiver) {
            receiver.setCleanroom(null);
        }
    }

    private void onConditionChange() {
        temperatureMode = !temperatureModuleInv.getStackInSlot(0).isEmpty();
        gravityMode = !gravityModuleInv.getStackInSlot(0).isEmpty();
        vacuumMode = !vacuumModuleInv.getStackInSlot(0).isEmpty();
        var cleanroom = Wrapper.CLEAN_CHECK.get(cleanroomModuleInv.getStackInSlot(0).getItem());
        if (!getControllers().isEmpty() &&
                getControllers().first() instanceof ICleanroomReceiver receiver && receiver.getCleanroom() != cleanroom) {
            receiver.setCleanroom(cleanroom);
        }
        if (gravityWidget != null) {
            gravityWidget.setActive(vacuumMode).setVisible(vacuumMode);
        }
        if (temperatureWidget != null) {
            temperatureWidget.setActive(temperatureMode).setVisible(temperatureMode);
        }
    }

    private void setCurrentGravity(int gravity) {
        currentGravity = Mth.clamp(gravity, MIN_GRAVITY, MAX_GRAVITY);
    }

    @Override
    public boolean showFancyTooltip() {
        return super.showFancyTooltip();
    }

    @Override
    public int getMaxTemperature() {
        return MAX_TEMPERATURE;
    }

    private static class Wrapper {

        private static final Item VACUUM_CHECK = GTOMachines.VACUUM_CONFIGURATION_HATCH.getItem();
        private static final Item GRAVITY_CHECK = GTOMachines.GRAVITY_CONFIGURATION_HATCH.getItem();
        private static final Item TEMPERATURE_CHECK = GTOMachines.ELECTRIC_HEATER.getItem();
        private static final Map<Item, ICleanroomProvider> CLEAN_CHECK = Map.of(
                GTOMachines.CLEANING_CONFIGURATION_MAINTENANCE_HATCH.getItem(), CMHatchPartMachine.DUMMY_CLEANROOM,
                GTOMachines.STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.getItem(), CMHatchPartMachine.STERILE_DUMMY_CLEANROOM,
                GTOMachines.LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH.getItem(), CMHatchPartMachine.LAW_DUMMY_CLEANROOM);
    }

    @RegisterLanguage(cn = "在槽位放入%s以启用%s功能", en = "Place %s in the corresponding slot to enable %s functionality")
    private static final String TOOLTIP_KEY = "gtocore.machine.modular_maintenance.tooltip";
    @RegisterLanguage(cn = "在槽位放入超净可配置维护仓以启用对应等级的超净环境", en = "Place a Cleanroom Configurable Maintenance Hatch in the slot to enable the corresponding level of cleanroom environment")
    private static final String TOOLTIP_KEY_CLEANROOM = "gtocore.machine.modular_maintenance.tooltip.cleanroom";
    @RegisterLanguage(cn = "放入%s以启用调节", en = "Insert %s to enable adjustment")
    private static final String TOOLTIP_REQUIRED_KEY = "gtocore.machine.modular_maintenance.required.tooltip";
    @RegisterLanguage(cn = "控制重力：", en = "Control Gravity：")
    private static final String GRAVITY_CONFIG = "gtocore.machine.modular_maintenance.gravity_config";
    @RegisterLanguage(cn = "调节温度：", en = "Adjust Temperature：")
    private static final String TEMPERATURE_CONFIG = "gtocore.machine.modular_maintenance.temperature_config";
    @RegisterLanguage(cn = "未设置超净环境", en = "Cleanroom Not Set")
    private static final String CLEANROOM_NOT_SET = "gtocore.machine.modular_maintenance.no_cleanroom";
    @RegisterLanguage(cn = "无控制器或不接受超净", en = "No Controller or Not Accepting Cleanroom")
    private static final String CLEANROOM_NOT_APPLICABLE = "gtocore.machine.modular_maintenance.no_controller";
    @RegisterLanguage(cn = "调节范围限制：%s~%s", en = "Adjustment Range Limit: %s~%s")
    private static final String RANGE_LIMIT = "gtocore.machine.modular_maintenance.range_limit";
    @RegisterLanguage(cn = "当前的超净环境：", en = "Current Cleanroom: ")
    private static final String CURRENT_CLEANROOM = "gtocore.machine.modular_maintenance.current_cleanroom";
    @RegisterLanguage(cn = "4级真空", en = "Tier 4 Vacuum")
    private static final String VACUUM_TIER_4 = "gtocore.machine.modular_maintenance.vacuum_tier_4";
    @RegisterLanguage(cn = "可控温度", en = "Controllable Temperature")
    private static final String TEMPERATURE_FUNC = "gtocore.machine.modular_maintenance.temperature_check";
    @RegisterLanguage(cn = "可控重力", en = "Controllable Gravity")
    private static final String GRAVITY_FUNC = "gtocore.machine.modular_maintenance.gravity_check";
}
