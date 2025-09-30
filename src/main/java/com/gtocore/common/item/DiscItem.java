package com.gtocore.common.item;

import com.gtocore.api.placeholder.IPlaceholderEXKT;
import com.gtocore.api.placeholder.ItemStackFluidStack;
import com.gtolib.IItem;
import com.gtolib.api.recipe.lookup.MapIngredient;
import com.gtolib.utils.FluidUtils;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.RLUtils;
import kotlin.Unit;
import net.minecraft.nbt.CompoundTag;
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class DiscItem extends Item implements IPlaceholderEXKT<ItemStackFluidStack, ItemStack, Unit> {

    public DiscItem(Properties properties) {
        super(properties);
    }

    private static final ItemStackFluidStack EMPTY_CONTENT = new ItemStackFluidStack.Item(ItemStack.EMPTY);

    public ItemStack getDisc(ItemStack itemStack) {
        ItemStack stack = getDefaultInstance();
        ResourceLocation id = ItemUtils.getIdLocation(itemStack.getItem());
        stack.getOrCreateTag().putString("i", id.getNamespace());
        stack.getOrCreateTag().putString("n", id.getPath());
        MapIngredient.add(id.getPath());
        return stack;
    }

    public ItemStack getDisc(Fluid fluid) {
        ItemStack stack = getDefaultInstance();
        ResourceLocation id = FluidUtils.getIdLocation(fluid);
        stack.getOrCreateTag().putString("f", id.getNamespace());
        stack.getOrCreateTag().putString("n", id.getPath());
        MapIngredient.add(id.getPath());
        return stack;
    }

    /**
     * 从光盘ItemStack中读取并返回存储的Item。
     *
     * @param stack 要读取的光盘ItemStack
     * @return 一个包含Item的Optional，如果不存在或无效则为空
     */
    private Optional<Item> getStoredItem(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return Optional.empty();
        }

        String namespace = tag.getString("i");
        if (!namespace.isEmpty()) {
            ResourceLocation id = RLUtils.fromNamespaceAndPath(namespace, tag.getString("n"));
            // Optional.ofNullable 会处理 getValue 可能返回 null 的情况
            return Optional.ofNullable(ForgeRegistries.ITEMS.getValue(id));
        }

        return Optional.empty();
    }

    /**
     * 从光盘ItemStack中读取并返回存储的Fluid。
     *
     * @param stack 要读取的光盘ItemStack
     * @return 一个包含Fluid的Optional，如果不存在或无效则为空
     */
    private Optional<Fluid> getStoredFluid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return Optional.empty();
        }

        String namespace = tag.getString("f");
        if (!namespace.isEmpty()) {
            ResourceLocation id = RLUtils.fromNamespaceAndPath(namespace, tag.getString("n"));
            return Optional.ofNullable(ForgeRegistries.FLUIDS.getValue(id));
        }

        return Optional.empty();
    }

    @NotNull
    private ItemStackFluidStack getStoredContent(ItemStack discStack) {
        Optional<Item> itemOpt = getStoredItem(discStack);
        if (itemOpt.isPresent()) {
            ItemStack displayStack = ((IItem) itemOpt.get()).gtolib$getReadOnlyStack();
            return new ItemStackFluidStack.Item(displayStack);
        }

        Optional<Fluid> fluidOpt = getStoredFluid(discStack);
        if (fluidOpt.isPresent()) {
            return new ItemStackFluidStack.Fluid(new FluidStack(fluidOpt.get(), 1));
        }

        // 如果上面都没有返回，说明光盘是空的，返回我们的“空对象”实例。
        return EMPTY_CONTENT;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        Optional<Item> storedItem = getStoredItem(itemstack);
        if (storedItem.isPresent()) {
            list.add(Component.translatable("item.gtocore.disc.data", ((IItem) storedItem.get()).gtolib$getReadOnlyStack().getDisplayName()));
        } else {
            getStoredFluid(itemstack).ifPresent(fluid ->
                    list.add(Component.translatable("item.gtocore.disc.data", "[" + new FluidStack(fluid, 1).getDisplayName().getString() + "]"))
            );
        }
    }

    @NotNull
    @Override
    public List<List<ItemStackFluidStack>> getTargetLists(@NotNull ItemStack sourceDisc) {
        ItemStackFluidStack content = getStoredContent(sourceDisc);
        if (content.equals(EMPTY_CONTENT)) {
            return Collections.emptyList();
        }
        return List.of(List.of(content));
    }

    @Nullable
    @Override
    public ItemStackFluidStack getCurrentTarget(@NotNull ItemStack sourceDisc, Unit context) {
        ItemStackFluidStack content = getStoredContent(sourceDisc);
        if (content.equals(EMPTY_CONTENT)) {
            return null;
        }
        return content;
    }
}
