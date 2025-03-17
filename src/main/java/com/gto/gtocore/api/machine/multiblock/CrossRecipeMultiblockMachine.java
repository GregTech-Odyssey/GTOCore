package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.gui.ParallelConfigurator;
import com.gto.gtocore.api.machine.feature.multiblock.IOverclockConfigMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.trait.CustomParallelTrait;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.AsyncCrossRecipeSearchTask;
import com.gto.gtocore.api.recipe.AsyncRecipeSearchTask;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.part.ThreadHatchPartMachine;
import com.gto.gtocore.utils.ItemUtils;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class CrossRecipeMultiblockMachine extends ElectricMultiblockMachine implements IParallelMachine, IOverclockConfigMachine {

    public static CrossRecipeMultiblockMachine createHatchParallel(IMachineBlockEntity holder) {
        return new CrossRecipeMultiblockMachine(holder, false, true, MachineUtils::getHatchParallel);
    }

    public static Function<IMachineBlockEntity, CrossRecipeMultiblockMachine> createParallel(boolean infinite, boolean isHatchParallel, Function<CrossRecipeMultiblockMachine, Integer> parallel) {
        return holder -> new CrossRecipeMultiblockMachine(holder, infinite, isHatchParallel, parallel);
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CrossRecipeMultiblockMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    @Getter
    private final Set<GTRecipe> originRecipes = new ObjectOpenHashSet<>();
    @Persisted
    @Getter
    private final Set<GTRecipe> lastRecipes = new ObjectOpenHashSet<>();

    @Persisted
    @Getter
    @Setter
    private boolean jadeInfo;

    private ThreadHatchPartMachine threadHatchPartMachine;

    @Persisted
    private final CustomParallelTrait customParallelTrait;
    private final boolean infinite;
    private final boolean isHatchParallel;

    protected CrossRecipeMultiblockMachine(IMachineBlockEntity holder, boolean infinite, boolean isHatchParallel, @NotNull Function<CrossRecipeMultiblockMachine, Integer> parallel) {
        super(holder);
        this.infinite = infinite;
        this.isHatchParallel = isHatchParallel;
        customParallelTrait = new CustomParallelTrait(this, false, machine -> parallel.apply((CrossRecipeMultiblockMachine) machine));
    }

    public int getThread() {
        return infinite ? 128 : threadHatchPartMachine == null ? 1 : threadHatchPartMachine.getCurrentThread();
    }

    public boolean isRepeatedRecipes() {
        if (threadHatchPartMachine == null) return true;
        return threadHatchPartMachine.isRepeatedRecipes();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        if (!isHatchParallel) configuratorPanel.attachConfigurators(new ParallelConfigurator(this));
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GuiTextures.BUTTON_SILK_TOUCH_MODE.getSubTexture(0, 0, 1, 0.5),
                GuiTextures.BUTTON_SILK_TOUCH_MODE.getSubTexture(0, 0.5, 1, 0.5),
                this::isJadeInfo, (clickData, pressed) -> this.setJadeInfo(pressed))
                .setTooltipsSupplier(pressed -> List.of(Component.translatable("gtceu.top.recipe_output").append(Component.translatable(pressed ? "waila.ae2.Showing" : "config.jade.display_fluids_none")))));
    }

    private GTRecipe findAndHandleRecipe() {
        if (getRecipeLogic().gtocore$getAsyncRecipeSearchTask() instanceof AsyncCrossRecipeSearchTask task && (task.isNext() || task.isHasTask())) {
            task.setNext(false);
            if (task.getResult() != null) {
                if (task.getResult() instanceof AsyncCrossRecipeSearchTask.Result result && result.recipe() != null) {
                    lastRecipes.addAll(result.lastRecipes());
                    GTRecipe recipe = result.recipe();
                    task.setNext(true);
                    task.clean();
                    return recipe;
                }
                task.clean();
            }
        } else {
            AsyncRecipeSearchTask.addAsyncLogic(getRecipeLogic());
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CrossRecipeLogic(this, this::findAndHandleRecipe);
    }

    @NotNull
    @Override
    public CrossRecipeLogic getRecipeLogic() {
        return (CrossRecipeLogic) super.getRecipeLogic();
    }

    public GTRecipe modifyRecipe(GTRecipe recipe) {
        return GTORecipeModifiers.accurateParallel(this, recipe, isHatchParallel ? getMaxParallel() : getParallel());
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!isFormed()) return;
        if (!isActive()) return;
        for (GTRecipe recipe : lastRecipes) {
            textList.add(Component.translatable("gtceu.top.recipe_output"));
            int recipeTier = RecipeHelper.getPreOCRecipeEuTier(recipe);
            int chanceTier = recipeTier + recipe.ocLevel;
            var function = recipe.getType().getChanceFunction();
            double maxDurationSec = (double) recipe.duration / 20.0;
            var itemOutputs = recipe.getOutputContents(ItemRecipeCapability.CAP);
            var fluidOutputs = recipe.getOutputContents(FluidRecipeCapability.CAP);
            for (var item : itemOutputs) {
                ItemStack stack = ItemUtils.getFirst(ItemRecipeCapability.CAP.of(item.content));
                if (stack.isEmpty()) continue;
                int count = stack.getCount();
                double countD = count;
                if (item.chance < item.maxChance) {
                    countD = countD * recipe.parallels *
                            function.getBoostedChance(item, recipeTier, chanceTier) / item.maxChance;
                    count = countD < 1 ? 1 : (int) Math.round(countD);
                }
                if (count < maxDurationSec) {
                    String key = "gtceu.multiblock.output_line." + (item.chance < item.maxChance ? "2" : "0");
                    textList.add(Component.translatable(key, stack.getHoverName(), count,
                            FormattingUtil.formatNumber2Places(maxDurationSec / countD)));
                } else {
                    String key = "gtceu.multiblock.output_line." + (item.chance < item.maxChance ? "3" : "1");
                    textList.add(Component.translatable(key, stack.getHoverName(), count,
                            FormattingUtil.formatNumber2Places(countD / maxDurationSec)));
                }
            }
            for (var fluid : fluidOutputs) {
                var stacks = FluidRecipeCapability.CAP.of(fluid.content).getStacks();
                if (stacks.length == 0) continue;
                var stack = stacks[0];
                int amount = stack.getAmount();
                double amountD = amount;
                if (fluid.chance < fluid.maxChance) {
                    amountD = amountD * recipe.parallels *
                            function.getBoostedChance(fluid, recipeTier, chanceTier) / fluid.maxChance;
                    amount = amountD < 1 ? 1 : (int) Math.round(amountD);
                }
                if (amount < maxDurationSec) {
                    String key = "gtceu.multiblock.output_line." + (fluid.chance < fluid.maxChance ? "2" : "0");
                    textList.add(Component.translatable(key, stack.getDisplayName(), amount,
                            FormattingUtil.formatNumber2Places(maxDurationSec / amountD)));
                } else {
                    String key = "gtceu.multiblock.output_line." + (fluid.chance < fluid.maxChance ? "3" : "1");
                    textList.add(Component.translatable(key, stack.getDisplayName(), amount,
                            FormattingUtil.formatNumber2Places(amountD / maxDurationSec)));
                }
            }
        }
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (threadHatchPartMachine == null && part instanceof ThreadHatchPartMachine threadHatchPart) {
            threadHatchPartMachine = threadHatchPart;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        threadHatchPartMachine = null;
    }

    @Override
    public int getMaxParallel() {
        return customParallelTrait.getMaxParallel();
    }

    @Override
    public int getParallel() {
        if (isHatchParallel) return 0;
        return customParallelTrait.getParallel();
    }

    @Override
    public void setParallel(int number) {
        customParallelTrait.setParallel(number);
    }

    public static class CrossRecipeLogic extends CustomRecipeLogic {

        CrossRecipeLogic(IRecipeLogicMachine machine, Supplier<GTRecipe> recipeSupplier) {
            super(machine, recipeSupplier);
        }

        @Override
        public AsyncRecipeSearchTask gtocore$createAsyncRecipeSearchTask() {
            return new AsyncCrossRecipeSearchTask(getLogic());
        }

        @Override
        public CrossRecipeMultiblockMachine getMachine() {
            return (CrossRecipeMultiblockMachine) super.getMachine();
        }

        @Override
        public boolean canLockRecipe() {
            return true;
        }

        @Override
        public void gTOCore$setLockRecipe(boolean look) {
            super.gTOCore$setLockRecipe(look);
            getMachine().originRecipes.clear();
        }

        @Override
        public void onRecipeFinish() {
            for (GTRecipe recipe : getMachine().lastRecipes) {
                handleRecipeIO(recipe, IO.OUT);
            }
            getMachine().lastRecipes.clear();
            super.onRecipeFinish();
        }
    }
}
