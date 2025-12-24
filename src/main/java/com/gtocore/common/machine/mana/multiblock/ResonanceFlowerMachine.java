package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.feature.multiblock.IStorageMultiblock;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lowdragmc.lowdraglib.LDLib.random;

public class ResonanceFlowerMachine extends NoEnergyMultiblockMachine implements IStorageMultiblock, IDropSaveMachine {

    // 时间消耗波动系数
    @Persisted
    private double timeFluctuationCoefficient = 1.0D;
    // 元素消耗波动系数
    @Persisted
    private double elementalFluctuationCoefficient = 1.0D;

    // 剩余的锚定时间
    @Persisted
    private int stableTime = 0;

    // 存储信息
    @Persisted
    private List<CompoundTag> recipeIncremental = new ArrayList<>();
    private static final int MAX_SIZE = 10;
    private static final String NBT_KEY_RECIPE_INCREMENTAL = "RecipeIncremental";

    @Persisted
    protected final NotifiableItemStackHandler machineStorage;

    public ResonanceFlowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
        machineStorage = createMachineStorage(i -> i.getItem() == Items.NETHER_STAR || i.getItem() == GTOItems.STABILIZER_CORE.asItem());
    }

    @Override
    public NotifiableItemStackHandler getMachineStorage() {
        return machineStorage;
    }

    @Override
    public @NotNull Widget createUIWidget() {
        return IStorageMultiblock.super.createUIWidget(super.createUIWidget());
    }

    @Override
    protected @Nullable Recipe getRealRecipe(@NotNull Recipe recipe) {
        recipe = super.getRealRecipe(recipe);
        if (recipe == null) return null;
        String id = recipe.id.getPath();
        Object[] tierEffect = getTierEffect(id);

        if (recipe.data.contains("resonance")) enhanceRecipe(recipe);

        recipe.duration = (int) Math.max(1, recipe.duration * timeFluctuationCoefficient * (float) tierEffect[0]);
        long maxContentParallel = Math.max(ParallelLogic.getMaxContentParallel(this, recipe), (long) tierEffect[1]);

        addEntry(id, maxContentParallel);
        upgradeEntry(id);
        updateStableTime();

        return ParallelLogic.accurateParallel(this, recipe, maxContentParallel);
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        updateStableTime();
        if (stableTime > 0) stableTime--;
        else triggerFluctuation();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.resonance_flower.stable_operation_times", stableTime));
        textList.add(Component.translatable("gtocore.machine.resonance_flower.time_fluctuation_coefficient", timeFluctuationCoefficient));
        textList.add(Component.translatable("gtocore.machine.resonance_flower.elemental_fluctuation_coefficient", elementalFluctuationCoefficient));
    }

    @Override
    public void saveToItem(CompoundTag tag) {
        ListTag tagList = new ListTag();
        tagList.addAll(this.recipeIncremental);
        tag.put(NBT_KEY_RECIPE_INCREMENTAL, tagList);
    }

    @Override
    public void loadFromItem(CompoundTag tag) {
        if (tag.contains(NBT_KEY_RECIPE_INCREMENTAL, Tag.TAG_LIST)) {
            ListTag tagList = tag.getList(NBT_KEY_RECIPE_INCREMENTAL, Tag.TAG_COMPOUND);
            for (Tag itemTag : tagList) {
                if (itemTag instanceof CompoundTag compoundTag) this.recipeIncremental.add(compoundTag);
            }
        }
    }

    private void updateStableTime() {
        if (!machineStorage.isEmpty() && stableTime < 1000000000) {
            ItemStack stack = machineStorage.getStackInSlot(0);
            if (stack.getItem() == GTOItems.STABILIZER_CORE.asItem()) {
                stableTime += 1000000000;
                stack.setCount(stack.getCount() - 1);
                machineStorage.setStackInSlot(0, stack);
            } else if (stack.getItem() == Items.NETHER_STAR) {
                stableTime += 5 * stack.getCount();
                machineStorage.setStackInSlot(0, ItemStack.EMPTY);
            }
        }
    }

    /////////////////////////////////////
    // ********** 共鸣消耗系统 ********** //
    /////////////////////////////////////

    private static final String KEY_TYPE = "type";
    private static final String TYPE_ITEM = "item";
    private static final String TYPE_FLUID = "fluid";

    // 通用序列化：ItemStack/FluidStack → CompoundTag
    public static CompoundTag toTag(Object stack) {
        CompoundTag root = new CompoundTag();
        if (stack instanceof ItemStack itemStack) {
            root.putString(KEY_TYPE, TYPE_ITEM);
            root.merge(itemStack.save(new CompoundTag()));
        } else if (stack instanceof FluidStack fluidStack) {
            root.putString(KEY_TYPE, TYPE_FLUID);
            root.putString("fluidId", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())).toString());
            root.putInt("amount", fluidStack.getAmount());
            if (fluidStack.hasTag()) root.put("tag", fluidStack.getTag().copy());
        }
        return root;
    }

    // 通用反序列化：CompoundTag → ItemStack/FluidStack（修复核心问题）
    public static Object fromTag(CompoundTag tag) {
        String type = tag.getString(KEY_TYPE);
        if (TYPE_ITEM.equals(type)) {
            CompoundTag itemTag = tag.copy();
            itemTag.remove(KEY_TYPE);
            return ItemStack.of(itemTag);
        } else if (TYPE_FLUID.equals(type)) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString("fluidId")));
            if (fluid == null || fluid == Fluids.EMPTY) return null;
            int amount = tag.getInt("amount");
            CompoundTag extraTag = tag.contains("tag", Tag.TAG_COMPOUND) ? tag.getCompound("tag") : new CompoundTag();
            return new FluidStack(fluid, amount, extraTag);
        }
        return null;
    }

    /** 修改配方：添加共鸣消耗 */
    private void enhanceRecipe(Recipe recipe) {
        Object resonance = fromTag(recipe.data.getCompound("resonance"));
        if (resonance instanceof ItemStack itemStack) {
            recipe.tickInputs.computeIfAbsent(ItemRecipeCapability.CAP, k -> new ObjectArrayList<>()).add(
                    new Content(ItemRecipeCapability.CAP.of(
                            FastSizedIngredient.create(itemStack.getItem(), (int) Math.max(1, itemStack.getCount() * elementalFluctuationCoefficient))),
                            ChanceLogic.getMaxChancedValue(), 0));
        } else if (resonance instanceof FluidStack fluidStack) {
            recipe.tickInputs.computeIfAbsent(FluidRecipeCapability.CAP, k -> new ObjectArrayList<>()).add(
                    new Content(FluidRecipeCapability.CAP.of(
                            FastFluidIngredient.of((int) Math.max(1, fluidStack.getAmount() * elementalFluctuationCoefficient), fluidStack.getFluid())),
                            ChanceLogic.getMaxChancedValue(), 0));
        }
    }

    /** 波动系数系统 */
    public void triggerFluctuation() {
        // 1. 时间消耗波动：每次跳变倍数范围 0.2 ~ 2.6，最终倍数范围 0.05 ~ 20
        double newTimeMultiplier = timeFluctuationCoefficient * (0.2D + random.nextDouble() * (2.6D - 0.2D));
        timeFluctuationCoefficient = Mth.clamp(newTimeMultiplier, 0.05D, 20.0D);
        // 2. 元素消耗波动：每次跳变倍数范围 0.5 ~ 1.8，最终倍数范围 0.1 ~ 16
        double newElemMultiplier = elementalFluctuationCoefficient * (0.5D + random.nextDouble() * (1.8D - 0.5D));
        elementalFluctuationCoefficient = Mth.clamp(newElemMultiplier, 0.1D, 16.0D);
    }

    /////////////////////////////////////
    // ********** 配方记录系统 ********** //
    /////////////////////////////////////

    /**
     * 添加/更新条目：
     * - id不存在 → 新增（tier=1，frequency=传入值）；
     * - id存在 → 累加frequency（旧值+传入值），并将条目移到末尾（最晚添加）；
     * - 超量则删除最早添加的条目。
     */
    public void addEntry(String id, long frequency) {
        if (id == null || id.isEmpty()) return;

        // 查找并移除旧条目（存在则累加，且移到末尾）
        CompoundTag oldEntry = null;
        for (int i = 0; i < recipeIncremental.size(); i++) {
            Tag element = recipeIncremental.get(i);
            if (element instanceof CompoundTag entryTag && id.equals(entryTag.getString("id"))) {
                oldEntry = entryTag;
                recipeIncremental.remove(i);
                break;
            }
        }

        // 构建新条目（存在则累加frequency）
        CompoundTag newEntry = new CompoundTag();
        newEntry.putString("id", id);
        if (oldEntry != null) {
            newEntry.putShort("tier", oldEntry.getShort("tier"));
            newEntry.putLong("frequency", oldEntry.getLong("frequency") + frequency);
        } else {
            newEntry.putShort("tier", (short) 1);
            newEntry.putLong("frequency", frequency);
        }

        recipeIncremental.add(newEntry);

        while (recipeIncremental.size() > MAX_SIZE) recipeIncremental.removeFirst();
    }

    /** 按id查找条目 */
    public CompoundTag getEntryById(String id) {
        if (id == null || id.isEmpty()) return null;
        for (Tag element : recipeIncremental) {
            if (element instanceof CompoundTag entryTag && id.equals(entryTag.getString("id"))) {
                return entryTag;
            }
        }
        return null;
    }

    /** 升级指定ID的配方等级 */
    public void upgradeEntry(String id) {
        CompoundTag entry = getEntryById(id);
        if (entry == null) return;

        short currentTier = entry.getShort("tier");
        if (currentTier > 256) return;

        long currentFrequency = entry.getLong("frequency");
        long upgradeRequirement = calculateUpgradeRequirement(currentTier);

        if (currentFrequency < upgradeRequirement) return;

        entry.putLong("frequency", currentFrequency - upgradeRequirement);
        entry.putShort("tier", (short) (currentTier + 1));
    }

    /** 计算升级所需frequency */
    private long calculateUpgradeRequirement(short currentTier) {
        if (currentTier <= 0) return 10L;
        if (currentTier >= 256) return Long.MAX_VALUE;

        if (currentTier <= 4) {
            return 10L + currentTier * 100L;
        } else if (currentTier <= 8) {
            return 410L + (currentTier - 4) * 200L;
        } else if (currentTier <= 16) {
            return 1210L + (currentTier - 8) * 400L;
        } else if (currentTier <= 32) {
            return 4410L + (currentTier - 16) * 800L;
        } else if (currentTier <= 48) {
            return 17210L + (currentTier - 32) * 2600L;
        } else if (currentTier <= 64) {
            return 58900L + (currentTier - 48) * 9400L;
        } else if (currentTier <= 96) {
            return 360000L + (currentTier - 64) * 5000000L;
        } else if (currentTier <= 128) {
            return 180000000L + (currentTier - 96) * 64000000L;
        } else if (currentTier <= 192) {
            return 2400000000L + (currentTier - 128) * 800000000000000L;
        } else {
            return 52000000000000000L + (currentTier - 192) * 140000000000000000L;
        }
    }

    /** 指定ID的运行加成 */
    public Object[] getTierEffect(String id) {
        CompoundTag entry = getEntryById(id);
        short tier = entry == null ? 0 : entry.getShort("tier");
        return new Object[] { getTimeMultiplier(tier), getMaxParallel(tier) };
    }

    public float getTimeMultiplier(short tier) {
        if (tier >= 32) return 0.5f;
        if (tier <= 0) return 1.0f;

        if (tier <= 8) {
            return 1.0f - (tier * 0.025f);
        } else if (tier <= 16) {
            return 0.8f - ((tier - 8) * 0.0125f);
        } else if (tier <= 24) {
            return 0.7f - ((tier - 16) * 0.0125f);
        } else {
            return 0.6f - ((tier - 24) * 0.0125f);
        }
    }

    public long getMaxParallel(short tier) {
        if (tier >= 256) return Long.MAX_VALUE;
        if (tier <= 0) return 1L;

        if (tier <= 4) {
            return 1L + tier * 10L;
        } else if (tier <= 8) {
            return 50L + (tier - 4) * 20L;
        } else if (tier <= 16) {
            return 200L + (tier - 8) * 40L;
        } else if (tier <= 32) {
            return 800L + (tier - 16) * 80L;
        } else if (tier <= 48) {
            return 2400L + (tier - 32) * 250L;
        } else if (tier <= 64) {
            return 8000L + (tier - 48) * 800L;
        } else if (tier <= 96) {
            return 24000L + (tier - 64) * 50000L;
        } else if (tier <= 128) {
            return 1800000L + (tier - 96) * 600000L;
        } else if (tier <= 192) {
            return 42000000L + (tier - 128) * 800000000L;
        } else {
            return 52000000000L + (tier - 192) * 140000000000L;
        }
    }
}
