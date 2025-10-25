package com.gtocore.common.machine.mana;

import com.gtolib.api.machine.feature.IReceiveHeatMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.lowdragmc.lowdraglib.LDLib.random;

public class AlchemyCauldron extends SimpleManaMachine implements IReceiveHeatMachine {

    @Persisted
    private int temperature = 293;
    private TickableSubscription tickSubs;
    @Persisted
    private final int[] probabilityParams = { 10000, 10000, 10000 };
    private final int[] currentRecipeParams = new int[3];

    public AlchemyCauldron(MetaMachineBlockEntity holder) {
        super(holder, 1, t -> 16000);
    }

    @Nullable
    @Override
    public Recipe doModifyRecipe(@NotNull Recipe recipe) {
        int temperature = recipe.data.getInt("temperature");
        if (temperature > 0 && temperature > this.temperature) {
            setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
            return null;
        }
        boolean param = false;
        for (int i = 0; i < 3; i++) {
            String key = "param" + (i + 1);
            param = param || recipe.data.contains(key);
            currentRecipeParams[i] = recipe.data.contains(key) ? recipe.data.getInt(key) * 100 : 10000;
        }
        if (param) {
            adjustParameters(currentRecipeParams);
            return enhanceRecipe(recipe, currentRecipeParams);
        }
        return super.doModifyRecipe(recipe);
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            if (getOffsetTimer() % 20 == 0) return reduceTemperature(1) == 1;
            return true;
        }
        return false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    @Override
    public int getHeatCapacity() {
        return 24;
    }

    @Override
    public int getMaxTemperature() {
        return 1600;
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

    @Override
    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
    }
}
