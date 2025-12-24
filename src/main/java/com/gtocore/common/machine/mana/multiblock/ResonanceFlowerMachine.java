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
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
        machineStorage = createMachineStorage(i -> i.getItem() == GTOItems.INFINITE_CELL_COMPONENT.asItem() || i.getItem() == GTOItems.STABILIZER_CORE.asItem());
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
        String id = recipe.id.getPath();
        Object[] tierEffect = getTierEffect(id);

        recipe.duration = (int) Math.max(1, recipe.duration * timeFluctuationCoefficient * (float) tierEffect[0]);
        long maxContentParallel = Math.max(ParallelLogic.getMaxContentParallel(this, recipe), (long) tierEffect[1]);

        addEntry(id, maxContentParallel);
        upgradeEntry(id);

        return ParallelLogic.accurateParallel(this, enhanceRecipe(recipe), maxContentParallel);
    }

    /**
     * 增强配方：添加输入消耗
     */
    private Recipe enhanceRecipe(Recipe recipe) {
        recipe.tickInputs.computeIfAbsent(ItemRecipeCapability.CAP, k -> new ObjectArrayList<>()).add(
                new Content(ItemRecipeCapability.CAP.of(
                        FastSizedIngredient.create(Items.IRON_INGOT, 2)),
                        ChanceLogic.getMaxChancedValue(), 0));

        recipe.tickInputs.computeIfAbsent(FluidRecipeCapability.CAP, k -> new ObjectArrayList<>()).add(
                new Content(FluidRecipeCapability.CAP.of(
                        FastFluidIngredient.of(1000, Fluids.WATER)),
                        ChanceLogic.getMaxChancedValue(), 0));

        return recipe;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (stableTime > 0) {
            stableTime--;
        } else if (stableTime == 0) {
            triggerFluctuation();
        }
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.stable_operation_times", stableTime < 0 ? "♾️" : stableTime));
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

    /////////////////////////////////////
    // ********** 波动系数系统 ********** //
    /////////////////////////////////////

    // 时间波动：每次跳变倍数范围 0.2 ~ 2.6，最终倍数范围 0.05 ~ 20
    private static final double TIME_JUMP_MIN = 0.2D;
    private static final double TIME_JUMP_MAX = 2.6D;
    private static final double TIME_LIMIT_MIN = 0.05D;
    private static final double TIME_LIMIT_MAX = 20.0D;

    // 元素波动：每次跳变倍数范围 0.5 ~ 1.8，最终倍数范围 0.1 ~ 16
    private static final double ELEM_JUMP_MIN = 0.5D;
    private static final double ELEM_JUMP_MAX = 1.8D;
    private static final double ELEM_LIMIT_MIN = 0.1D;
    private static final double ELEM_LIMIT_MAX = 16.0D;

    /** 触发波动 */
    public void triggerFluctuation() {
        // 1. 时间消耗波动：直接基于倍数相乘，无需基准转换
        double newTimeMultiplier = timeFluctuationCoefficient * (TIME_JUMP_MIN + random.nextDouble() * (TIME_JUMP_MAX - TIME_JUMP_MIN));
        timeFluctuationCoefficient = Mth.clamp(newTimeMultiplier, TIME_LIMIT_MIN, TIME_LIMIT_MAX);
        // 2. 元素消耗波动：同理简化
        double newElemMultiplier = elementalFluctuationCoefficient * (ELEM_JUMP_MIN + random.nextDouble() * (ELEM_JUMP_MAX - ELEM_JUMP_MIN));
        elementalFluctuationCoefficient = Mth.clamp(newElemMultiplier, ELEM_LIMIT_MIN, ELEM_LIMIT_MAX);
    }

    /////////////////////////////////////
    // ********** 配方记录系统 ********** //
    /////////////////////////////////////

    /**
     * 添加/更新条目：
     * - id不存在 → 新增（tire=0，frequency=传入值）；
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
            newEntry.putShort("tire", oldEntry.getShort("tire"));
            newEntry.putLong("frequency", oldEntry.getInt("frequency") + frequency);
        } else {
            newEntry.putShort("tire", (short) 1);
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

        short currentTire = entry.getShort("tire");
        if (currentTire > 256) return;

        long currentFrequency = entry.getLong("frequency");
        long upgradeRequirement = calculateUpgradeRequirement(currentTire);

        if (currentFrequency < upgradeRequirement) return;

        entry.putLong("frequency", currentFrequency - upgradeRequirement);
        entry.putShort("tire", (short) (currentTire + 1));
    }

    /** 计算升级所需frequency */
    private long calculateUpgradeRequirement(short currentTire) {
        if (currentTire <= 0) return 10L;
        if (currentTire >= 100) return Long.MAX_VALUE;
        double rawRequirement = 10L * Math.pow(1.511, currentTire);
        return rawRequirement > Long.MAX_VALUE ? Long.MAX_VALUE : Math.round(rawRequirement);
    }

    /** 指定ID的运行加成 */
    public Object[] getTierEffect(String id) {
        CompoundTag entry = getEntryById(id);
        short tier = entry == null ? 0 : entry.getShort("tire");
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

        if (tier <= 10) {
            return 1 + tier;
        } else if (tier <= 20) {
            return 11 + (tier - 10) * 10;
        } else if (tier <= 40) {
            return 111 + (tier - 20) * 50;
        } else if (tier <= 80) {
            return 1111 + (tier - 40) * 250;
        } else if (tier <= 160) {
            return 11111 + (tier - 80) * 1250;
        } else {
            return Math.min(101111 + (tier - 160) * 92500000000L, Long.MAX_VALUE);
        }
    }
}
