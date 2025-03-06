package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.api.item.IItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Item.class, priority = 0)
public class ItemMixin implements IItem {

    @Unique
    private ResourceLocation gtocore$id;
    @Unique
    private String gtocore$idString;

    @Override
    public ResourceLocation gtocore$getIdLocation() {
        if (gtocore$id == null) {
            gtocore$id = ForgeRegistries.ITEMS.getKey((Item) (Object) this);
        }
        return gtocore$id;
    }

    @Override
    public String gtocore$getId() {
        if (gtocore$idString == null) {
            gtocore$idString = gtocore$getIdLocation().toString();
        }

        return gtocore$idString;
    }
}
