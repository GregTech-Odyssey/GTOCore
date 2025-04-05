package com.gto.gtocore.api.recipe;

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

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class FastSizedIngredient extends Ingredient {

    @Getter
    private int amount;
    @Getter
    private final Ingredient inner;
    private ItemStack[] itemStacks = null;

    private FastSizedIngredient(Ingredient inner, int amount) {
        super(EmptyStream.INSTANCE);
        this.amount = amount;
        this.inner = inner;
    }

    private FastSizedIngredient(@NotNull TagKey<Item> tag, int amount) {
        this(Ingredient.of(tag), amount);
    }

    private FastSizedIngredient(ItemStack itemStack) {
        this((itemStack.hasTag() || itemStack.getDamageValue() > 0) ? NBTIngredient.createNBTIngredient(itemStack) : Ingredient.of(itemStack), itemStack.getCount());
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
                return SizedIngredient.copy(ingredient);
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

    @SuppressWarnings("unchecked")
    private static class EmptyStream implements Stream<Ingredient.Value> {

        private static final EmptyStream INSTANCE = new EmptyStream();

        private static final Stream EMPTY = Stream.empty();
        private static final IntStream EMPTY_INT = IntStream.empty();
        private static final LongStream EMPTY_LONG = LongStream.empty();
        private static final DoubleStream EMPTY_DOUBLE = DoubleStream.empty();
        private static final Ingredient.Value[] EMPTY_ARRAY = new Ingredient.Value[0];

        @Override
        public Stream<Ingredient.Value> filter(Predicate<? super Ingredient.Value> predicate) {
            return INSTANCE;
        }

        @Override
        public <R> Stream<R> map(Function<? super Ingredient.Value, ? extends R> mapper) {
            return EMPTY;
        }

        @Override
        public IntStream mapToInt(ToIntFunction<? super Ingredient.Value> mapper) {
            return EMPTY_INT;
        }

        @Override
        public LongStream mapToLong(ToLongFunction<? super Ingredient.Value> mapper) {
            return EMPTY_LONG;
        }

        @Override
        public DoubleStream mapToDouble(ToDoubleFunction<? super Ingredient.Value> mapper) {
            return EMPTY_DOUBLE;
        }

        @Override
        public <R> Stream<R> flatMap(Function<? super Ingredient.Value, ? extends Stream<? extends R>> mapper) {
            return EMPTY;
        }

        @Override
        public IntStream flatMapToInt(Function<? super Ingredient.Value, ? extends IntStream> mapper) {
            return EMPTY_INT;
        }

        @Override
        public LongStream flatMapToLong(Function<? super Ingredient.Value, ? extends LongStream> mapper) {
            return EMPTY_LONG;
        }

        @Override
        public DoubleStream flatMapToDouble(Function<? super Ingredient.Value, ? extends DoubleStream> mapper) {
            return EMPTY_DOUBLE;
        }

        @Override
        public Stream<Ingredient.Value> distinct() {
            return INSTANCE;
        }

        @Override
        public Stream<Ingredient.Value> sorted() {
            return INSTANCE;
        }

        @Override
        public Stream<Ingredient.Value> sorted(Comparator<? super Ingredient.Value> comparator) {
            return INSTANCE;
        }

        @Override
        public Stream<Ingredient.Value> peek(Consumer<? super Ingredient.Value> action) {
            return INSTANCE;
        }

        @Override
        public Stream<Ingredient.Value> limit(long maxSize) {
            return INSTANCE;
        }

        @Override
        public Stream<Ingredient.Value> skip(long n) {
            return INSTANCE;
        }

        @Override
        public void forEach(Consumer<? super Ingredient.Value> action) {}

        @Override
        public void forEachOrdered(Consumer<? super Ingredient.Value> action) {}

        @Override
        public @NotNull Ingredient.Value @NotNull [] toArray() {
            return EMPTY_ARRAY;
        }

        @Override
        public @NotNull <A> A @NotNull [] toArray(IntFunction<A[]> generator) {
            return (A[]) EMPTY_ARRAY;
        }

        @Override
        public Ingredient.Value reduce(Ingredient.Value identity, BinaryOperator<Ingredient.Value> accumulator) {
            return null;
        }

        @Override
        public @NotNull Optional<Ingredient.Value> reduce(BinaryOperator<Ingredient.Value> accumulator) {
            return Optional.empty();
        }

        @Override
        public <U> U reduce(U identity, BiFunction<U, ? super Ingredient.Value, U> accumulator, BinaryOperator<U> combiner) {
            return null;
        }

        @Override
        public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Ingredient.Value> accumulator, BiConsumer<R, R> combiner) {
            return null;
        }

        @Override
        public <R, A> R collect(Collector<? super Ingredient.Value, A, R> collector) {
            return null;
        }

        @Override
        public @NotNull Optional<Ingredient.Value> min(Comparator<? super Ingredient.Value> comparator) {
            return Optional.empty();
        }

        @Override
        public @NotNull Optional<Ingredient.Value> max(Comparator<? super Ingredient.Value> comparator) {
            return Optional.empty();
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public boolean anyMatch(Predicate<? super Ingredient.Value> predicate) {
            return false;
        }

        @Override
        public boolean allMatch(Predicate<? super Ingredient.Value> predicate) {
            return false;
        }

        @Override
        public boolean noneMatch(Predicate<? super Ingredient.Value> predicate) {
            return false;
        }

        @Override
        public @NotNull Optional<Ingredient.Value> findFirst() {
            return Optional.empty();
        }

        @Override
        public @NotNull Optional<Ingredient.Value> findAny() {
            return Optional.empty();
        }

        @Override
        public @NotNull Iterator<Ingredient.Value> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public @NotNull Spliterator<Ingredient.Value> spliterator() {
            return Spliterators.emptySpliterator();
        }

        @Override
        public boolean isParallel() {
            return false;
        }

        @Override
        public @NotNull Stream<Ingredient.Value> sequential() {
            return INSTANCE;
        }

        @Override
        public @NotNull Stream<Ingredient.Value> parallel() {
            return INSTANCE;
        }

        @Override
        public @NotNull Stream<Ingredient.Value> unordered() {
            return INSTANCE;
        }

        @Override
        public @NotNull Stream<Ingredient.Value> onClose(@NotNull Runnable closeHandler) {
            return INSTANCE;
        }

        @Override
        public void close() {}
    }
}
