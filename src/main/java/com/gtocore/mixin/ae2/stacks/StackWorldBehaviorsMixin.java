package com.gtocore.mixin.ae2.stacks;

import com.gtolib.api.ae2.ExternalStorageCacheStrategy;
import com.gtolib.api.ae2.StorageExportCacheStrategy;
import com.gtolib.api.ae2.StorageImportCacheStrategy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.behaviors.StackExportStrategy;
import appeng.api.behaviors.StackImportStrategy;
import appeng.api.stacks.AEKeyType;
import appeng.parts.automation.StackWorldBehaviors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;

@Mixin(StackWorldBehaviors.class)
public class StackWorldBehaviorsMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static StackImportStrategy createImportFacade(ServerLevel level, BlockPos fromPos, Direction fromSide) {
        return StorageImportCacheStrategy.createImportFacade(level.getBlockEntity(fromPos.relative(fromSide)), fromPos, fromSide.getOpposite(), fromSide);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static StackExportStrategy createExportFacade(ServerLevel level, BlockPos fromPos, Direction fromSide) {
        return StorageExportCacheStrategy.createExportFacade(level.getBlockEntity(fromPos.relative(fromSide)), fromPos, fromSide.getOpposite(), fromSide);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static Map<AEKeyType, ExternalStorageStrategy> createExternalStorageStrategies(ServerLevel level, BlockPos fromPos, Direction fromSide) {
        return ExternalStorageCacheStrategy.createExternalStorageStrategies(level.getBlockEntity(fromPos.relative(fromSide)), fromPos, fromSide.getOpposite(), fromSide);
    }
}
