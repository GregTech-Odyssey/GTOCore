package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.api.gui.GTOGuiTextures;
import com.gtocore.common.saved.VirtualCoinSavedData;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.ICustomElectricMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.shadowsoffire.placebo.color.GradientColor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

@DataGeneratorScanned
public class VirtualCoinMiner extends ElectricMultiblockMachine implements ICustomElectricMachine, ICustomRecipeLogicHolder {

    @SaveToDisk
    @Getter
    private long cwuLimitConfig = 256L;
    @SaveToDisk
    private long cwuBuffer = 0L;
    @SaveToDisk
    private long coinBuffer = 0L;
    private long eut = 0L;

    public VirtualCoinMiner(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean handleTickRecipe(@Nullable GTRecipe recipe) {
        if (recipe != null) {
            var cwuAvailable = requestCWU(cwuLimitConfig, true);
            eut = cwuAvailable * VA[EV];
            if (useEnergy(eut, false)) {
                cwuBuffer += requestCWU(cwuAvailable, false);
            }
        }
        return true;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        super.customText(textList);
        textList.add(Component.translatable(LANG_ACCUMULATED_COINS, FormattingUtil.formatNumbers(VirtualCoinSavedData.getTimesHasRun(getOwnerUUID())))
                .withStyle(s -> s.withColor(GradientColor.RAINBOW)));
        textList.add(Component.translatable(LANG_CWU_TO_NEXT_COIN,
                Component.literal(FormattingUtil.formatNumbers(VirtualCoinSavedData.getNextCoinNeeded(getOwnerUUID())))
                        .withStyle(s -> s.withColor(GradientColor.RAINBOW)))
                .withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable(LANG_UNCOMMITTED_CWU_BUFFER,
                Component.literal(FormattingUtil.formatNumbers(cwuBuffer)).withStyle(ChatFormatting.WHITE))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        if (cwuBuffer > 0) {
            coinBuffer += VirtualCoinSavedData.accumulateCoinWork(getOwnerUUID(), cwuBuffer);
            if (outputItem(ChemicalHelper.getItem(GTOTagPrefix.COIN, GTMaterials.Gold), coinBuffer)) {
                coinBuffer = 0L;
            }
            cwuBuffer = 0L;
        }
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new ParallelConfigurator(this));
    }

    @Override
    public double getTotalEu() {
        return eut;
    }

    @Override
    public boolean isActivated() {
        return recipeLogic.isActive();
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (getOwner() == null) return null;
        return getRecipeBuilder().duration(20).inputFluids(GTMaterials.PCBCoolant, 20).build();
    }

    private record ParallelConfigurator(VirtualCoinMiner machine) implements IFancyConfigurator {

        @Override
        public Component getTitle() {
            return Component.translatable(LANG_CWU_LIMIT);
        }

        @Override
        public IGuiTexture getIcon() {
            return GTOGuiTextures.PARALLEL_CONFIG;
        }

        @Override
        public Widget createConfigurator() {
            WidgetGroup group = new WidgetGroup(0, 0, 100, 20);
            var longInput = new LongInputWidget(machine::getCwuLimitConfig, this::onChange);
            group.addWidget(longInput);
            return group;
        }

        private void onChange(long newValue) {
            machine.cwuLimitConfig = newValue;
        }
    }

    @RegisterLanguage(cn = "未提交算力缓存: %s CWU", en = "Uncommitted CWU Buffer: %s CWU")
    public static final String LANG_UNCOMMITTED_CWU_BUFFER = "gtocore.machine.virtual_coin_miner.cwu_buffer";
    @RegisterLanguage(cn = "最大每刻消耗网络算力", en = "Max CWU Consumption per Tick")
    public static final String LANG_CWU_LIMIT = "gtocore.machine.virtual_coin_miner.cwu_limit";
    @RegisterLanguage(cn = "你已经累计挖到%s枚虚拟金币了！", en = "You have accumulated %s virtual coins!")
    public static final String LANG_ACCUMULATED_COINS = "gtocore.machine.virtual_coin_miner.accumulated_coins";
    @RegisterLanguage(cn = "下一枚虚拟金币:%sCWU", en = "CWU needed for next coin: %s CWU")
    public static final String LANG_CWU_TO_NEXT_COIN = "gtocore.machine.virtual_coin_miner.cwu_to_next_coin";
}
