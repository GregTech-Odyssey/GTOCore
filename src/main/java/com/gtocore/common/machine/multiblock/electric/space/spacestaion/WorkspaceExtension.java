package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.machine.feature.multiblock.IMultiStructureMachine;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTMachines;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtocore.api.machine.part.ILargeSpaceStationMachine.ConnectType.MODULE;

@DataGeneratorScanned
public class WorkspaceExtension extends Extension implements IMultiStructureMachine {

    @Persisted
    @DescSynced
    private int length = 2;

    public WorkspaceExtension(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    @Override
    public List<BlockPattern> getMultiPattern() {
        return Stream.of(2, 9).map(i -> patternAtLength(i).apply(getDefinition())).toList();
    }

    @Override
    public BlockPattern getPattern() {
        return patternAtLength(length).apply(getDefinition());
    }

    @Override
    public Set<BlockPos> getModulePositions() {
        return ILargeSpaceStationMachine.twoWayPositionFunction(17 + 10 + length * 6 - 4).apply(this);
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfigurator() {

            @Override
            public Component getTitle() {
                return Component.translatable(REPEAT_LENGTH);
            }

            @Override
            public IGuiTexture getIcon() {
                return GTOGuiTextures.PARALLEL_CONFIG;
            }

            @Override
            public Widget createConfigurator() {
                WidgetGroup group = new WidgetGroup(0, 0, 100, 20);
                var intInput = new IntInputWidget(() -> length, p -> {
                    if (p != length) requestCheck();
                    length = p;
                }).setMin(2).setMax(9).setValue(length);
                return group.addWidget(intInput);
            }
        });
    }

