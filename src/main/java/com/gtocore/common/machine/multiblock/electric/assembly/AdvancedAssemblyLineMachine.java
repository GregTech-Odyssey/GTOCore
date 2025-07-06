package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtocore.common.machine.multiblock.part.HugeBusPartMachine;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class AdvancedAssemblyLineMachine extends ElectricMultiblockMachine {

    public AdvancedAssemblyLineMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private List<CustomItemStackHandler> itemStackTransfers = new ArrayList<>();

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        Ingredient[] recipeIngredients = getRecipeIngredients(recipe);
        int size = recipeIngredients.length;
        if (!hasSufficientStackTransfers(this, size)) return null;
        Ingredient[] matchIngredients = getMatchIngredients(recipeIngredients);
        if (!validateIngredientStacks(this, size, matchIngredients)) {
            setIdleReason(IdleReason.ORDERED);
            return null;
        }
        return RecipeModifierFunction.laserLossOverclocking(this, RecipeModifierFunction.hatchParallel(this, recipe));
    }

    /**
     * 从给定的GTRecipe对象中获取配方所需的原料。
     *
     * @param recipe 包含配方信息的GTRecipe对象
     * @return 一个包含配方所需原料的Ingredient数组
     */
    private static Ingredient[] getRecipeIngredients(GTRecipe recipe) {
        return recipe.inputs.get(ItemRecipeCapability.CAP).stream()
                .map(i -> FastSizedIngredient.getInner(ItemRecipeCapability.CAP.of(i.getContent())))
                .toArray(Ingredient[]::new);
    }

    /**
     * 检查高级总装线机器的物品堆叠转移数是否满足指定的最小数量。
     *
     * @param lineMachine 高级总装线机器对象。
     * @param size        要求的最小物品堆叠转移数。
     * @return 如果物品堆叠转移数大于或等于指定的数量，则返回 true；否则返回 false。
     */
    private static boolean hasSufficientStackTransfers(AdvancedAssemblyLineMachine lineMachine, int size) {
        return lineMachine.itemStackTransfers.size() >= size;
    }

    /**
     * 获取匹配的配方原料数组，并根据第一个原料的堆栈检测其余原料是否匹配。
     * 如果匹配，将对应位置的原料设为空。
     *
     * @param recipeIngredients 配方原料数组
     * @return 匹配后的原料数组
     */
    private static Ingredient[] getMatchIngredients(Ingredient[] recipeIngredients) {
        int size = recipeIngredients.length;
        Ingredient[] matchIngredients = new Ingredient[size];
        ItemStack recipeStack = ItemUtils.getFirst(recipeIngredients[0]);
        matchIngredients[0] = recipeIngredients[0];

        for (int i = 1; i < size; i++) {
            Ingredient currentIngredient = recipeIngredients[i];
            if (currentIngredient.test(recipeStack)) {
                matchIngredients[i - 1] = Ingredient.EMPTY;
                matchIngredients[i] = Ingredient.EMPTY;
            } else {
                matchIngredients[i] = currentIngredient;
            }
            recipeStack = ItemUtils.getFirst(currentIngredient);
        }
        return matchIngredients;
    }

    /**
     * 验证AdvancedAssemblyLineMachine中的配料堆栈是否满足给定的匹配配料。
     *
     * @param lineMachine      AdvancedAssemblyLineMachine对象
     * @param size             配料的数量
     * @param matchIngredients 用于匹配的配料数组
     * @return 如果所有配料堆栈匹配则返回true，否则返回false
     */
    private static boolean validateIngredientStacks(AdvancedAssemblyLineMachine lineMachine, int size, Ingredient[] matchIngredients) {
        for (int i = 0; i < size; i++) {
            Ingredient currentIngredient = matchIngredients[i];
            if (currentIngredient.isEmpty()) continue;
            CustomItemStackHandler storage = lineMachine.itemStackTransfers.get(i);
            if (!isValidStorage(storage, currentIngredient)) return false;
        }
        return true;
    }

    /**
     * 判断指定的存储是否有效。
     *
     * @param storage           指定的ItemStackTransfer存储对象
     * @param currentIngredient 当前的成分
     * @return 如果存储中的所有物品相同并且匹配当前成分，则返回true，否则返回false
     */
    private static boolean isValidStorage(CustomItemStackHandler storage, Ingredient currentIngredient) {
        Set<Item> itemSet = new ObjectOpenHashSet<>();
        ItemStack stack = ItemStack.EMPTY;

        var slots = storage.getSlots();
        for (int j = 0; j < slots; j++) {
            ItemStack item = storage.getStackInSlot(j);
            if (!item.isEmpty()) {
                itemSet.add(item.getItem());
                stack = item;
            }
        }

        return itemSet.size() == 1 && currentIngredient.test(stack);
    }

    /**
     * 当结构形成时调用的方法。
     * 在此方法中，设置部件的排序方式，并初始化itemStackTransfers字段。
     * itemStackTransfers字段存储了通过筛选和映射获取的所有ItemBusPartMachine实例的库存存储区。
     * 此外，该方法还调用了父类的onStructureFormed方法以执行超类中的相关逻辑。
     */
    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        itemStackTransfers = new ArrayList<>();
        for (Object part : getParts()) {
            if (part instanceof ItemBusPartMachine itemBusPart) {
                itemStackTransfers.add(itemBusPart.getInventory().storage);
            } else if (part instanceof HugeBusPartMachine hugeBusPartMachine) {
                itemStackTransfers.add(hugeBusPartMachine.getInventory().storage);
            }
        }
    }

    public static Comparator<IMultiPart> partSorter(MultiblockControllerMachine mc) {
        return Comparator.comparing(p -> p.self().getPos(), RelativeDirection.RIGHT.getSorter(mc.getFrontFacing(), mc.getUpwardsFacing(), mc.isFlipped()));
    }
}
