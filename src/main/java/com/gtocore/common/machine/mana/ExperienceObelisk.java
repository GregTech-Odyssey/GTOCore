package com.gtocore.common.machine.mana;

import com.gtocore.api.gui.GTOGuiTextures;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import dev.shadowsoffire.placebo.util.EnchantmentUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.IntSupplier;

import static com.gtocore.common.data.GTOFluids.XP_JUICE;

@DataGeneratorScanned
public class ExperienceObelisk extends MetaMachine implements IFancyUIMachine, IDropSaveMachine {

    @Persisted
    private final NotifiableFluidTank experienceTank;
    @Persisted
    @Getter
    private int currentConfigAmount = 0;
    @Persisted
    @Getter
    private boolean isConfiguringLevels = false;

    @Persisted
    @Getter
    private boolean vacuumHopperMode = false;
    private final ConditionalSubscriptionHandler tickSubs;

    public ExperienceObelisk(MetaMachineBlockEntity holder) {
        super(holder);
        experienceTank = new NotifiableFluidTank(this, 1, Integer.MAX_VALUE, IO.NONE, IO.BOTH);
        experienceTank.setFilter(f -> f.getFluid() == XP_JUICE.getSource());
        tickSubs = new ConditionalSubscriptionHandler(this, this::absorbXpOrb, 20, this::isVacuumHopperMode);
    }

    private void absorbXpOrb() {
        this.tickSubs.updateSubscription();
        Level world = holder.getLevel();
        if (world == null) return;
        var aabb = new AABB(holder.getSelf().getBlockPos()).inflate(14.0);
        var xpOrbs = world.getEntitiesOfClass(ExperienceOrb.class, aabb);
        for (var xpOrb : xpOrbs) {
            int juiceAmount = xpOrb.getValue();
            int xpAbsorbed = experienceTank.fill(new FluidStack(XP_JUICE.getSource(), juiceAmount), IFluidHandler.FluidAction.EXECUTE);
            if (xpAbsorbed > 0) {
                xpOrb.discard();
                int remainingXp = xpOrb.getValue() - xpAbsorbed;
                if (remainingXp > 0) {
                    ExperienceOrb orb = new ExperienceOrb(
                            world,
                            xpOrb.getX(),
                            xpOrb.getY(),
                            xpOrb.getZ(),
                            remainingXp);
                    world.addFreshEntity(orb);
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        tickSubs.initialize(getLevel());
    }

    @Override
    public void onUnload() {
        super.onUnload();
        tickSubs.unsubscribe();
    }

    @Override
    public @Nullable <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return LazyOptional.of(() -> experienceTank).cast();
        }
        return null;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        var widgetsOuter = new WidgetGroup(0, 0, 100, 100);
        widgetsOuter.setAlign(Align.TOP_CENTER);
        var widgets = new WidgetGroup(0, 8, 100, 92);
        widgetsOuter.addWidget(widgets);

        // 配置输入组件
        widgets.addWidget(new WidgetGroup(0, 0, 100, 12).addWidget(new LabelWidget(0, 0, LANG_CONFIGURE_AMOUNT).setAlign(Align.CENTER)));
        widgets.addWidget(createConfigAmountWidget());

        // 切换等级/经验点模式
        widgets.addWidget(createLevelsModeToggleButton());
        widgets.addWidget(new LabelWidget(20, 40, () -> Component.translatable(LANG_STORED_EXPERIENCE, EnchantmentUtils.getLevelForExperience(experienceTank.getFluidInTank(0).getAmount())).getString()));

        // 经验转移按钮
        widgets.addWidget(createTransferConfiguredButton(entityPlayer, true, 0));
        widgets.addWidget(createTransferConfiguredButton(entityPlayer, false, 20));
        widgets.addWidget(createTransferAllButton(entityPlayer, true, 40));
        widgets.addWidget(createTransferAllButton(entityPlayer, false, 60));

        // 真空模式按钮
        widgets.addWidget(createVacuumHopperModeButton(80));
        widgets.addWidget(fixToolsWithMendingEnchantmentButton(entityPlayer, 100));

        return new ModularUI(176, 166, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, 176, 166))
                .widget(widgetsOuter);
    }

    @Override
    public Widget createUIWidget() {
        return new WidgetGroup();
    }

    private IntInputWidget createConfigAmountWidget() {
        var intWidget = new IntInputWidget(this::getCurrentConfigAmount, this::setCurrentConfigAmount);
        intWidget.setSelfPosition(0, 12);
        intWidget.setMax(100000000);
        intWidget.setMin(0);
        intWidget.setValue(currentConfigAmount);
        return intWidget;
    }

