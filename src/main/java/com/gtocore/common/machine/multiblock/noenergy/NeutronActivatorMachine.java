package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;
import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.api.gui.MagicProgressBarProWidget;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.FunctionContainer;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.NumberUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeutronActivatorMachine extends NoEnergyMultiblockMachine implements IExplosionMachine {

    private static final Item dustBeryllium = ChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Beryllium);
    private static final Item dustGraphite = ChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Graphite);
    int height;
    @Persisted
    protected int eV;
    private final ConditionalSubscriptionHandler neutronEnergySubs;
    private SensorPartMachine sensorMachine;
    private final List<ItemBusPartMachine> busMachines = new ObjectArrayList<>(2);
    private final List<NeutronAcceleratorPartMachine> acceleratorMachines = new ObjectArrayList<>(2);

    public NeutronActivatorMachine(MetaMachineBlockEntity holder) {
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
                    traitSubscriptions.add(handler.subscribe(this::absorptionUpdate));
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
        acceleratorMachines.clear();
        busMachines.clear();
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
    protected Recipe getRealRecipe(Recipe recipe) {
        if ((eV > recipe.data.getInt("ev_min") * 1000000 && eV < recipe.data.getInt("ev_max") * 1000000)) {
            recipe = RecipeModifierFunction.hatchParallel(this, recipe);
            if (recipe == null) return null;
            recipe.duration = (int) Math.round(Math.max(recipe.duration * getEfficiencyFactor(), 1));
            return recipe;
        }
        setIdleReason(IdleReason.NEUTRON_KINETIC_ENERGY_NOT_SATISFIES);
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
                setIdleReason(IdleReason.NEUTRON_KINETIC_ENERGY_NOT_SATISFIES);
                return false;
            } else {
                eV -= evt;
            }
        }
        return true;
    }

    double getEVtMultiplier() {
        return Math.max(1, Math.pow(MachineUtils.getHatchParallel(this), 1.2) * getEfficiencyFactor());
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
        if (getOffsetTimer() % 20 == 0) {
            getRecipeLogic().updateTickSubscription();
            if (!active) eV = Math.max(eV - 72 * 1000, 0);
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

    private final MagicProgressBarProWidget progressBarPro = new MagicProgressBarProWidget(0, 1400000000).addStartColor(-16711936).addMilestone(1200000000, -65536, Component.translatable("gtocore.bar.exploration")).setLeftLabel("eV");

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return super.createUI(entityPlayer).widget(progressBarPro);
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.neutron_activator.ev", NumberUtils.formatLong(eV)));
        textList.add(Component.translatable("gtocore.machine.neutron_activator.efficiency", FormattingUtil.formatNumbers(getEVtMultiplier())));
        textList.add(Component.translatable("gtocore.machine.height", height));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(getEfficiencyFactor() * 100)).append("%"));
        progressBarPro.setProgressSupplier(() -> eV);
    }

    public int getEV() {
        return this.eV;
    }
}
