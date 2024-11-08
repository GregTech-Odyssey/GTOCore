package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.multiblock.WorkableNoEnergyMultiblockMachine;
import com.gto.gtocore.api.pattern.util.IValueContainer;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeutronActivatorMachine extends WorkableNoEnergyMultiblockMachine implements IExplosionMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NeutronActivatorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final Item dustBeryllium = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Beryllium).getItem();
    private static final Item dustGraphite = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Graphite).getItem();

    private int height = 0;

    @Getter
    @Persisted
    @DescSynced
    private int eV;

    private final ConditionalSubscriptionHandler neutronEnergySubs;

    private SensorPartMachine sensorMachine;

    private final Set<ItemBusPartMachine> busMachines = new HashSet<>();

    private final Set<NeutronAcceleratorPartMachine> acceleratorMachines = new HashSet<>();

    public NeutronActivatorMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.neutronEnergySubs = new ConditionalSubscriptionHandler(this, this::neutronEnergyUpdate, this::isFormed);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IValueContainer<?> container = getMultiblockState().getMatchContext()
                .getOrCreate("SpeedPipeValue", IValueContainer::noop);
        if (container.getValue() instanceof Integer integer) {
            height = integer;
        }
        for (IMultiPart part : getParts()) {
            if (part instanceof ItemBusPartMachine itemBusPart) {
                IO io = itemBusPart.getInventory().getHandlerIO();
                if (io == IO.IN || io == IO.BOTH) {
                    busMachines.add(itemBusPart);
                }
            } else if (part instanceof NeutronAcceleratorPartMachine neutronAccelerator) {
                acceleratorMachines.add(neutronAccelerator);
            } else if (part instanceof SensorPartMachine sensorPartMachine) {
                sensorMachine = sensorPartMachine;
            }
        }
        neutronEnergySubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        height = 0;
        sensorMachine = null;
        busMachines.clear();
        acceleratorMachines.clear();
    }

    private double getEfficiencyFactor() {
        return Math.pow(0.95, Math.max(height - 4, 0));
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
        if (machine instanceof NeutronActivatorMachine nMachine &&
                (nMachine.eV > recipe.data.getInt("ev_min") * 1000000 &&
                        nMachine.eV < recipe.data.getInt("ev_max") * 1000000)) {
            GTRecipe recipe1 = GTRecipeModifiers.accurateParallel(machine, recipe, GTORecipeModifiers.getHatchParallel(nMachine), false)
                    .getFirst();
            recipe1.duration = (int) Math.round(Math.max(recipe1.duration * nMachine.getEfficiencyFactor(), 1));
            return recipe1;
        }
        return null;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe != null) {
            return eV >= recipe.data.getInt("evt") * 1000 * getEVtMultiplier();
        }
        return false;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (getRecipeLogic().getLastRecipe() != null) {
            int evt = (int) (getRecipeLogic().getLastRecipe().data.getInt("evt") *
                    1000 * getEVtMultiplier());
            if (eV < evt) {
                return false;
            } else {
                eV -= evt;
            }
        }
        return value;
    }

    private double getEVtMultiplier() {
        return Math.max(1, Math.pow(GTORecipeModifiers.getHatchParallel(this), 1.5) * getEfficiencyFactor());
    }

    private void neutronEnergyUpdate() {
        for (var accelerator : acceleratorMachines) {
            long increase = accelerator.consumeEnergy();
            if (increase > 0) {
                this.eV += (int) Math.round(Math.max(increase * getEfficiencyFactor(), 1));
            }
        }
        if (this.eV > 1200000000) doExplosion(6);
        moderateUpdate();
        absorptionUpdate();
    }

    private void moderateUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            this.eV = Math.max(eV - 72 * 1000, 0);
        }
        if (this.eV < 0) this.eV = 0;
        if (sensorMachine == null) return;
        sensorMachine.update((float) eV / 1000000);
    }

    private void absorptionUpdate() {
        if (eV <= 0 && getOffsetTimer() % 10 != 0) return;
        for (ItemBusPartMachine bus : busMachines) {
            var inv = bus.getInventory();
            for (int i = 0; i < inv.getSlots(); i++) {
                var stack = inv.getStackInSlot(i);
                if (stack.is(dustBeryllium) || stack.is(dustGraphite)) {
                    int consume = Math.min(Math.max(eV / (10 * 1000000), 1), stack.getCount());
                    inv.extractItemInternal(i, consume, false);
                    this.eV -= 10 * 1000000 * consume;
                }
            }
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("gtocore.machine.neutron_activator.ev", NumberUtils.formatLong(eV)));
            textList.add(Component.translatable("gtocore.machine.neutron_activator.efficiency",
                    FormattingUtil.formatNumbers(getEVtMultiplier())));
            textList.add(Component.translatable("gtocore.machine.neutron_activator.height", height));
            textList.add(Component.translatable("gtocore.machine.neutron_activator.time",
                    FormattingUtil.formatNumbers(getEfficiencyFactor() * 100)).append("%"));
        }
    }
}