    private ToggleButtonWidget createLevelsModeToggleButton() {
        ObjectHolder<ToggleButtonWidget> toggleHolder = new ObjectHolder<>(null);
        ToggleButtonWidget toggleButton = new ToggleButtonWidget(
                0, 40, 16, 16,
                this::isConfiguringLevels,
                pressed -> {
                    isConfiguringLevels = pressed;
                    if (LDLib.isRemote()) {
                        toggleHolder.value.setHoverTooltips(getConfigureLevelsOrPointsTooltip(pressed));
                    }
                });
        toggleHolder.value = toggleButton;
        toggleButton.setPressed(isConfiguringLevels);
        toggleButton.setTexture(new GuiTextureGroup(GuiTextures.BUTTON, GTOGuiTextures.SMALL_XP_ORB),
                new GuiTextureGroup(GuiTextures.BUTTON, GTOGuiTextures.LARGE_XP_ORB.scale(0.8f)));
        toggleButton.setHoverTooltips(getConfigureLevelsOrPointsTooltip(isConfiguringLevels));
        return toggleButton;
    }

    private ButtonWidget createTransferConfiguredButton(Player player, boolean toPlayer, int x) {
        var button = createTransferButton(player, () -> calculateConfiguredAmount(player, toPlayer), x);
        if (toPlayer) {
            button.setButtonTexture(GuiTextures.BUTTON_RIGHT.copy().rotate(45));
            button.setHoverTooltips(Component.translatable(LANG_TRANSFER_TO_PLAYER));
        } else {
            button.setButtonTexture(GuiTextures.BUTTON_LEFT.copy().rotate(45));
            button.setHoverTooltips(Component.translatable(LANG_TRANSFER_TO_MACHINE));
        }
        return button;
    }

    private ButtonWidget createTransferAllButton(Player player, boolean toPlayer, int x) {
        IntSupplier amountSupplier = toPlayer ? () -> experienceTank.getFluidInTank(0).getAmount() : () -> -getExperiencePoints(player);
        var button = createTransferButton(player, amountSupplier, x);
        if (toPlayer) {
            button.setButtonTexture(GuiTextures.BUTTON_RIGHT.copy().rotate(45));
            button.setHoverTooltips(Component.translatable(LANG_TRANSFER_ALL_TO_PLAYER));
        } else {
            button.setButtonTexture(GuiTextures.BUTTON_LEFT.copy().rotate(45));
            button.setHoverTooltips(Component.translatable(LANG_TRANSFER_ALL_TO_MACHINE));
        }
        return button;
    }

    private ToggleButtonWidget createVacuumHopperModeButton(int x) {
        ToggleButtonWidget button = new ToggleButtonWidget(x, 60, 16, 16, this::isVacuumHopperMode, this::setVacuumHopperMode);
        button.setTexture(createToggleTexture(false), createToggleTexture(true));
        button.setHoverTooltips(Component.translatable(LANG_VACUUM_HOPPER_MODE));
        return button;
    }

