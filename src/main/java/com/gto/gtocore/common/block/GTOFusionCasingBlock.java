package com.gto.gtocore.common.block;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.api.block.IFusionCasingType;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;

public class GTOFusionCasingBlock extends FusionCasingBlock {

    public GTOFusionCasingBlock(Properties properties, IFusionCasingType casingType) {
        super(properties, casingType);
    }

    public static Block getCompressedCoilState(int tier) {
        return switch (tier) {
            case LuV -> GTOBlocks.IMPROVED_SUPERCONDUCTOR_COIL.get();
            case ZPM -> GTOBlocks.COMPRESSED_FUSION_COIL.get();
            case UV -> GTOBlocks.ADVANCED_COMPRESSED_FUSION_COIL.get();
            case UHV -> GTOBlocks.COMPRESSED_FUSION_COIL_MK2_PROTOTYPE.get();
            default -> GTOBlocks.COMPRESSED_FUSION_COIL_MK2.get();
        };
    }

    public static Block getCoilState(int tier) {
        return tier == UHV ? GTOBlocks.ADVANCED_FUSION_COIL.get() : GTOBlocks.FUSION_COIL_MK2.get();
    }

    public static Block getFrameState(int tier) {
        return switch (tier) {
            case LuV -> ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy);
            case ZPM -> ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Duranium);
            case UV -> ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Naquadria);
            case UHV -> ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Trinium);
            default -> ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Neutronium);
        };
    }

    public static Block getCasingState(int tier) {
        return switch (tier) {
            case LuV -> FUSION_CASING.get();
            case ZPM -> FUSION_CASING_MK2.get();
            case UV -> FUSION_CASING_MK3.get();
            case UHV -> GTOBlocks.FUSION_CASING_MK4.get();
            default -> GTOBlocks.FUSION_CASING_MK5.get();
        };
    }

    public static IFusionCasingType getCasingType(int tier) {
        return switch (tier) {
            case LuV -> FusionCasingBlock.CasingType.FUSION_CASING;
            case ZPM -> FusionCasingBlock.CasingType.FUSION_CASING_MK2;
            case UV -> FusionCasingBlock.CasingType.FUSION_CASING_MK3;
            case UHV -> GTOFusionCasingBlock.CasingType.FUSION_CASING_MK4;
            default -> GTOFusionCasingBlock.CasingType.FUSION_CASING_MK5;
        };
    }

    public enum CasingType implements IFusionCasingType, StringRepresentable {

        FUSION_CASING_MK4("fusion_casing_mk4"),
        FUSION_CASING_MK5("fusion_casing_mk5");

        private final String name;
        @Getter
        private final int harvestLevel;

        CasingType(String name) {
            this.name = name;
            this.harvestLevel = 3;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        @Override
        public ResourceLocation getTexture() {
            return GTOCore.id("block/casings/fusion/%s".formatted(this.name));
        }
    }
}