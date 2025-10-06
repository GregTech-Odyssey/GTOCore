package com.gtocore.mixin.eio;

import com.gtocore.integration.eio.ITravelHandlerHook;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import com.enderio.api.travel.ITravelTarget;
import com.enderio.base.common.handler.TravelHandler;
import com.enderio.base.common.travel.TravelSavedData;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.stream.Stream;

@Mixin(TravelHandler.class)
public class TravelHandlerMixin implements ITravelHandlerHook {

    @Redirect(method = { "lambda$getTeleportAnchorTarget$8", "lambda$getElevatorAnchorTarget$13" }, at = @At(value = "INVOKE", target = "Lcom/enderio/base/common/handler/TravelHandler;isTeleportPositionClear(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;"), remap = false)
    private static Optional<Double> gto$redirectIsTeleportPositionClear2(BlockGetter level, BlockPos target) {
        return ITravelHandlerHook.gto$isTeleportPositionAndSurroundingClear(level, target);
    }

    @Redirect(method = "blockTeleportTo", at = @At(value = "INVOKE", target = "Lcom/enderio/base/common/handler/TravelHandler;isTeleportPositionClear(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;"), remap = false)
    private static Optional<Double> gto$redirectIsTeleportPositionClear(BlockGetter level, BlockPos target, @Local(argsOnly = true) Player player) {
        return player.level().isClientSide() ? Optional.of(2d) : TravelHandler.isTeleportPositionClear(level, target);
    }

    @Redirect(method = "getTeleportAnchorTarget", at = @At(value = "INVOKE", target = "Lcom/enderio/base/common/travel/TravelSavedData;getTravelTargetsInItemRange(Lnet/minecraft/core/BlockPos;)Ljava/util/stream/Stream;"), remap = false)
    private static Stream<ITravelTarget> gto$filterAnchorTargets(TravelSavedData instance, BlockPos center, @Local(argsOnly = true) Player player) {
        return ITravelHandlerHook.filterTargets(player, instance.getTravelTargetsInItemRange(center));
    }

    @WrapMethod(method = "blockTeleportTo", remap = false)
    private static boolean gto$wrapIsTeleportPositionClear(Level level, Player player, ITravelTarget target, boolean sendToServer, Operation<Boolean> original) {
        return original.call(level, player, target, sendToServer) ||
                original.call(level, player, gtocore$offset(target, Direction.NORTH), sendToServer) ||
                original.call(level, player, gtocore$offset(target, Direction.SOUTH), sendToServer) ||
                original.call(level, player, gtocore$offset(target, Direction.EAST), sendToServer) ||
                original.call(level, player, gtocore$offset(target, Direction.WEST), sendToServer);
    }

    @Unique
    private static ITravelTarget gtocore$offset(ITravelTarget target, Direction dir) {
        return new ITravelTarget() {

            @Override
            public @NotNull ResourceLocation getSerializationName() {
                return target.getSerializationName();
            }

            @Override
            public @NotNull BlockPos getPos() {
                return target.getPos().relative(dir);
            }

            @Override
            public @NotNull CompoundTag save() {
                return target.save();
            }

            @Override
            public int getItem2BlockRange() {
                return target.getItem2BlockRange();
            }

            @Override
            public int getBlock2BlockRange() {
                return target.getBlock2BlockRange();
            }
        };
    }
}
