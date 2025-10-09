package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.ae2.IPatternProviderLogic;
import com.gtolib.api.ae2.PatternProviderTargetCache;
import com.gtolib.api.ae2.machine.ICustomCraftingMachine;
import com.gtolib.api.ae2.pattern.IDetails;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.utils.holder.BooleanHolder;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@DataGeneratorScanned
public class ProgrammableHatchPartMachine extends DualHatchPartMachine implements IProgrammableMachine, ICustomCraftingMachine {

    @RegisterLanguage(cn = "切换配方类型[%s]", en = "Switch recipe type [%s]")
    private final static String SWITCH_TYPE = "gtocore.machine.switch_type";

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ProgrammableHatchPartMachine.class, DualHatchPartMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private boolean switchType = false;

    public ProgrammableHatchPartMachine(MetaMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
    }

    @Override
    protected @NotNull NotifiableItemStackHandler createInventory(Object @NotNull... args) {
        return new NotifiableItemStackHandler(this, getInventorySize(), io).setFilter(itemStack -> !(itemStack.hasTag() && itemStack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())));
    }

    @Override
    protected @NotNull NotifiableItemStackHandler createCircuitItemHandler(Object... args) {
        if (args.length > 0 && args[0] instanceof IO io && io == IO.IN) {
            return new ProgrammableCircuitHandler(this);
        } else {
            return NotifiableItemStackHandler.empty(this);
        }
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0.5, 1, 0.5), GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0, 1, 0.5), () -> switchType, (clickData, pressed) -> switchType = pressed).setTooltipsSupplier(pressed -> List.of(Component.translatable(SWITCH_TYPE, Component.translatable(pressed ? "gtocore.machine.on" : "gtocore.machine.off")))));
    }

    @Override
    public boolean customPush() {
        return switchType;
    }

    @Override
    public IPatternProviderLogic.PushResult pushPattern(IPatternProviderLogic logic, IActionSource actionSource, BooleanHolder success, Operate operate, Set<AEKey> patternInputs, IPatternDetails patternDetails, ObjectHolder<KeyCounter[]> inputHolder, Supplier<IPatternProviderLogic.PushResult> pushPatternSuccess, BooleanSupplier canPush, Direction direction, Direction adjBeSide) {
        var target = PatternProviderTargetCache.find(holder, logic, adjBeSide, actionSource, 0);
        if (target == null || target.containsPatternInput(patternInputs)) return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
        var result = operate.pushTarget(patternDetails, inputHolder, pushPatternSuccess, canPush, direction, target, true);
        if (result.success()) {
            success.value = true;
            if (patternDetails instanceof IDetails details) {
                var type = details.getRecipeType();
                if (type != null) {
                    getHandlerList().external.recipeType = type;
                }
            }
        }
        if (result.needBreak()) return result;
        return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
    }

    @Override
    public boolean isProgrammable() {
        return true;
    }

    @Override
    public void setProgrammable(boolean programmable) {}

    public static class ProgrammableCircuitHandler extends CircuitHandler {

        public ProgrammableCircuitHandler(MetaMachine machine) {
            super(machine, IO.IN, s -> new ProgrammableHandler(machine));
        }

        private static class ProgrammableHandler extends ItemStackHandler {

            private final IProgrammableMachine machine;

            private ProgrammableHandler(Object machine) {
                super(1);
                this.machine = machine instanceof IProgrammableMachine programmableMachine ? programmableMachine : null;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (machine.isProgrammable() && stack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())) {
                    setStackInSlot(slot, VirtualItemProviderBehavior.getVirtualItem(stack));
                    return ItemStack.EMPTY;
                }
                return stack;
            }
        }
    }
}
