package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.api.item.IItem;
import com.gto.gtocore.utils.RLUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Item.class, priority = 0)
public class ItemMixin implements IItem {

    @Unique
    private ResourceLocation gtocore$id;

    @Override
    public @NotNull ResourceLocation gtocore$getIdLocation() {
        if (gtocore$id == null) {
            gtocore$id = ForgeRegistries.ITEMS.getKey((Item) (Object) this);
            if (gtocore$id == null) gtocore$id = RLUtils.mc("air");
        }
        return gtocore$id;
    }
}
