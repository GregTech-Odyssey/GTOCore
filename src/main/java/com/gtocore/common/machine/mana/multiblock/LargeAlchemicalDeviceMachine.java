package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.gtolib.utils.MachineUtils.getHatchParallel;
import static com.lowdragmc.lowdraglib.LDLib.random;

public final class LargeAlchemicalDeviceMachine extends ManaMultiblockMachine {

    @Persisted
    private final int[] probabilityParams = { 10000, 10000, 10000 };

    private final int[] currentRecipeParams = new int[3];

    private boolean perfectProbability = false;

    private double timeReduction = 0.4;

    public LargeAlchemicalDeviceMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        perfectProbability = (getSubFormedAmount() == 1);
        timeReduction = (getSubFormedAmount() == 1) ? 0.01 : 0.4;
    }

    @Override
    protected @Nullable Recipe getRealRecipe(@NotNull Recipe recipe) {
        boolean param = false;
        long parallels = getHatchParallel(this);
        recipe.duration = Math.max(1, (int) (recipe.duration * timeReduction));
        for (int i = 0; i < 3; i++) {
            String key = "param" + (i + 1);
            param = param || recipe.data.contains(key);
            currentRecipeParams[i] = recipe.data.contains(key) ? recipe.data.getInt(key) * 100 : 10000;
        }
        if (param) {
            adjustParameters(currentRecipeParams);
            return ParallelLogic.accurateParallel(this, enhanceRecipe(recipe, currentRecipeParams), parallels);
        }
        return ParallelLogic.accurateParallel(this, recipe, parallels);
    }

    /**
     * 增强配方：添加概率输出项
     */
    private Recipe enhanceRecipe(Recipe recipe, int[] recipeParams) {
        int matchRate = calculateMatchRate(recipeParams);

        recipe.outputs.put(ItemRecipeCapability.CAP, recipe.getOutputContents(ItemRecipeCapability.CAP).stream().map(content -> {
            if (content.chance < 11) return new Content(content.content, matchRate, ChanceLogic.getMaxChancedValue(), 0);
            else return content;
        }).toList());

        recipe.outputs.put(FluidRecipeCapability.CAP, recipe.getOutputContents(FluidRecipeCapability.CAP).stream().map(content -> {
            if (content.chance < 11) return new Content(content.content, matchRate, ChanceLogic.getMaxChancedValue(), 0);
            else return content;
        }).toList());

        return recipe;
    }

    /**
     * 计算匹配率
     */
    private int calculateMatchRate(int[] recipeParams) {
        if (perfectProbability) return 10000;
        int distance = calculateDistance(probabilityParams, recipeParams);

        if (distance <= 0) return 10000;
        else if (distance <= 5) return 9000;
        else if (distance >= 3000) return 1;

        float linear = (1 - distance * 0.00033333333f);
        float exponential = (float) Math.exp((1000 - distance) * 0.0005f);
        int randomValue = random.nextInt() & 3;
        return Mth.clamp(Math.round(5000.0f * linear * exponential * randomValue), 0, 10000);
    }

    /**
     * 计算距离
     */
    private static int calculateDistance(int[] p, int[] r) {
        int d = 0, rmc = 0, pmc = 0;
        int pMask = 0, rMask = 0;
        double cp2 = 0, pr2 = 0, dot = 0;
        final int c = 10000;

        for (int i = 0; i < 3; i++) {
            // 一次读取所有需要的坐标值
            int pi = p[i], ri = r[i];
            // 曼哈顿距离计算
            d += Math.abs(pi - ri);
            // 中心点距离计算
            rmc += Math.abs(ri - c);
            pmc += Math.abs(pi - c);
            // 位掩码存储坐标方向（第i位表示维度i的方向）
            pMask |= (pi >= c) ? (1 << i) : 0;
            rMask |= (ri >= c) ? (1 << i) : 0;
            // 向量计算（复用已计算的差值）
            double cp = pi - c;
            double pr = ri - pi;
            cp2 += cp * cp;
            pr2 += pr * pr;
            dot += cp * pr;
        }
        // 合并条件判断：1.方向mask不同 2.超出曼哈顿距离
        if ((pMask != rMask) | (rmc > pmc)) return d;
        // 最终角度检查（使用预计算cos²(10°)）
        return (dot * dot >= 0.984807753 * cp2 * pr2) ? -d : d;
    }

    /**
     * 参数调整
     */
    private void adjustParameters(int[] targetParams) {
        for (int i = 0; i < 3; i++) {
            probabilityParams[i] = Mth.clamp(
                    Math.round(probabilityParams[i] * 0.66f + targetParams[i] * 0.34f),
                    0, 20000);
        }
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", timeReduction));
        if (perfectProbability) textList.add(Component.translatable("gtocore.machine.alchemical_device.2"));
        else textList.add(Component.translatable("gtocore.machine.alchemical_device.1"));
    }
}