    private GuiTextureGroup createToggleTexture(boolean active) {
        var baseTexture = GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true).copy();
        if (active) {
            return new GuiTextureGroup(GuiTextures.BUTTON, baseTexture.getSubTexture(0, 0.5, 1, 0.5).scale(0.8f));
        } else {
            return new GuiTextureGroup(GuiTextures.BUTTON, baseTexture.getSubTexture(0, 0, 1, 0.5).scale(0.8f));
        }
    }

    private int calculateConfiguredAmount(Player player, boolean toPlayer) {
        int amount = toPlayer ? currentConfigAmount : -currentConfigAmount;
        if (isConfiguringLevels) {
            int targetLevel = player.experienceLevel + (toPlayer ? amount : -amount);
            int experienceNeeded = EnchantmentUtils.getTotalExperienceForLevel(targetLevel) - getExperiencePoints(player);
            amount = toPlayer ? experienceNeeded : -experienceNeeded;
        }
        return amount;
    }

    private ButtonWidget createTransferButton(Player player, IntSupplier amountToPlayer, int x) {
        return new ButtonWidget(x, 60, 16, 16,
                (clickData -> {
                    int amount = amountToPlayer.getAsInt();
                    if (player instanceof ServerPlayer && amount != 0) {
                        int canTransfer = amount;
                        if (amount > 0) {
                            int beforeAdd = getExperiencePoints(player);
                            EnchantmentUtils.chargeExperience(player, -amount);
                            int afterAdd = getExperiencePoints(player);
                            int drained = experienceTank.drain(afterAdd - beforeAdd, IFluidHandler.FluidAction.EXECUTE).getAmount();
                            if (drained < canTransfer) {
                                EnchantmentUtils.chargeExperience(player, canTransfer - drained);
                            }
                        } else {
                            canTransfer = experienceTank.fill(new FluidStack(XP_JUICE.getSource(), -canTransfer), IFluidHandler.FluidAction.SIMULATE);
                            EnchantmentUtils.chargeExperience(player, canTransfer);
                            experienceTank.fill(new FluidStack(XP_JUICE.getSource(), canTransfer), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }));
    }

    private Widget fixToolsWithMendingEnchantmentButton(Player player, int x) {
        return new ButtonWidget(x, 60, 16, 16,
                (clickData -> {
                    if (player instanceof ServerPlayer) {
                        int value = experienceTank.getFluidInTank(0).getAmount();
                        int extracted = experienceTank.drain(value, IFluidHandler.FluidAction.EXECUTE).getAmount();
                        int remainingExp = repairPlayerItems(player, extracted, extracted);
                        if (remainingExp > 0) {
                            experienceTank.fill(new FluidStack(XP_JUICE.getSource(), remainingExp), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }))
                .setButtonTexture(GuiTextures.BUTTON, GuiTextures.TOOL_SLOT_OVERLAY.copy().scale(0.88f))
                .setHoverTooltips(Component.translatable(LANG_REPAIR_MENDING_TOOLS));
    }

    /// @see ExperienceOrb#repairPlayerItems(Player, int)
    private int repairPlayerItems(Player p_147093_, int remainingExp, int initialValue) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, p_147093_, ItemStack::isDamaged);
        if (entry != null) {
            ItemStack itemstack = entry.getValue();
            int i = Math.min((int) (initialValue * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
            itemstack.setDamageValue(itemstack.getDamageValue() - i);
            int j = remainingExp - this.durabilityToXp(i);
            return j > 0 ? this.repairPlayerItems(p_147093_, j, initialValue) : 0;
        } else {
            return remainingExp;
        }
    }

    private int durabilityToXp(int pDurability) {
        return pDurability / 2;
    }

    public void setVacuumHopperMode(boolean vacuumHopperMode) {
        this.vacuumHopperMode = vacuumHopperMode;
        this.tickSubs.updateSubscription();
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        IDropSaveMachine.super.saveToItem(tag);
        tag.putInt("xp", experienceTank.getFluidInTank(0).getAmount());
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        IDropSaveMachine.super.loadFromItem(tag);
        int xpAmount = tag.getInt("xp");
        experienceTank.setFluidInTank(0, new FluidStack(XP_JUICE.getSource(), xpAmount));
    }

    private void setCurrentConfigAmount(int integer) {
        this.currentConfigAmount = integer;
    }

    private static Component getConfigureLevelsOrPointsTooltip(boolean isConfiguringLevels) {
        return isConfiguringLevels ? Component.translatable(LANG_CONFIGURE_LEVELS) : Component.translatable(LANG_CONFIGURE_POINTS);
    }

    private static int getExperiencePoints(Player player) {
        return (int) (EnchantmentUtils.getTotalExperienceForLevel(player.experienceLevel) + (player.experienceProgress) * player.getXpNeededForNextLevel());
    }

    @RegisterLanguage(cn = "单位：经验等级", en = "Unit: Experience Levels")
    private static final String LANG_CONFIGURE_LEVELS = "gtocore.machine.experience_obelisk.configure_levels";
    @RegisterLanguage(cn = "单位：经验值", en = "Unit: Experience Points")
    private static final String LANG_CONFIGURE_POINTS = "gtocore.machine.experience_obelisk.configure_points";
    @RegisterLanguage(cn = "设定经验值转移数量", en = "Transfer the configured experience amount")
    private static final String LANG_CONFIGURE_AMOUNT = "gtocore.machine.experience_obelisk.configure_amount";
    @RegisterLanguage(cn = "从玩家转移设定的经验值到机器", en = "Transfer the configured experience from player to machine")
    private static final String LANG_TRANSFER_TO_MACHINE = "gtocore.machine.experience_obelisk.transfer_to_machine";
    @RegisterLanguage(cn = "从机器转移设定的经验值到玩家", en = "Transfer the configured experience from machine to player")
    private static final String LANG_TRANSFER_TO_PLAYER = "gtocore.machine.experience_obelisk.transfer_to_player";
    @RegisterLanguage(cn = "转移所有的经验值到玩家", en = "Transfer all experience from machine to player")
    private static final String LANG_TRANSFER_ALL_TO_PLAYER = "gtocore.machine.experience_obelisk.transfer_all_to_player";
    @RegisterLanguage(cn = "转移所有的经验值到机器", en = "Transfer all experience from machine to player")
    private static final String LANG_TRANSFER_ALL_TO_MACHINE = "gtocore.machine.experience_obelisk.transfer_all_to_machine";
    @RegisterLanguage(cn = "真空箱子模式", en = "Vacuum Hopper Mode")
    private static final String LANG_VACUUM_HOPPER_MODE = "gtocore.machine.experience_obelisk.vacuum_hopper_mode";
    @RegisterLanguage(cn = "已存储%s级经验", en = "Stored %s Levels of Experience")
    public static final String LANG_STORED_EXPERIENCE = "gtocore.machine.experience_obelisk.stored_experience";
    @RegisterLanguage(cn = "修复带有经验修补附魔的工具", en = "Repair tools with Mending enchantment")
    private static final String LANG_REPAIR_MENDING_TOOLS = "gtocore.machine.experience_obelisk.repair_mending_tools";
}
