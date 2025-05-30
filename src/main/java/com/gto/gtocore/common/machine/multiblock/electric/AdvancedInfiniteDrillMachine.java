package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.common.machine.trait.AdvancedInfiniteDrillLogic;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author EasterFG on 2024/10/26
 *         <p>
 *         1.2 * heat
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class AdvancedInfiniteDrillMachine extends StorageMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AdvancedInfiniteDrillMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final FluidStack DISTILLED_WATER = GTMaterials.DistilledWater.getFluid(20000);
    private static final FluidStack OXYGEN = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 20000);
    private static final FluidStack HELIUM = GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 20000);

    private static final Map<Material, Integer> HEAT_MAP = Map.of(
            GTMaterials.Neutronium, 1);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final int RUNNING_HEAT = 2000;

    private static final int MAX_HEAT = 10000;

    @Persisted
    @Getter
    private int currentHeat = 300;

    @Persisted
    private int process;

    private final ConditionalSubscriptionHandler heatSubs;

    public AdvancedInfiniteDrillMachine(IMachineBlockEntity holder) {
        super(holder, 1, i -> ChemicalHelper.getPrefix(i.getItem()) == TagPrefix.toolHeadDrill);
        heatSubs = new ConditionalSubscriptionHandler(this, this::heatUpdate, this::isFormed);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new AdvancedInfiniteDrillLogic(this, 5);
    }

    private void heatUpdate() {
        if (getOffsetTimer() % 5 != 0) return;
        if (isEmpty()) return;
        int heat = 0;

        if (getRecipeLogic().isWorking()) {
            if (process <= 0) {
                heat += (int) Math.floor(Math.abs(currentHeat - RUNNING_HEAT) / 2000.0D);
            }
            if (MachineUtils.inputFluid(this, DISTILLED_WATER)) {
                heat--;
            } else if (MachineUtils.inputFluid(this, OXYGEN)) {
                heat -= 2;
            } else if (MachineUtils.inputFluid(this, HELIUM)) {
                heat -= 4;
            }
        }

        if (inputBlast()) {
            heat++;
        }

        currentHeat = Math.max(4, heat + currentHeat);

        if (currentHeat > MAX_HEAT) {
            process++;
            if (process >= 200) {
                process = 0;
                currentHeat = 300;
                machineStorage.setStackInSlot(0, ItemStack.EMPTY);
                getRecipeLogic().interruptRecipe();
            }
        } else if (process > 0) {
            process--;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        heatSubs.initialize(getLevel());
    }

    @Override
    public AdvancedInfiniteDrillLogic getRecipeLogic() {
        return (AdvancedInfiniteDrillLogic) super.getRecipeLogic();
    }

    @Override
    protected void customText(List<Component> textList) {
        super.customText(textList);
        if (isEmpty()) {
            textList.add(Component.translatable("gtocore.machine.advanced_infinite_driller.not_fluid_head")
                    .withStyle(ChatFormatting.RED));
        } else {
            textList.add(Component.translatable("gtceu.universal.tooltip.working_area", 5, 5));
            textList.add(Component.translatable("gtocore.machine.advanced_infinite_driller.heat", MAX_HEAT, RUNNING_HEAT));
            textList.add(Component.translatable("gtocore.machine.advanced_infinite_driller.current_heat", currentHeat));
            textList.add(Component.translatable("gtocore.machine.fission_reactor.damaged", FormattingUtil.formatNumber2Places(process / 200.0F * 100)).append("%"));
            var fluids = getRecipeLogic().getVeinFluids();
            if (!fluids.isEmpty()) {
                fluids.forEach((fluid, produced) -> {
                    Component fluidInfo = fluid.getFluidType().getDescription().copy().withStyle(ChatFormatting.GREEN);
                    Component amountInfo = Component.literal(FormattingUtil.formatNumbers(produced * getRate()) + " mB/s").withStyle(ChatFormatting.BLUE);
                    textList.add(Component.translatable("gtocore.machine.advanced_infinite_driller.drilled_fluid", fluidInfo, amountInfo));
                });
            } else {
                Component noFluid = Component.translatable("gtceu.multiblock.fluid_rig.no_fluid_in_area").withStyle(ChatFormatting.RED);
                textList.add(Component.translatable("gtceu.multiblock.fluid_rig.drilled_fluid", noFluid).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public int getRate() {
        return (int) Math.max(1, (currentHeat - RUNNING_HEAT) * getDrillHeadTier() * 0.75);
    }

    private int getDrillHeadTier() {
        ItemStack itemStack = getStorageStack();
        if (!itemStack.isEmpty()) {
            MaterialStack ms = ChemicalHelper.getMaterial(itemStack);
            if (ms != null) {
                Material material = ms.material();
                Integer result = HEAT_MAP.get(material);
                if (result != null) return result;
            }
        }
        return 0;
    }

    private boolean inputBlast() {
        return MachineUtils.inputFluid(this, GTMaterials.Blaze.getFluid(getFluidConsume()));
    }

    private int getFluidConsume() {
        return (int) Math.pow(currentHeat, 1.3);
    }

    public boolean canRunnable() {
        return currentHeat >= RUNNING_HEAT;
    }
}
