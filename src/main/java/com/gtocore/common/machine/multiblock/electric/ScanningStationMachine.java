package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.ScanningHolderMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtocore.data.recipe.builder.research.ExResearchManager.*;
import static com.gtolib.utils.RegistriesUtils.getItem;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ScanningStationMachine extends ElectricMultiblockMachine {

    private ScanningHolderMachine objectHolder;

    public ScanningStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof ScanningHolderMachine scanningHolder) {
                if (scanningHolder.getFrontFacing() != getFrontFacing().getOpposite()) {
                    onStructureInvalid();
                    return;
                }
                this.objectHolder = scanningHolder;
                // 添加物品处理器（包含扫描槽、催化剂槽和数据槽）
                addHandlerList(RecipeHandlerList.of(IO.IN, scanningHolder.getAsHandler()));
            }
        }

        // 必须同时有扫描部件和计算提供者
        if (objectHolder == null) {
            onStructureInvalid();
        }
    }

    @Override
    public boolean checkPattern() {
        boolean isFormed = super.checkPattern();
        if (isFormed && objectHolder != null && objectHolder.getFrontFacing() != getFrontFacing().getOpposite()) {
            onStructureInvalid();
        }
        return isFormed;
    }

    @Override
    public void onStructureInvalid() {
        if (objectHolder != null) {
            objectHolder.setLocked(false);
            objectHolder = null;
        }
        super.onStructureInvalid();
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .setWorkingStatusKeys("gtceu.multiblock.idling", "gtceu.multiblock.work_paused",
                        "gtocore.machine.analysis")
                .addEnergyUsageLine(energyContainer)
                .addEnergyTierLine(tier)
                .addWorkingStatusLine()
                .addProgressLineOnlyPercent(recipeLogic.getProgressPercent());
    }

    @Override
    public boolean matchRecipe(Recipe recipe) {
        return RecipeRunner.matchRecipeInput(this, recipe);
    }

    @Override
    public boolean handleRecipeIO(Recipe originalRecipe, IO io) {
        report();

        if (io == IO.IN) {
            objectHolder.setLocked(true);
            return true;
        }

        if (getRecipeLogic().getLastRecipe() == null) {
            objectHolder.setLocked(false);
            return true;
        }

        var catalyst = getRecipeLogic().getLastRecipe().getInputContents(ItemRecipeCapability.CAP);
        if (ItemRecipeCapability.CAP.of(catalyst.get(0).content).getItems()[0].getItem() != objectHolder.getCatalystItem(false).getItem()) {
            ItemStack hold = objectHolder.getHeldItem(true);
            objectHolder.setHeldItem(objectHolder.getCatalystItem(true));
            objectHolder.setCatalystItem(hold);
            objectHolder.setLocked(false);
            return true;
        }

        objectHolder.setHeldItem(ItemStack.EMPTY);
        ItemStack outputItem = ItemStack.EMPTY;
        var contents = getRecipeLogic().getLastRecipe().getOutputContents(ItemRecipeCapability.CAP);
        if (!contents.isEmpty()) {
            outputItem = ItemUtils.getFirstSized((Ingredient) contents.get(0).content).copy();
        }
        if (!outputItem.isEmpty()) {
            objectHolder.setDataItem(outputItem);
        }

        objectHolder.setLocked(false);
        return true;
    }

    private static void report() {
        StringBuilder scanning_report = new StringBuilder("# ***数据晶片(扫描)-统计***\n  ");
        scanning_report.append("|   扫描 ID  | 数据等级 | 介质等级 |    扫描物    |   数量   |\n  ");
        scanning_report.append("|-----------|--------|---------|-------------|---------|\n  ");

        for (var entry : ScanningMap.int2ObjectEntrySet()) {
            int serial = entry.getIntKey();
            String scanningId = entry.getValue();
            String[] parts = scanningId.split("-", 3);
            String countPart = parts[0];
            int count = Integer.parseInt(countPart.substring(0, countPart.length() - 1));
            String state = countPart.substring(countPart.length() - 1);

            if (state.equals("i")) {
                Item item = getItem(parts[1], parts[2]);
                ItemStack stack = new ItemStack(item, 1);
                String scanningThing = "item  - " + stack.getHoverName().getString();
                scanning_report.append(String.format("|0x%08X|%d|%d|%s|%d|\n  ", serial, ExtractDataTier(serial), ExtractDataCrystal(serial), scanningThing, count));
                continue;
            }

            if (state.equals("f")) {
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(parts[1], parts[2]));
                String fluidState;
                if (parts[2].contains("gas")) fluidState = "气态 ";
                else if (parts[2].contains("liquid")) fluidState = "液态 ";
                else if (parts[2].contains("molten")) fluidState = "熔融 ";
                else if (parts[2].contains("plasma")) fluidState = "等离子态 ";
                else fluidState = "";
                String scanningThing = "fluid - " + fluidState + I18n.get(fluid.getFluidType().getDescriptionId());
                scanning_report.append(String.format("|0x%08X|%d|%d|%s|%d|\n  ", serial, ExtractDataTier(serial), ExtractDataCrystal(serial), scanningThing, count));
            }
        }

        StringBuilder analyze_report = new StringBuilder("# ***数据晶片(研究)-统计***\n  ");
        analyze_report.append("|   研究 ID  | 数据等级 | 介质等级 |    研究名    |\n  ");
        analyze_report.append("|-----------|--------|---------|-------------|\n  ");

        for (var entry : AnalyzeMap.int2ObjectEntrySet()) {
            int serial = entry.getIntKey();
            String analyzeId = "data." + entry.getValue();
            analyze_report.append(String.format("|%08X|%d|%d|%s|\n  ", serial, ExtractDataTier(serial), ExtractDataCrystal(serial), I18n.get(analyzeId)));
        }

        try {
            Path logDir = Paths.get("logs", "report");
            if (!Files.exists(logDir)) Files.createDirectories(logDir);
            Path reportPath1 = logDir.resolve("scanning_report.md");
            Path reportPath2 = logDir.resolve("analyze_report.md");
            try (BufferedWriter writer = Files.newBufferedWriter(reportPath1)) {
                writer.write(scanning_report.toString());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(reportPath2)) {
                writer.write(analyze_report.toString());
            }
            GTOCore.LOGGER.info("数据晶片(扫描/研究)统计报告已生成:run\\logs\\report\\scanning_report.md & analyze_report.md");
        } catch (Exception e) {
            GTOCore.LOGGER.error("生成数据晶片(扫描/研究)统计报告失败", e);
        }
    }
}
