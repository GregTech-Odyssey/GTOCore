package com.gtocore.common.machine.mana.multiblock;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.feature.multiblock.IStorageMultiblock;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDropSaveMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lowdragmc.lowdraglib.LDLib.random;

public class ResonanceFlowerMachine extends ManaMultiblockMachine implements IStorageMultiblock, IDropSaveMachine {

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
    private final List<CompoundTag> recipeIncremental = new ArrayList<>();
    private static final int MAX_SIZE = 10;
    private static final String NBT_KEY_RECIPE_INCREMENTAL = "RecipeIncremental";

    // 额外共鸣输入
    @Persisted
    private int frequency = Integer.MAX_VALUE;
    @Persisted
    private ItemStack resonanceItem = ItemStack.EMPTY;
    @Persisted
    private FluidStack resonanceFluid = FluidStack.EMPTY;

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
        resetResonance();

        String id = recipe.id.getPath();
        Object[] tierEffect = getTierEffect(id);

        if (recipe.data.contains("resonance")) {
            Object[] resonance = fromResonanceTag(recipe.data.getCompound("resonance"));
            if (resonance[0] instanceof ItemStack itemStack) {
                resonanceItem = itemStack;
                resonanceFluid = FluidStack.EMPTY;
            } else if (resonance[0] instanceof FluidStack fluidStack) {
                resonanceItem = ItemStack.EMPTY;
                resonanceFluid = fluidStack;
            }
            frequency = (int) resonance[1];
        }

        double durationMultiplier = recipe.duration * timeFluctuationCoefficient * (float) tierEffect[0];
        recipe.duration = (int) Math.max(1, durationMultiplier);
        long maxContentParallel = Math.min(ParallelLogic.getMaxContentParallel(this, recipe), (long) tierEffect[1]);

        addEntry(id, maxContentParallel);
        upgradeEntry(id);
        updateStableTime();

        return ParallelLogic.accurateParallel(this, recipe, maxContentParallel);
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        resetResonance();

        updateStableTime();
        if (stableTime > 0) stableTime--;
        else triggerFluctuation();
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            if (frequency > 0 && getRecipeLogic().getProgress() % frequency == 0 && getRecipeLogic().getProgress() != 0) {
                if (!resonanceFluid.isEmpty()) {
                    return inputFluid(resonanceFluid);
                } else if (!resonanceItem.isEmpty()) {
                    return inputItem(resonanceItem);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.resonance_flower.stable_operation_times", stableTime));
        textList.add(Component.translatable("gtocore.machine.resonance_flower.time_fluctuation_coefficient", String.format("%.6f", timeFluctuationCoefficient)));
        textList.add(Component.translatable("gtocore.machine.resonance_flower.elemental_fluctuation_coefficient", String.format("%.6f", elementalFluctuationCoefficient)));
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
            tagList.forEach(itemTag -> this.recipeIncremental.add((CompoundTag) itemTag));
        }
    }

    private void resetResonance() {
        resonanceItem = ItemStack.EMPTY;
        resonanceFluid = FluidStack.EMPTY;
        frequency = Integer.MAX_VALUE;
    }