    private static final String[][] BLOCK = {
            { "      ", "      ", "      ", "      ", "      ", " LpLpp", "      ", "FFFFFF", "      ", "      ", "      ", "FFFFFF", "      ", " LpLpp", "      ", "      ", "      ", "      ", "      " },
            { "      ", "      ", "      ", "      ", "     C", "LLLLLC", "OLLLOC", "LLLLLC", "LLLLLC", "GGGGGC", "LLLLLC", "LLLLLC", "OLLLOC", "LLLLLC", "     C", "      ", "      ", "      ", "      " },
            { "      ", "      ", "      ", "     C", "GGGGGG", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "GGGGGG", "     C", "      ", "      ", "      " },
            { "      ", "      ", "     C", "GGGGGG", "HIIIHH", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "HIIIHH", "GGGGGG", "     C", "      ", "      " },
            { "      ", "     C", "GGGGGG", "HIIIHH", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "HIIIHH", "GGGGGG", "     C", "      " },
            { "      ", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "      " },
            { "      ", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "      " },
            { "FFFFFF", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "FFFFFF" },
            { "      ", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "      " },
            { "      ", "GGGGGC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "GGGGGC", "      " },
            { "      ", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "      " },
            { "FFFFFF", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "FFFFFF" },
            { "      ", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "      " },
            { "      ", "LLLLLC", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "LLLLLC", "      " },
            { "      ", "     C", "GGGGGG", "HIIIHH", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "HIIIHH", "GGGGGG", "     C", "      " },
            { "      ", "      ", "     C", "GGGGGG", "HIIIHH", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "HIIIHH", "GGGGGG", "     C", "      ", "      " },
            { "      ", "      ", "      ", "     C", "GGGGGG", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "pppppp", "GGGGGG", "     C", "      ", "      ", "      " },
            { "      ", "      ", "      ", "      ", "     C", "LLLLLC", "OLLLOC", "LLLLLC", "LLLLLC", "GGGGGC", "LLLLLC", "LLLLLC", "OLLLOC", "LLLLLC", "     C", "      ", "      ", "      ", "      " },
            { "      ", "      ", "      ", "      ", "      ", " LpLpp", "      ", "FFFFFF", "      ", "      ", "      ", "FFFFFF", "      ", " LpLpp", "      ", "      ", "      ", "      ", "      " },

    };
    private static final String[][] HEAD = {
            { "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "               FF", "                 ", "                 ", "                 ", "               FF", "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "                 " },
            { "                 ", "                 ", "                 ", "                 ", "                C", "                C", "               EC", "              FLC", "              NLC", "              NLC", "              NLC", "              FLC", "                C", "                C", "                C", "                 ", "                 ", "                 ", "                 " },
            { "                 ", "                 ", "                 ", "                C", "               EG", "               Ep", "              FHp", "            FFAAp", "              AAp", "              AAp", "              AAp", "            FFAAp", "              FHp", "               Ep", "                G", "                C", "                 ", "                 ", "                 " },
            { "                 ", "                 ", "                C", "               EG", "               EH", "              FHp", "              AAp", "           FLLppp", "           FLLppp", "      FFFFFFMLppp", "           FLLppp", "           FLLppp", "              AAp", "              FHp", "               EH", "                G", "                C", "                 ", "                 " },
            { "                 ", "                C", "               EG", "               EH", "              FHp", "            LLAAp", "         KppLLppp", "       EEKEEGpppp", "       EEKEEGpppp", "      FEEKEEGpppp", "       EEKEEGpppp", "       EEKEEGpppp", "         KppLLppp", "            LLAAp", "              FHp", "               EH", "                G", "                C", "                 " },
            { "                 ", "                C", "               Ep", "              FHp", "            LLAAp", "         KppLLppp", "       EEKEEGpppp", "      AHJJJJppppp", "     FAHJJJJppppp", "  CCFFAIJJJJppppp", "     FAHJJJJppppp", "      AHJJJJppppp", "       EEKEEGpppp", "         KppLLppp", "            LLAAp", "              FHp", "               Ep", "                C", "                 " },
            { "                 ", "               EC", "              FHp", "              AAp", "         KppLLppp", "       EEKEEGpppp", "      AHHHHHGpppp", "     FApppppppppp", "   EEAGpppppppppp", "   CCAGpppppppppp", "   EEAGpppppppppp", "     FApppppppppp", "      AHHHHHGpppp", "       EEKEEGpppp", "         KppLLppp", "              AAp", "              FHp", "                C", "                 " },
            { "               FF", "              FLC", "            FFAAp", "           FLLppp", "       EEKEEGpppp", "      AHJJJJppppp", "     FApppppppppp", "   EEAGpppppppppp", "AAAAAAppppppppppp", "ABDDAAppppppppppp", "AAAAAAppppppppppp", "   EEAGpppppppppp", "     FApppppppppp", "      AHJJJJppppp", "       EEKEEGpppp", "           FLLppp", "            FFAAp", "              FLC", "               FF" },
            { "                 ", "              NLC", "              AAp", "           FLLppp", "       EEKEEGpppp", "     FAHJJJJppppp", "   EEAGpppppppppp", "   AAAppppppppppp", "ppppppppppppppppp", "ppppppppppppppppp", "ppppppppppppppppp", "   AAAppppppppppp", "   EEAGpppppppppp", "     FAHJJJJppppp", "       EEKEEGpppp", "           FLLppp", "              AAp", "              NLC", "                 " },
            { "                 ", "              NLC", "              AAp", "      FFFFFFMLppp", "      FEEKEEGpppp", "  CCFFAIJJJJppppp", "   CCAGpppppppppp", "   DAAppppppppppp", "ppppppppppppppppp", "ppppppppppppppppp", "ppppppppppppppppp", "   DAAppppppppppp", "   CCAGpppppppppp", "  CCFFAIJJJJppppp", "      FEEKEEGpppp", "      FFFFFFMLppp", "              AAp", "              NLC", "                 " },
            { "                 ", "              NLC", "              AAp", "           FLLppp", "       EEKEEGpppp", "     FAHJJJJppppp", "   EEAGpppppppppp", "   AAAppppppppppp", "ppppppppppppppppp", "ppppppppppppppppp", "ppppppppppppppppp", "   AAAppppppppppp", "   EEAGpppppppppp", "     FAHJJJJppppp", "       EEKEEGpppp", "           FLLppp", "              AAp", "              NLC", "                 " },
            { "               FF", "              FLC", "            FFAAp", "           FLLppp", "       EEKEEGpppp", "      AHJJJJppppp", "     FApppppppppp", "   EEAGpppppppppp", "   AAAppppppppppp", "   DAAppppppppppp", "   AAAppppppppppp", "   EEAGpppppppppp", "     FApppppppppp", "      AHJJJJppppp", "       EEKEEGpppp", "           FLLppp", "            FFAAp", "              FLC", "               FF" },
            { "                 ", "               EC", "              FHp", "              AAp", "         KppLLppp", "       EEKEEGpppp", "      AHHHHHGpppp", "     FApppppppppp", "   EEAGpppppppppp", "   CCAGpppppppppp", "   EEAGpppppppppp", "     FApppppppppp", "      AHHHHHGpppp", "       EEKEEGpppp", "         KppLLppp", "              AAp", "              FHp", "               EC", "                 " },
            { "                 ", "                C", "               Ep", "              FHp", "            LLAAp", "         KppLLppp", "       EEKEEGpppp", "      AHJJJJppppp", "     FAHJJJJppppp", "  CCFFAIJJJJppppp", "     FAHJJJJppppp", "      AHJJJJppppp", "       EEKEEGpppp", "         KppLLppp", "            LLAAp", "              FHp", "               Ep", "                C", "                 " },
            { "                 ", "                C", "               EG", "               EH", "              FHp", "            LLAAp", "         KppLLppp", "       EEKEEGpppp", "       EEKEEGpppp", "      FEEKEEGpppp", "       EEKEEGpppp", "       EEKEEGpppp", "         KppLLppp", "            LLAAp", "              FHp", "               EH", "               EG", "                C", "                 " },
            { "                 ", "                 ", "                C", "               EG", "               EH", "              FHp", "              AAp", "           FLLppp", "           FLLppp", "      FFFFFFMLppp", "           FLLppp", "           FLLppp", "              AAp", "              FHp", "               EH", "               EG", "                C", "                 ", "                 " },
            { "                 ", "                 ", "                 ", "                C", "               EG", "               Ep", "              FHp", "            FFAAp", "              AAp", "              AAp", "              AAp", "            FFAAp", "              FHp", "               Ep", "               EG", "                C", "                 ", "                 ", "                 " },
            { "                 ", "                 ", "                 ", "                 ", "                C", "                C", "               EC", "              FLC", "              NLC", "              NLC", "              NLC", "              FLC", "               EC", "                C", "                C", "                 ", "                 ", "                 ", "                 " },
            { "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "               FF", "                 ", "                 ", "                 ", "               FF", "                 ", "                 ", "                 ", "                 ", "                 ", "                 ", "                 " },
    };

    private static final String[][] TAIL = {
            { "          ", "          ", "          ", "          ", "          ", "          ", "          ", "F         ", "          ", "          ", "          ", "F         ", "          ", "          ", "          ", "          ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "          ", "          ", "          ", "E         ", "LF        ", "LN        ", "LN        ", "LN        ", "LF        ", "E         ", "          ", "          ", "          ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "          ", "E         ", "E         ", "HF        ", "AAFF      ", "AA        ", "AA        ", "AA        ", "AAFF      ", "HF        ", "E         ", "E         ", "          ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "E         ", "E         ", "HF        ", "AA        ", "ppLLF     ", "ppLLF     ", "ppLMFF    ", "ppLLF     ", "ppLLF     ", "AA        ", "HF        ", "E         ", "E         ", "          ", "          ", "          " },
            { "          ", "          ", "E         ", "E         ", "HF        ", "AALL      ", "ppLL      ", "pppGE     ", "pppGE     ", "pppGEF    ", "pppGE     ", "pppGE     ", "ppLL      ", "AALL      ", "HF        ", "E         ", "E         ", "          ", "          " },
            { "          ", "          ", "E         ", "HF        ", "AALL      ", "ppLL      ", "pppGE     ", "ppppAA    ", "ppppAAF   ", "ppppAAFFCC", "ppppAAF   ", "ppppAA    ", "pppGE     ", "ppLL      ", "AALL      ", "HF        ", "E         ", "          ", "          " },
            { "          ", "E         ", "HF        ", "AA        ", "ppLL      ", "pppGE     ", "pppGIA    ", "pppppAF   ", "pppppGAEE ", "pppppGACC ", "pppppGAEE ", "pppppAF   ", "pppGIA    ", "pppGE     ", "ppLL      ", "AA        ", "HF        ", "E         ", "          " },
            { "F         ", "LF        ", "AAFF      ", "ppLLF     ", "pppGE     ", "ppppAA    ", "pppppAF   ", "pppppGAEE ", "ppppppAAA ", "ppppppAcD ", "ppppppAAA ", "pppppGAEE ", "pppppAF   ", "ppppAA    ", "pppGE     ", "ppLLF     ", "AAFF      ", "LF        ", "F         " },
            { "          ", "LN        ", "AA        ", "ppLLF     ", "pppGE     ", "ppppAAF   ", "pppppGAEE ", "ppppppAAA ", "pppppppppp", "pppppppppp", "pppppppppp", "ppppppAAA ", "pppppGAEE ", "ppppAAF   ", "pppGE     ", "ppLLF     ", "AA        ", "LN        ", "          " },
            { "          ", "LN        ", "AA        ", "ppLMFF    ", "pppGEF    ", "ppppAAFFCC", "pppppGACC ", "ppppppAcD ", "pppppppppp", "pppppppppp", "pppppppppp", "ppppppAcD ", "pppppGACC ", "ppppAAFFCC", "pppGEF    ", "ppLMFF    ", "AA        ", "LN        ", "          " },
            { "          ", "LN        ", "AA        ", "ppLLF     ", "pppGE     ", "ppppAAF   ", "pppppGAEE ", "ppppppAAA ", "pppppppppp", "pppppppppp", "pppppppppp", "ppppppAAA ", "pppppGAEE ", "ppppAAF   ", "pppGE     ", "ppLLF     ", "AA        ", "LN        ", "          " },
            { "F         ", "LF        ", "AAFF      ", "ppLLF     ", "pppGE     ", "ppppAA    ", "pppppAF   ", "pppppGAEE ", "ppppppAAA ", "ppppppAcD ", "ppppppAAA ", "pppppGAEE ", "pppppAF   ", "ppppAA    ", "pppGE     ", "ppLLF     ", "AAFF      ", "LF        ", "F         " },
            { "          ", "E         ", "HF        ", "AA        ", "ppLL      ", "pppGE     ", "pppGIA    ", "pppppAF   ", "pppppGAEE ", "pppppGACC ", "pppppGAEE ", "pppppAF   ", "pppGIA    ", "pppGE     ", "ppLL      ", "AA        ", "HF        ", "          ", "          " },
            { "          ", "          ", "E         ", "HF        ", "AALL      ", "ppLL      ", "pppGE     ", "ppppAA    ", "ppppAAF   ", "ppppAAFFCC", "ppppAAF   ", "ppppAA    ", "pppGE     ", "ppLL      ", "AALL      ", "HF        ", "E         ", "          ", "          " },
            { "          ", "          ", "E         ", "E         ", "HF        ", "AALL      ", "ppLL      ", "pppGE     ", "pppGE     ", "pppGEF    ", "pppGE     ", "pppGE     ", "ppLL      ", "AALL      ", "HF        ", "E         ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "E         ", "E         ", "HF        ", "AA        ", "ppLLF     ", "ppLLF     ", "ppLMFF    ", "ppLLF     ", "ppLLF     ", "AA        ", "HF        ", "E         ", "          ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "          ", "E         ", "E         ", "HF        ", "AAFF      ", "AA        ", "AA        ", "AA        ", "AAFF      ", "HF        ", "E         ", "          ", "          ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "          ", "          ", "          ", "E         ", "LF        ", "LN        ", "LN        ", "LN        ", "LF        ", "          ", "          ", "          ", "          ", "          ", "          ", "          " },
            { "          ", "          ", "          ", "          ", "          ", "          ", "          ", "F         ", "          ", "          ", "          ", "F         ", "          ", "          ", "          ", "          ", "          ", "          ", "          " },
    };

    public static Function<MultiblockMachineDefinition, BlockPattern> patternAtLength(int length) {
        String[][] pattern = new String[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                pattern[i][j] = HEAD[i][j] +
                        String.valueOf(BLOCK[i][j]).repeat(length) +
                        TAIL[i][j];
            }
        }
        return definition -> {
            var builder = FactoryBlockPattern.start(definition);
            for (String[] aisle : pattern) {
                builder = builder.aisle(aisle);
            }

            return builder.where('A', blocks(GTOBlocks.TITANIUM_ALLOY_INTERNAL_FRAME.get()))
                    .where('B', controller(blocks(definition.get())))
                    .where('C', blocks(GTOBlocks.ALUMINUM_ALLOY_7050_SUPPORT_MECHANICAL_BLOCK.get()))
                    .where('c', MODULE.traceabilityPredicate.get())
                    .where('D', blocks(GTOBlocks.SPACECRAFT_DOCKING_CASING.get()))
                    .where('E', blocks(GTOBlocks.ALUMINUM_ALLOY_2090_SKIN_MECHANICAL_BLOCK.get()))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.StainlessSteel316)))
                    .where('G', blocks(GTOBlocks.PRESSURE_RESISTANT_HOUSING_MECHANICAL_BLOCK.get()))
                    .where('H', blocks(GTOBlocks.SPACECRAFT_SEALING_MECHANICAL_BLOCK.get()))
                    .where('I', GTOPredicates.light())
                    .where('J', blocks(GTOBlocks.SPACE_STATION_CONTROL_CASING.get()))
                    .where('K', blocks(GTOBlocks.ALUMINUM_ALLOY_8090_SKIN_MECHANICAL_BLOCK.get()))
                    .where('L', blocks(GTOBlocks.TITANIUM_ALLOY_PROTECTIVE_MECHANICAL_BLOCK.get()))
                    .where('M', blocks(GTOBlocks.SPACE_ENGINE_NOZZLE.get()))
                    .where('N', blocks(GTOBlocks.LOAD_BEARING_STRUCTURAL_STEEL_MECHANICAL_BLOCK.get()))
                    .where('O', blocks(Stream.of(GTMachines.HULL).map(MachineDefinition::get).toArray(IMachineBlock[]::new)))
                    .where('p', ISpacePredicateMachine.innerBlockPredicate.get())
                    .where(' ', any())
                    .build();
        };
    }

    @RegisterLanguage(cn = "工作区扩展舱长度", en = "Workspace Extension Length")
    public static final String REPEAT_LENGTH = "gtocore.machine.space_station.workspace_extension.repeat_length";
}
