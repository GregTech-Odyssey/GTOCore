package com.gto.gtocore.common.item;

import com.gto.gtocore.utils.FluidUtils;
import com.gto.gtocore.utils.ItemUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Nullable;

public final class DiscItem extends Item {

    public DiscItem(Properties properties) {
        super(properties);
    }

    public ItemStack getDisc(ItemStack itemStack) {
        ItemStack stack = getDefaultInstance();
        ResourceLocation id = ItemUtils.getIdLocation(itemStack.getItem());
        stack.getOrCreateTag().putString("i", id.getNamespace());
        stack.getOrCreateTag().putString("n", id.getPath());
        CompoundTag tag = itemStack.getTag();
        if (tag != null) stack.getOrCreateTag().put("t", tag);
        return stack;
    }

    public ItemStack getDisc(Fluid fluid) {
        ItemStack stack = getDefaultInstance();
        ResourceLocation id = FluidUtils.getIdLocation(fluid);
        stack.getOrCreateTag().putString("f", id.getNamespace());
        stack.getOrCreateTag().putString("n", id.getPath());
        return stack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        CompoundTag tag = itemstack.getTag();
        if (tag != null) {
            String i = tag.getString("i");
            if (!i.isEmpty()) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(i, tag.getString("n")));
                if (item != null) {
                    ItemStack stack = new ItemStack(item);
                    Tag nbt = tag.get("t");
                    if (nbt != null) {
                        stack.setTag((CompoundTag) nbt);
                    }
                    list.add(Component.translatable("item.gtocore.disc.data", stack.getDisplayName()));
                }
            } else {
                String f = tag.getString("f");
                if (!f.isEmpty()) {
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(f, tag.getString("n")));
                    if (fluid != null) {
                        list.add(Component.translatable("item.gtocore.disc.data", "[" + new FluidStack(fluid, 1).getDisplayName().getString() + "]"));
                    }
                }
            }
        }
    }
}
