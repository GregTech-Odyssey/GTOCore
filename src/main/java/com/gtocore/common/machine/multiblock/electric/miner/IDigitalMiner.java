package com.gtocore.common.machine.multiblock.electric.miner;

import com.gregtechceu.gtceu.api.cover.filter.Filter;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterEnumLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;



public interface IDigitalMiner extends IRecipeLogicMachine {

    MinerConfig getMinerConfig();

    Level getLevel();

    EnergyContainerList getEnergyContainer();

    @NotNull DigitalMinerLogic getRecipeLogic();


    @Override
    default RecipeLogic createRecipeLogic(Object... args) {
        return new DigitalMinerLogic(this);
    }



    record MinerConfig(AABB minerArea,int speed,long energyPerTick,int parallelMining,int silkLevel,Filter<?, ?> filter,FluidMode fluidMode){ }

    @DataGeneratorScanned
    @RegisterEnumLang(keyPrefix = "gtocore.digital_miner.fluid_mode")
    @SuppressWarnings("all")
    enum FluidMode {

        Harvest("采集", "Harvest",
                "采集并获取流体", "Harvest and collect fluids", ChatFormatting.GREEN),
        Ignore("忽略", "Ignore",
                "遇到流体方块时忽略它们", "Ignore fluid blocks when encountered", ChatFormatting.GRAY),
        Void("销毁", "Void",
                "销毁遇到的流体方块，且不采集", "Destroy encountered fluid blocks without collecting", ChatFormatting.GOLD);

        @RegisterEnumLang.CnValue("title")
        private final String cn;
        @RegisterEnumLang.EnValue("title")
        private final String en;
        @RegisterEnumLang.CnValue("tooltip")
        private final String cnTooltip;
        @RegisterEnumLang.EnValue("tooltip")
        private final String enTooltip;
        public final ChatFormatting color;

        FluidMode(String cn, String en, String cnTooltip, String enTooltip, ChatFormatting color) {
            this.cn = cn;
            this.en = en;
            this.cnTooltip = cnTooltip;
            this.enTooltip = enTooltip;
            this.color = color;
        }

        public String getTitle() {
            return Component.translatable("gtocore.digital_miner.fluid_mode.title." + this.name()).getString();
        }

        public String getTooltip() {
            return Component.translatable("gtocore.digital_miner.fluid_mode.tooltip." + this.name()).getString();
        }

        public FluidMode next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

}
