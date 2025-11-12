package com.gtocore.common.machine.multiblock.electric.miner;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterEnumLang;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.cover.filter.Filter;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

@DataGeneratorScanned
public interface IDigitalMiner extends IRecipeLogicMachine {

    @RegisterLanguage(cn = "流体模式", en = "Fluid Mode")
    String FLUID_MODE = "gtocore.digital_miner.fluid_mode";

    @RegisterLanguage(cn = "重置", en = "Reset")
    String RESET = "gtocore.digital_miner.reset";
    @RegisterLanguage(cn = "精准", en = "Silk")
    String SILK = "gtocore.digital_miner.silk";
    @RegisterLanguage(cn = "待挖掘：", en = "To be mined: ")
    String TO_BE_MINED = "gtocore.digital_miner.to_be_mined";
    @RegisterLanguage(cn = "开启精准采集模式，4倍耗电", en = "Enable silk touch mode, 4x power consumption")
    String SILK_TOOLTIP = "gtocore.digital_miner.silk.tooltip";
    @RegisterLanguage(cn = "修改配置后必须重置才能生效", en = "You must reset the configuration for it to take effect.")
    String RESET_TOOLTIP = "gtocore.digital_miner.reset.tooltip";
    @RegisterLanguage(cn = "最小高度", en = "Min Height")
    String MIN_HEIGHT = "gtocore.digital_miner.min_height";
    @RegisterLanguage(cn = "最大高度", en = "Max Height")
    String MAX_HEIGHT = "gtocore.digital_miner.max_height";
    @RegisterLanguage(cn = "同时采集§d%s§r个方块", en = "Mining §d%s§r blocks simultaneously")
    String PARALLEL = "gtocore.miner.parallel";
    @RegisterLanguage(cn = "显示范围", en = "Show Range")
    String SHOW_RANGE = "gtocore.digital_miner.show_range";
    @RegisterLanguage(cn = "在世界中显示当前采集范围", en = "Show the current mining range in the world")
    String SHOW_RANGE_TOOLTIP = "gtocore.digital_miner.show_range.tooltip";
    @RegisterLanguage(cn = "x径向长度", en = "x radial length")
    String XRADIAL_LENGTH = "gtocore.digital_miner.x_radial_length";
    @RegisterLanguage(cn = "z径向长度", en = "z radial length")
    String ZRADIAL_LENGTH = "gtocore.digital_miner.z_radial_length";
    @RegisterLanguage(cn = "x偏移量", en = "x offset")
    String X_OFFSET = "gtocore.digital_miner.x_offset";
    @RegisterLanguage(cn = "z偏移量", en = "z offset")
    String Z_OFFSET = "gtocore.digital_miner.z_offset";

    Level getLevel();

    MinerConfig getMinerConfig();

    boolean drainInput(boolean simulate);

    @NotNull
    DigitalMinerLogic getRecipeLogic();

    @Override
    default RecipeLogic createRecipeLogic(Object... args) {
        return new DigitalMinerLogic(this);
    }

    @Override
    default void setWorkingEnabled(boolean isWorkingAllowed) {
        if (isWorkingAllowed && getRecipeLogic().isDone()) getRecipeLogic().resetRecipeLogic();
        IRecipeLogicMachine.super.setWorkingEnabled(isWorkingAllowed);
    }

    @DataGeneratorScanned
    @RegisterEnumLang(keyPrefix = FLUID_MODE)
    @SuppressWarnings("all")
    enum FluidMode {

        Harvest("采集", "Harvest",
                "采集并获取流体", "Harvest and collect fluids", ChatFormatting.GREEN),
        Ignore("忽略", "Ignore",
                "遇到流体方块时忽略它们", "Ignore fluid blocks when encountered", ChatFormatting.GRAY),
        Void("销毁", "Void",
                "销毁遇到的流体方块，且不采集", "Destroy encountered fluid blocks without collecting", ChatFormatting.GOLD);

        public final ChatFormatting color;
        @RegisterEnumLang.CnValue("title")
        final String cn;
        @RegisterEnumLang.EnValue("title")
        final String en;
        @RegisterEnumLang.CnValue("tooltip")
        final String cnTooltip;
        @RegisterEnumLang.EnValue("tooltip")
        final String enTooltip;

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

    record MinerConfig(AABB minerArea, long energyPerTick, int speed, int parallelMining, int silkLevel,
                       Filter<?, ?> itemFilter, Filter<?, ?> fluidFilter, FluidMode fluidMode) {}
}
