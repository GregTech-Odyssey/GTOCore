package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.data.chemical.GTOChemicalHelper;
import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.FunctionContainer;
import com.gto.gtocore.utils.MachineUtils;
import com.gto.gtocore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeutronActivatorMachine extends NoEnergyMultiblockMachine implements IExplosionMachine {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NeutronActivatorMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final Item dustBeryllium = GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Beryllium);
    private static final Item dustGraphite = GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Graphite);

    int height;

    @Getter
    @Persisted
    protected int eV;

    private final ConditionalSubscriptionHandler neutronEnergySubs;

    private SensorPartMachine sensorMachine;

    private final Set<ItemBusPartMachine> busMachines = new ObjectOpenHashSet<>();

    private final Set<NeutronAcceleratorPartMachine> acceleratorMachines = new ObjectOpenHashSet<>();

    public NeutronActivatorMachine(IMachineBlockEntity holder) {
        super(holder);
        neutronEnergySubs = new ConditionalSubscriptionHandler(this, this::neutronEnergyUpdate, () -> isFormed || eV > 0);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof ItemBusPartMachine itemBusPart) {
            IO io = itemBusPart.getInventory().getHandlerIO();
            if (io == IO.IN || io == IO.BOTH) {
                busMachines.add(itemBusPart);
                for (var handler : part.getRecipeHandlers()) {
                    handler.subscribe(this::absorptionUpdate);
                }
            }
        } else if (part instanceof NeutronAcceleratorPartMachine neutronAccelerator) {
            acceleratorMachines.add(neutronAccelerator);
        } else if (part instanceof SensorPartMachine sensorPartMachine) {
            sensorMachine = sensorPartMachine;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        FunctionContainer<Integer, ?> container = getMultiblockState().getMatchContext().get("SpeedPipe");
        if (container != null) {
            height = container.getValue();
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
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        if ((eV > recipe.data.getInt("ev_min") * 1000000 && eV < recipe.data.getInt("ev_max") * 1000000)) {
            recipe = GTORecipeModifiers.hatchParallel(this, recipe);
            recipe.duration = (int) Math.round(Math.max(recipe.duration * getEfficiencyFactor(), 1));
            return recipe;
        }
        return null;
    }

    @Override
    public boolean onWorking() {
        return super.onWorking() && working();
    }

    boolean working() {
        if (getRecipeLogic().getLastRecipe() != null) {
            int evt = (int) (getRecipeLogic().getLastRecipe().data.getInt("evt") * 1000 * getEVtMultiplier());
            if (eV < evt) {
                return false;
            } else {
                eV -= evt;
            }
        }
        return true;
    }

    double getEVtMultiplier() {
        return Math.max(1, Math.pow(MachineUtils.getHatchParallel(this), 1.5) * getEfficiencyFactor());
    }

    void neutronEnergyUpdate() {
        boolean active = false;
        if (isFormed) {
            for (var accelerator : acceleratorMachines) {
                long increase = accelerator.consumeEnergy();
                if (increase > 0) {
                    active = true;
                    eV += (int) Math.round(Math.max(increase * getEfficiencyFactor(), 1));
                }
            }
            if (eV > 1200000000) doExplosion(6);
        }
        if (!active && getOffsetTimer() % 20 == 0) {
            eV = Math.max(eV - 72 * 1000, 0);
        }
        if (eV < 0) eV = 0;
        if (sensorMachine == null) return;
        sensorMachine.update((float) eV / 1000000);
        neutronEnergySubs.updateSubscription();
    }

    private void absorptionUpdate() {
        for (ItemBusPartMachine bus : busMachines) {
            var inv = bus.getInventory();
            for (int i = 0; i < inv.getSlots(); i++) {
                var stack = inv.getStackInSlot(i);
                if (stack.is(dustBeryllium) || stack.is(dustGraphite)) {
                    int consume = Math.min(Math.max(eV / (10 * 1000000), 1), stack.getCount());
                    inv.extractItemInternal(i, consume, false);
                    eV -= 10 * 1000000 * consume;
                }
            }
        }
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.neutron_activator.ev", NumberUtils.formatLong(eV)));
        textList.add(Component.translatable("gtocore.machine.neutron_activator.efficiency",
                FormattingUtil.formatNumbers(getEVtMultiplier())));
        textList.add(Component.translatable("gtocore.machine.height", height));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip",
                FormattingUtil.formatNumbers(getEfficiencyFactor() * 100)).append("%"));
    }
}
