package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AbsoluteBaryonicPerfectionPurificationUnitMachine extends WaterPurificationUnitMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AbsoluteBaryonicPerfectionPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final List<Item> CATALYST = List.of(
            GTOItems.UP_QUARK_RELEASING_CATALYST.get(),
            GTOItems.DOWN_QUARK_RELEASING_CATALYST.get(),
            GTOItems.BOTTOM_QUARK_RELEASING_CATALYST.get(),
            GTOItems.TOP_QUARK_RELEASING_CATALYST.get(),
            GTOItems.STRANGE_QUARK_RELEASING_CATALYST.get(),
            GTOItems.CHARM_QUARK_RELEASING_CATALYST.get());

    private static final Fluid QUARK_GLUON = GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA);

    @Persisted
    @DescSynced
    private Item catalyst1;
    @Persisted
    @DescSynced
    private Item catalyst2;

    @Persisted
    private int inputCount;

    @Persisted
    private boolean successful;

    @Persisted
    private final Set<ItemStack> outputs = new HashSet<>();

    private final Set<ItemBusPartMachine> busMachines = new HashSet<>();

    public AbsoluteBaryonicPerfectionPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtocore.machine.absolute_baryonic_perfection_purification_unit.items", catalyst1.getDescription(), catalyst2.getDescription()));
            textList.add(Component.translatable("gtceu.gui.content.chance_1", successful ? 100 : 0));
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof ItemBusPartMachine itemBusPart) {
                IO io = itemBusPart.getInventory().getHandlerIO();
                if (io == IO.IN || io == IO.BOTH) {
                    busMachines.add(itemBusPart);
                }
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        busMachines.clear();
    }

    @Override
    public boolean onWorking() {
        boolean result = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            boolean successful = false;
            for (ItemBusPartMachine bus : busMachines) {
                NotifiableItemStackHandler inv = bus.getInventory();
                int slots = inv.getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (CATALYST.contains(stack.getItem()) && MachineUtil.inputFluid(this, FluidStack.create(QUARK_GLUON, stack.getCount() * 144L))) {
                        if (i < slots - 1 && stack.getItem() == catalyst1) {
                            ItemStack stack1 = inv.getStackInSlot(i + 1);
                            if (!stack1.isEmpty() && MachineUtil.inputFluid(this, FluidStack.create(QUARK_GLUON, stack1.getCount() * 144L))) {
                                if (stack1.getItem() == catalyst2) {
                                    // TODO 输出稳定重子
                                    successful = true;
                                    this.successful = true;
                                }
                                inv.setStackInSlot(i + 1, Items.AIR.getDefaultInstance());
                                if (!successful) outputs.add(stack1);
                            }
                        }
                        if (!successful) outputs.add(stack);
                        inv.setStackInSlot(i, Items.AIR.getDefaultInstance());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        outputs.forEach(i -> MachineUtil.outputItem(this, i));
        outputs.clear();
        if (successful) MachineUtil.outputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater8, inputCount * 9L / 10));
    }

    @Override
    long before() {
        eut = 0;
        successful = false;
        inputCount = (int) Math.min(Integer.MAX_VALUE, MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater7)[0]);
        recipe = GTRecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater7, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            int a = (int) (Math.random() * 5);
            int b;
            do {
                b = (int) (Math.random() * 5);
            } while (b == a);
            catalyst1 = CATALYST.get(a);
            catalyst2 = CATALYST.get(b);
            eut = inputCount * 8L;
        }
        return eut;
    }
}
