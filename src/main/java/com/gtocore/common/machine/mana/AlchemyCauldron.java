package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.machine.heat.HeatHandler;
import com.gtolib.api.machine.heat.feature.IHeatContainerMachine;
import com.gtolib.api.recipe.IdleReason;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import com.gto.datasynclib.annotations.SaveToDisk;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.lowdragmc.lowdraglib.LDLib.random;

public class AlchemyCauldron extends SimpleManaMachine implements IHeatContainerMachine {

    @SaveToDisk
    private final int[] probabilityParams = { 10000, 10000, 10000 };
    private final int[] currentRecipeParams = new int[3];

    @Getter
    @SaveToDisk
    private final HeatHandler heatContainer;

    public AlchemyCauldron(MetaMachineBlockEntity holder) {
        super(holder, 3, t -> 16000);
        heatContainer = new HeatHandler(holder, 1600, 0.5, 1.2, 0.02);
        heatContainer.setSideIOCondition(s -> s == Direction.DOWN);
        heatContainer.addChangedListener(getRecipeLogic()::updateTickSubscription);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        heatContainer.onLoad();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        heatContainer.onUnLoad();
    }

    @Nullable
    @Override
    public GTRecipe doModifyRecipe(RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        int temperature = recipe.data.getInt(GTORecipeDataKeys.TEMPERATURE);
        if (temperature > 0 && temperature > heatContainer.getTemperature()) {
            setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
            return null;
        }
        boolean param = false;
        for (int i = 0; i < 3; i++) {
            var key = GTORecipeDataKeys.PARAM[i];
            param = param || recipe.data.contains(key);
            currentRecipeParams[i] = recipe.data.contains(key) ? recipe.data.getInt(key) * 100 : 10000;
        }
        if (param) {
            adjustParameters(currentRecipeParams);
            return enhanceRecipe(recipe, currentRecipeParams);
        }
        return super.doModifyRecipe(unit, recipe);
    }

    @Override
    public boolean handleTickRecipe(GTRecipe recipe) {
        if (super.handleTickRecipe(recipe)) {
            if (getOffsetTimer() % 20 == 0) return heatContainer.removeHeatUnrestricted(1, false) == 1;
            return true;
        }
        return false;
    }

    /**
     * 增强配方：添加概率输出项
     */
    private GTRecipe enhanceRecipe(GTRecipe recipe, int[] recipeParams) {
        int matchRate = calculateMatchRate(recipeParams);

        recipe.itemOutputs = recipe.itemOutputs.stream().map(content -> {
            if (content.chance < 11) return new Content<>(content.inner, matchRate, 0);
            else return content;
        }).toList();

        recipe.fluidOutputs = recipe.fluidOutputs.stream().map(content -> {
            if (content.chance < 11) return new Content<>(content.inner, matchRate, 0);
            else return content;
        }).toList();

        return recipe;
    }

    /**
     * 计算匹配率
     */
    private int calculateMatchRate(int[] recipeParams) {
        int distance = calculateDistance(probabilityParams, recipeParams);
        if (distance <= 0) return 10000;
        else if (distance <= 5) return 9000;
        else if (distance >= 3000) return 1;
        float linear = (1 - distance * 3.3333333E-4F);
        float exponential = (float) Math.exp((1000 - distance) * 5.0E-4F);
        int randomValue = random.nextInt() & 3;
        return Mth.clamp(Math.round(5000.0F * linear * exponential * randomValue), 0, 10000);
    }

    /**
     * 计算距离
     */
    private static int calculateDistance(int[] p, int[] r) {
        int d = 0;
        int rmc = 0;
        int pmc = 0;
        int pMask = 0;
        int rMask = 0;
        double cp2 = 0;
        double pr2 = 0;
        double dot = 0;
        final int c = 10000;
        for (int i = 0; i < 3; i++) {
            // 一次读取所有需要的坐标值
            int pi = p[i];
            int ri = r[i];
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
            probabilityParams[i] = Mth.clamp(Math.round(probabilityParams[i] * 0.66F + targetParams[i] * 0.34F), 0, 20000);
        }
    }
}
