package com.gtocore.common.cover;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.IHeatContainer;
import com.gtolib.api.machine.heat.HeatHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;

import com.gto.datasynclib.annotations.SaveToDisk;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@DataGeneratorScanned
public final class HeatInterfaceCover extends CoverBehavior {

    @RegisterLanguage(cn = "最高温度：%s K", en = "Max Temperature: %s K")
    public static final String MAX_TEMPERATURE = "gtocore.machine.max_temperature";
    @RegisterLanguage(cn = "热容：%s HU/K", en = "Heat Capacity: %s HU/K")
    public static final String HEAT_CAPACITY = "gtocore.machine.heat_capacity";
    @RegisterLanguage(cn = "传热速度：%s HU/tΔT", en = "Heat Transfer Rate：%s HU/t")
    public static final String TRANSFER_RATE = "gtocore.machine.heat_transfer_rate";
    @RegisterLanguage(cn = "自然冷却速度：%s HU/t√ΔT", en = "Natural Cooling Rate：%s HU/t")
    public static final String COOLDOWN_RATE = "gtocore.machine.heat_cooldown_rate";
    @RegisterLanguage(cn = "产热速度：%s HU/t", en = "Heat Generation Rate：%s HU/t")
    public static final String GENERATION_RATE = "gtocore.machine.heat_generation_rate";
    @RegisterLanguage(cn = "耗热速度：%s HU/t", en = "Heat Consumption Rate：%s HU/t")
    public static final String CONSUMPTION_RATE = "gtocore.machine.heat_consumption_rate";

    @SaveToDisk
    private final HeatHandler handler;

    public HeatInterfaceCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        var tier = coverHolder.holder() instanceof MetaMachineBlockEntity blockEntity ? blockEntity.definition.getTier() + 1 : 1;
        handler = new HeatHandler(coverHolder.holder(), 400L + (400L * tier), tier, tier, 0.01);
        handler.setSideIOCondition(s -> s == attachedSide);
        handler.addChangedListener(() -> {
            if (MetaMachine.getMachine(coverHolder.holder()) instanceof IRecipeLogicMachine recipeLogicMachine) {
                recipeLogicMachine.getRecipeLogic().updateTickSubscription();
            }
        });
    }

    @Nullable
    public <T> Object getGTCapability(Class<T> cap) {
        if (cap == IHeatContainer.class) {
            return handler;
        }
        return null;
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && MetaMachine.getMachine(coverHolder.holder()) != null && coverHolder.holder().getGTCapability(IHeatContainer.class, null) == null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        handler.onLoad();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        handler.onUnLoad();
    }
}