    private void updateStableTime() {
        if (stableTime >= 100000000) return;
        ItemStack stack = machineStorage.getStackInSlot(0);
        if (stack.isEmpty()) return;
        if (stack.getItem() == GTOItems.STABILIZER_CORE.asItem()) {
            stableTime += 10000000;
            stack.shrink(1);
            machineStorage.setStackInSlot(0, stack);
        } else if (stack.getItem() == Items.NETHER_STAR) {
            stableTime += 5 * stack.getCount();
            machineStorage.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    /////////////////////////////////////
    // ********** 共鸣消耗系统 ********** //
    /////////////////////////////////////

    private static final String KEY_TYPE = "type";
    private static final String KEY_FREQUENCY = "frequency";
    private static final String KEY_STACK = "stack";
    private static final String KEY_AMOUNT = "Amount";
    private static final String TYPE_ITEM = "item";
    private static final String TYPE_FLUID = "fluid";

    // 通用序列化：ItemStack/FluidStack + 频率 → CompoundTag
    public static CompoundTag toResonanceTag(Object stack, int frequency) {
        CompoundTag root = new CompoundTag();
        root.putInt(KEY_FREQUENCY, frequency);
        if (stack instanceof ItemStack itemStack) {
            root.putString(KEY_TYPE, TYPE_ITEM);
            CompoundTag stackTag = new CompoundTag();
            itemStack.save(stackTag);
            root.put(KEY_STACK, stackTag);
        } else if (stack instanceof FluidStack fluidStack) {
            root.putString(KEY_TYPE, TYPE_FLUID);
            CompoundTag stackTag = new CompoundTag();
            stackTag.putString("FluidName", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())).toString());
            stackTag.putInt(KEY_AMOUNT, fluidStack.getAmount());
            if (fluidStack.hasTag()) stackTag.put("tag", fluidStack.getTag().copy());
            root.put(KEY_STACK, stackTag);
        }
        return root;
    }

    // 通用反序列化：CompoundTag → Object[] [stack, frequency]
    public static Object[] fromResonanceTag(CompoundTag tag) {
        int frequency = tag.getInt(KEY_FREQUENCY);
        String type = tag.getString(KEY_TYPE);
        if (!tag.contains(KEY_STACK, Tag.TAG_COMPOUND)) return new Object[] { null, frequency };

        CompoundTag stackTag = tag.getCompound(KEY_STACK);
        Object stack = null;
        if (type.equals(TYPE_ITEM)) {
            stack = ItemStack.of(stackTag);
        } else if (type.equals(TYPE_FLUID)) {
            Fluid fluid = RegistriesUtils.getFluid(stackTag.getString("FluidName"));
            if (fluid != null && fluid != Fluids.EMPTY) {
                int amount = stackTag.getInt(KEY_AMOUNT);
                CompoundTag extraTag = stackTag.contains("tag", Tag.TAG_COMPOUND) ? stackTag.getCompound("tag") : null;
                stack = new FluidStack(fluid, amount, extraTag);
            }
        }
        return new Object[] { stack, frequency };
    }

    /** 波动系数系统 */
    public void triggerFluctuation() {
        // 1. 时间消耗波动：每次跳变倍数范围 0.2 ~ 2.6，最终倍数范围 0.05 ~ 20
        double newTimeMultiplier = timeFluctuationCoefficient * (0.2D + random.nextDouble() * 2.4D);
        timeFluctuationCoefficient = Mth.clamp(newTimeMultiplier, 0.05D, 20.0D);
        // 2. 元素消耗波动：每次跳变倍数范围 0.5 ~ 1.8，最终倍数范围 0.1 ~ 16
        double newElemMultiplier = elementalFluctuationCoefficient * (0.5D + random.nextDouble() * 1.3D);
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
            CompoundTag entryTag = recipeIncremental.get(i);
            if (id.equals(entryTag.getString("id"))) {
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
        for (CompoundTag entryTag : recipeIncremental) {
            if (id.equals(entryTag.getString("id"))) return entryTag;
        }
        return null;
    }

    /** 升级指定ID的配方等级 */
    public void upgradeEntry(String id) {
        CompoundTag entry = getEntryById(id);
        if (entry == null) return;

        short currentTier = entry.getShort("tier");
        if (currentTier >= 256) return;

        long currentFrequency = entry.getLong("frequency");
        long upgradeRequirement = calculateUpgradeRequirement(currentTier);

        if (currentFrequency < upgradeRequirement) return;

        entry.putLong("frequency", currentFrequency - upgradeRequirement);
        entry.putShort("tier", (short) (currentTier + 1));
    }

    /** 计算升级所需frequency */
    private long calculateUpgradeRequirement(short currentTier) {
        if (currentTier >= 256) return Long.MAX_VALUE;
        if (currentTier <= 0) return 10L;

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
        if (tier >= 64) return 0.1f;
        if (tier <= 0) return 1.0f;

        return tier <= 8 ? 1.0f - tier * 0.025f : 0.8f - (tier - 8) * 0.0125f;
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
