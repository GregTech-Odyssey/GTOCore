package com.gto.gtocore.api.recipe;

import com.gto.gtocore.utils.EmptyStream;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.NBTIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FastSizedIngredient extends Ingredient {

    private static final EmptyStream<Ingredient.Value> EMPTY_STREAM = EmptyStream.create(new Ingredient.Value[0]);

    @Getter
    private int amount;
    @Getter
    private final Ingredient inner;
    private ItemStack[] itemStacks = null;

    private FastSizedIngredient(Ingredient inner, int amount) {
        super(EMPTY_STREAM);
        this.amount = amount;
        this.inner = inner;
    }

    private FastSizedIngredient(@NotNull TagKey<Item> tag, int amount) {
        this(Ingredient.of(tag), amount);
    }

    private FastSizedIngredient(ItemStack itemStack) {
        this(itemStack.hasTag() ? NBTIngredient.createNBTIngredient(itemStack) : Ingredient.of(itemStack), itemStack.getCount());
    }

    public static FastSizedIngredient create(ItemStack inner) {
        return new FastSizedIngredient(inner);
    }

    public static FastSizedIngredient create(Ingredient inner, int amount) {
        return new FastSizedIngredient(inner, amount);
    }

    public static FastSizedIngredient create(Ingredient inner) {
        return new FastSizedIngredient(inner, 1);
    }

    public static FastSizedIngredient create(TagKey<Item> tag, int amount) {
        return new FastSizedIngredient(tag, amount);
    }

    public static Ingredient copy(Ingredient ingredient) {
        if (ingredient instanceof FastSizedIngredient fastSizedIngredient) {
            Ingredient innerIngredient = ItemUtils.getSizedInner(fastSizedIngredient);
            if (innerIngredient instanceof IntProviderIngredient) {
                return SizedIngredient.copy(innerIngredient);
            }
            return FastSizedIngredient.create(innerIngredient, fastSizedIngredient.amount);
        } else if (ingredient instanceof IntCircuitIngredient circuit) {
            return circuit;
        } else if (ingredient instanceof IntProviderIngredient) {
            return SizedIngredient.copy(ingredient);
        }
        return FastSizedIngredient.create(ingredient);
    }

    @Override
    @NotNull
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    @Override
    @NotNull
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", SizedIngredient.TYPE.toString());
        json.addProperty("count", amount);
        json.add("ingredient", inner.toJson());
        return json;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return inner.test(stack);
    }

    @Override
    public ItemStack @NotNull [] getItems() {
        if (inner instanceof IntProviderIngredient intProviderIngredient) {
            return intProviderIngredient.getItems();
        }
        if (itemStacks == null) {
            List<ItemStack> itemList = new ArrayList<>(inner.getItems().length);
            for (ItemStack i : inner.getItems()) {
                ItemStack ic = i.copy();
                ic.setCount(amount);
                itemList.add(ic);
            }
            itemStacks = itemList.toArray(new ItemStack[0]);
        }
        return itemStacks;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        if (itemStacks == null) return;
        for (ItemStack stack : itemStacks) {
            stack.setCount(amount);
        }
    }

    @Override
    protected void invalidate() {
        super.invalidate();
        this.itemStacks = null;
    }

    @Override
    @NotNull
    public IntList getStackingIds() {
        return inner.getStackingIds();
    }

    @Override
    public boolean isEmpty() {
        return inner.isEmpty();
    }

    @Override
    public int hashCode() {
        int result = amount;
        result = 31 * result + Arrays.hashCode(itemStacks);
        return result;
    }

    public static final IIngredientSerializer<FastSizedIngredient> SERIALIZER = new IIngredientSerializer<>() {

        @Override
        @NotNull
        public FastSizedIngredient parse(FriendlyByteBuf buffer) {
            int amount = buffer.readVarInt();
            return new FastSizedIngredient(Ingredient.fromNetwork(buffer), amount);
        }

        @Override
        @NotNull
        public FastSizedIngredient parse(JsonObject json) {
            int amount = json.get("count").getAsInt();
            Ingredient inner = Ingredient.fromJson(json.get("ingredient"));
            return new FastSizedIngredient(inner, amount);
        }

        @Override
        public void write(FriendlyByteBuf buffer, FastSizedIngredient ingredient) {
            buffer.writeVarInt(ingredient.getAmount());
            ingredient.inner.toNetwork(buffer);
        }
    };
}
