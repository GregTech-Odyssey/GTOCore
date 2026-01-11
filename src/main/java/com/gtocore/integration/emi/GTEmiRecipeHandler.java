package com.gtocore.integration.emi;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachineKt;

import net.minecraft.world.inventory.Slot;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

import java.util.List;
import java.util.function.BiPredicate;

final class GTEmiRecipeHandler implements StandardRecipeHandler<ModularUIContainer> {

    static final List<CanCraftOverride> canCraftOverrides = new java.util.ArrayList<>();

    static void registerCanCraftOverride(BiPredicate<EmiRecipe, EmiCraftContext<ModularUIContainer>> canCraft,
                                         BiPredicate<EmiRecipe, EmiCraftContext<ModularUIContainer>> craft) {
        canCraftOverrides.add(new CanCraftOverride(canCraft, craft));
    }

    @Override
    public List<Slot> getInputSources(ModularUIContainer handler) {
        return handler.getModularUI().getSlotMap().values().stream()
                .filter(e -> e.getIngredientIO() == IngredientIO.INPUT || e.isPlayerContainer || e.isPlayerHotBar)
                .map(SlotWidget::getHandler)
                .toList();
    }

    @Override
    public List<Slot> getCraftingSlots(ModularUIContainer handler) {
        return handler.getModularUI().getSlotMap().values().stream()
                .filter(e -> e.getIngredientIO() == IngredientIO.INPUT)
                .map(SlotWidget::getHandler)
                .toList();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe instanceof GTEMIRecipe;
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<ModularUIContainer> context) {
        for (CanCraftOverride override : canCraftOverrides) {
            if (override.canCraft.test(recipe, context)) {
                return true;
            }
        }
        return StandardRecipeHandler.super.canCraft(recipe, context);
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<ModularUIContainer> context) {
        for (CanCraftOverride override : canCraftOverrides) {
            if (override.canCraft.test(recipe, context)) {
                return override.craft.test(recipe, context);
            }
        }
        return StandardRecipeHandler.super.craft(recipe, context);
    }

    public record CanCraftOverride(BiPredicate<EmiRecipe, EmiCraftContext<ModularUIContainer>> canCraft,
                                   BiPredicate<EmiRecipe, EmiCraftContext<ModularUIContainer>> craft) {}

    static {
        registerCanCraftOverride(
                (recipe, context) -> context.getScreenHandler().getModularUI().holder instanceof MEPatternBufferPartMachine,
                (recipe, context) -> {
                    if (context.getScreenHandler().getModularUI().holder instanceof MEPatternBufferPartMachine patternBuffer &&
                            patternBuffer instanceof MEPatternBufferPartMachineKt && recipe.getId() != null) {
                        var currentSlot = patternBuffer.getConfiguratorField().get();
                        MEPatternBufferPartMachineKt.Companion.getSET_ID_CHANNEL()
                                .send(buf -> {
                                    buf.writeBlockPos(patternBuffer.getPos());
                                    buf.writeVarInt(currentSlot);
                                    buf.writeResourceLocation(recipe.getId());
                                });
                        return true;
                    }
                    return false;
                });
    }
}
