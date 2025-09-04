package com.gtocore.mixin.ae2.pattern;

import com.gtolib.api.ae2.pattern.IDetails;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.AECraftingPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AECraftingPattern.class)
public abstract class AECraftingPatternMixin implements IDetails {

    @Shadow(remap = false)
    @Final
    public boolean canSubstituteFluids;

    @Shadow(remap = false)
    @Final
    public static int CRAFTING_GRID_SLOTS;

    @Shadow(remap = false)
    @Final
    private CraftingRecipe recipe;

    @Shadow(remap = false)
    @Final
    private CraftingContainer testFrame;

    @Shadow(remap = false)
    public GenericStack getValidFluid(int slot) {
        return null;
    }

    @Unique
    private KeyCounter[] gtolib$inputHolder;

    @Override
    public KeyCounter[] gtolib$getInputHolder() {
        if (gtolib$inputHolder == null) {
            var length = getInputs().length;
            gtolib$inputHolder = new KeyCounter[length];
            for (int i = 0; i < length; i++) {
                gtolib$inputHolder[i] = new KeyCounter();
            }
        }
        return gtolib$inputHolder;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(AEItemKey definition, Level level, CallbackInfo ci) {
        NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(testFrame);
        if (!canSubstituteFluids) {
            for (ItemStack stack : remainingItems) {
                if (stack.isEmpty()) continue;
                throw new IllegalStateException("The recipe " + recipe + " contain remaining items.");
            }
        } else {
            for (int i = 0; i < CRAFTING_GRID_SLOTS; i++) {
                var item = remainingItems.get(i);
                if (item.isEmpty()) continue;
                var fluid = getValidFluid(i);
                if (fluid == null) {
                    throw new IllegalStateException("The recipe " + recipe + " contain remaining items.");
                }
            }
        }
    }
}
