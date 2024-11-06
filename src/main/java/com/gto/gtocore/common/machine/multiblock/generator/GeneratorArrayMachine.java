package com.gto.gtocore.common.machine.multiblock.generator;

import com.gto.gtocore.api.machine.multiblock.StorageMachine;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GeneratorArrayMachine extends StorageMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            GeneratorArrayMachine.class, StorageMachine.MANAGED_FIELD_HOLDER);

    @Nullable
    private GTRecipeType[] recipeTypeCache;

    @Persisted
    private UUID userid;

    @Persisted
    private boolean isw;

    @Persisted
    private long eut = 0;

    public GeneratorArrayMachine(IMachineBlockEntity holder) {
        super(holder, 4, GeneratorArrayMachine::filter);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static boolean filter(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();

            if (definition instanceof MultiblockMachineDefinition) {
                return false;
            }

            var recipeTypes = definition.getRecipeTypes();
            if (recipeTypes == null) {
                return false;
            }
            for (GTRecipeType type : recipeTypes) {
                if (type == GTRecipeTypes.STEAM_TURBINE_FUELS || type == GTRecipeTypes.GAS_TURBINE_FUELS || type == GTRecipeTypes.COMBUSTION_GENERATOR_FUELS || type == GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS || type == GTORecipeTypes.ROCKET_ENGINE_FUELS || type == GTORecipeTypes.NAQUADAH_REACTOR) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public MachineDefinition getMachineDefinition() {
        if (machineStorage.storage.getStackInSlot(0).getItem() instanceof MetaMachineItem metaMachineItem) {
            return metaMachineItem.getDefinition();
        }
        return null;
    }

    @Override
    public GTRecipeType[] getRecipeTypes() {
        if (recipeTypeCache == null) {
            var definition = getMachineDefinition();
            recipeTypeCache = definition == null ? new GTRecipeType[] { GTRecipeTypes.DUMMY_RECIPES } : definition.getRecipeTypes();
        }
        return recipeTypeCache;
    }

    @NotNull
    @Override
    public GTRecipeType getRecipeType() {
        return getRecipeTypes()[getActiveRecipeType()];
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            machineStorage.addChangedListener(this::onMachineChanged);
        }
    }

    protected void onMachineChanged() {
        recipeTypeCache = null;
        if (isFormed) {
            if (getRecipeLogic().getLastRecipe() != null) {
                getRecipeLogic().markLastRecipeDirty();
            }
            getRecipeLogic().updateTickSubscription();
        }
    }

    //////////////////////////////////////
    // ******* Recipe Logic *******//
    //////////////////////////////////////

    @Override
    public int getTier() {
        MachineDefinition machineDefinition = getMachineDefinition();
        return machineDefinition == null ? 0 : machineDefinition.getTier();
    }

    @Override
    public int getOverclockTier() {
        MachineDefinition machineDefinition = getMachineDefinition();
        return machineDefinition == null ? getDefinition().getTier() : machineDefinition.getTier();
    }

    @Override
    public int getMinOverclockTier() {
        return getOverclockTier();
    }

    @Override
    public int getMaxOverclockTier() {
        return getOverclockTier();
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.userid == null || !this.userid.equals(player.getUUID())) {
            this.userid = player.getUUID();
        }
        return true;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (this.isw) {
            if (eut > 0) {
                WirelessEnergyManager.addEUToGlobalEnergyMap(userid, eut, this);
            } else {
                return false;
            }
        }
        return value;
    }

    @Override
    public void afterWorking() {
        eut = 0;
        super.afterWorking();
    }

    @Override
    public boolean dampingWhenWaiting() {
        return false;
    }

    public static int getAmperage(int tier) {
        if (tier == 0) return 1;
        if (tier > GTValues.HV) {
            return 8 - tier;
        }
        return 10 - (2 * tier);
    }

    public static int getEfficiency(GTRecipeType recipeType, int tier) {
        if (recipeType == GTRecipeTypes.STEAM_TURBINE_FUELS) {
            return (135 - 35 * tier);
        }
        if (recipeType == GTORecipeTypes.ROCKET_ENGINE_FUELS) {
            return (80 - 15 * (tier - GTValues.EV));
        }
        if (recipeType == GTORecipeTypes.NAQUADAH_REACTOR) {
            return (100 + 50 * (tier - GTValues.IV));
        }
        return (105 - 5 * tier);
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof GeneratorArrayMachine generatorArrayMachine) {
            int a = generatorArrayMachine.machineStorage.storage.getStackInSlot(0).getCount();
            if (a > 0) {
                long EUt = RecipeHelper.getOutputEUt(recipe);
                if (EUt > 0) {
                    int maxParallel = (int) ((generatorArrayMachine.getTier() > 6 ? 0.5 : generatorArrayMachine.getTier() > 3 ? 1 : 2) * GTValues.V[generatorArrayMachine.getOverclockTier()] * a * getAmperage(generatorArrayMachine.getTier()) / EUt);
                    int multipliers = 0;
                    for (RecipeCapability<?> cap : recipe.inputs.keySet()) {
                        if (cap instanceof FluidRecipeCapability fluidRecipeCapability) {
                            multipliers += fluidRecipeCapability.getMaxParallelRatio(generatorArrayMachine, recipe, maxParallel);
                        }
                    }
                    GTRecipe paraRecipe = recipe.copy(ContentModifier.multiplier(multipliers), false);
                    paraRecipe.duration = paraRecipe.duration * getEfficiency(generatorArrayMachine.getRecipeType(), generatorArrayMachine.getTier()) / 100;
                    if (generatorArrayMachine.isw) {
                        generatorArrayMachine.eut = RecipeHelper.getOutputEUt(paraRecipe);
                        paraRecipe.tickOutputs.remove(EURecipeCapability.CAP);
                    }
                    return paraRecipe;
                }
            }
        }
        return null;
    }

    //////////////////////////////////////
    // ******** Gui ********//
    //////////////////////////////////////

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        textList.add(Component.translatable("gtocore.machine.generator_array.wireless")
                .append(ComponentPanelWidget.withButton(Component.literal("[")
                        .append(this.isw ?
                                Component.translatable("gtocore.machine.on") :
                                Component.translatable("gtocore.machine.off"))
                        .append(Component.literal("]")), "wireless_switch")));
        if (isActive() && this.isw) {
            GTRecipe r = getRecipeLogic().getLastRecipe();
            if (r != null) {
                textList.add(Component.translatable("gtceu.multiblock.max_energy_per_tick", FormattingUtil.formatNumbers(eut), Component.literal(GTValues.VNF[GTUtil.getFloorTierByVoltage(eut)])));
            }
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if (componentData.equals("wireless_switch")) {
                this.isw = !this.isw;
            }
        }
    }
}
