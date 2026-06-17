package com.gtocore.mixin.ae2.stacks;

import com.gtolib.api.item.IItem;
import com.gtolib.api.recipe.lookup.IIngredientConvertible;
import com.gtolib.api.recipe.lookup.MapIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;

import com.fast.recipesearch.IntLongMap;
import org.spongepowered.asm.mixin.*;

@Mixin(AEItemKey.class)
public abstract class AEItemKeyMixin implements IIngredientConvertible {

    @Shadow(remap = false)
    public abstract ItemStack getReadOnlyStack();

    @Shadow(remap = false)
    @Final
    private Item item;

    @Unique
    private int[] gtocore$is;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ResourceLocation getId() {
        return ((IItem) item).gtolib$getIdLocation();
    }

    @Override
    public void gtolib$convert(long amount, IntLongMap map) {
        if (gtocore$is == null) {
            var m = new IntLongMap();
            MapIngredient.ITEM_CONVERTER.convert(getReadOnlyStack(), 1, m);
            gtocore$is = m.toIntArray();
        }
        for (var i : gtocore$is) {
            map.add(i, amount);
        }
    }
}
