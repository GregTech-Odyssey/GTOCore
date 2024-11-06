package com.gto.gtocore.common.block;

import com.gto.gtocore.common.data.GTOBlocks;

import net.minecraft.world.item.Item;

import appeng.block.crafting.CraftingUnitBlock;
import appeng.block.crafting.ICraftingUnitType;
import com.tterrag.registrate.util.entry.BlockEntry;
import lombok.Getter;

public enum CraftingUnitType implements ICraftingUnitType {

    STORAGE_1M(1, "1m_storage"),
    STORAGE_4M(4, "4m_storage"),
    STORAGE_16M(16, "16m_storage"),
    STORAGE_64M(64, "64m_storage"),
    STORAGE_256M(256, "256m_storage"),
    STORAGE_MAX(-1, "max_storage");

    private final int storageMb;

    @Getter
    private final String affix;

    CraftingUnitType(int storageMb, String affix) {
        this.storageMb = storageMb;
        this.affix = affix;
    }

    @Override
    public long getStorageBytes() {
        return storageMb == -1 ? Long.MAX_VALUE : 1024L * 1024 * storageMb;
    }

    @Override
    public int getAcceleratorThreads() {
        return 0;
    }

    public BlockEntry<CraftingUnitBlock> getDefinition() {
        return switch (this) {
            case STORAGE_1M -> GTOBlocks.CRAFTING_STORAGE_1M;
            case STORAGE_4M -> GTOBlocks.CRAFTING_STORAGE_4M;
            case STORAGE_16M -> GTOBlocks.CRAFTING_STORAGE_16M;
            case STORAGE_64M -> GTOBlocks.CRAFTING_STORAGE_64M;
            case STORAGE_256M -> GTOBlocks.CRAFTING_STORAGE_256M;
            case STORAGE_MAX -> GTOBlocks.CRAFTING_STORAGE_MAX;
        };
    }

    @Override
    public Item getItemFromType() {
        return getDefinition().asItem();
    }
}
