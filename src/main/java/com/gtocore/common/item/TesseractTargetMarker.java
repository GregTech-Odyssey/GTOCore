package com.gtocore.common.item;

import com.gtocore.common.machine.tesseract.ITesseractMarkerInteractable;
import com.gtocore.common.machine.tesseract.TesseractDirectedTarget;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class TesseractTargetMarker implements IInteractionItem {

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        if (isTesseractTargetMarker(itemStack)) {
            var player = context.getPlayer();
            var level = context.getLevel();
            var pos = context.getClickedPos();
            var face = context.getClickedFace();
            if (level.isClientSide() || player == null) {
                return InteractionResult.PASS;
            }
            if (player.isShiftKeyDown()) {
                if (!removePatternFace(itemStack, level.dimension(), pos, face)) return InteractionResult.PASS;
            } else {
                addPatternFace(itemStack, level.dimension(), pos, face, true);
            }
            return InteractionResult.SUCCESS;
        }
        return IInteractionItem.super.onItemUseFirst(itemStack, context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        if (player.isShiftKeyDown()) {
            Vec3 playerPos = player.getEyePosition();
            Vec3 lookVec = player.getLookAngle().normalize();
            double range = player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
            Vec3 toPos = playerPos.add(lookVec.scale(range));

            ClipContext clipCtx = new ClipContext(playerPos, toPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null);
            BlockHitResult bhr = level.clip(clipCtx);
            if (bhr.getType() == HitResult.Type.MISS) {
                ItemStack itemStack = player.getItemInHand(usedHand);
                if (isTesseractTargetMarker(itemStack)) {
                    clearAllPatternFaces(itemStack);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
                }
            }
        }
        return IInteractionItem.super.use(item, level, player, usedHand);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack itemStack = event.getEntity().getMainHandItem();

        if (isTesseractTargetMarker(itemStack)) {
            var player = event.getEntity();
            var level = event.getLevel();
            var pos = event.getPos();
            var face = event.getFace();
            event.setCanceled(true);
            if (level.isClientSide() || player == null) {
                event.setCancellationResult(InteractionResult.PASS);
                return;
            }
            if (player.isShiftKeyDown()) {
                if (level.getBlockEntity(pos) instanceof MetaMachineBlockEntity mbe &&
                        mbe.getMetaMachine() instanceof ITesseractMarkerInteractable interactable &&
                        interactable.onMarkerInteract(player, getAllPatternFaces(itemStack))) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                } else {
                    event.setCancellationResult(InteractionResult.PASS);
                }
                return;
            } else {
                addPatternFace(itemStack, level.dimension(), pos, face, false);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    public static boolean isTesseractTargetMarker(ItemStack stack) {
        if (stack.getItem() instanceof ComponentItem c) {
            return c.getComponents().stream().anyMatch(comp -> comp instanceof TesseractTargetMarker);
        }
        return false;
    }

    private static List<PatternFaceUnindexed> getFromNBT(ItemStack stack, boolean positive) {
        String key = positive ? "positive" : "negative";
        var nbt = stack.getOrCreateTag();
        if (nbt.contains(key)) {
            var list = nbt.getList(key, 10);
            List<PatternFaceUnindexed> result = new ArrayList<>();
            for (var i = 0; i < list.size(); i++) {
                var entry = list.getCompound(i);
                var dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(entry.getString("dim")));
                var pos = BlockPos.of(entry.getLong("pos"));
                var face = Direction.from3DDataValue(entry.getInt("face"));
                result.add(new PatternFaceUnindexed(GlobalPos.of(dim, pos), face));
            }
            return result;
        }
        return new ArrayList<>();
    }

    private static void putToNBT(ItemStack stack, boolean positive, List<PatternFaceUnindexed> patternFaces) {
        String key = positive ? "positive" : "negative";
        var nbt = stack.getOrCreateTag();
        var list = new net.minecraft.nbt.ListTag();
        for (var pf : patternFaces) {
            var entry = new net.minecraft.nbt.CompoundTag();
            entry.putString("dim", pf.pos().dimension().location().toString());
            entry.putLong("pos", pf.pos().pos().asLong());
            entry.putInt("face", pf.face().get3DDataValue());
            list.add(entry);
        }
        nbt.put(key, list);
    }

    public static void addPatternFace(ItemStack stack, ResourceKey<Level> dimension, BlockPos pos, Direction face, boolean addToPositive) {
        var globalPos = GlobalPos.of(dimension, pos);
        var patternFace = new PatternFaceUnindexed(globalPos, face);
        var patternFaces = getFromNBT(stack, addToPositive);
        var patternFacesF = getFromNBT(stack, !addToPositive);
        if (!patternFaces.contains(patternFace) && !patternFacesF.contains(patternFace)) {
            patternFaces.add(patternFace);
            putToNBT(stack, addToPositive, patternFaces);
        }
    }

    public static boolean removePatternFace(ItemStack stack, ResourceKey<Level> dimension, BlockPos pos, Direction face) {
        var globalPos = GlobalPos.of(dimension, pos);
        var patternFace = new PatternFaceUnindexed(globalPos, face);
        var patternFacesPositive = getFromNBT(stack, true);
        var patternFacesNegative = getFromNBT(stack, false);
        var removed = patternFacesPositive.remove(patternFace) || patternFacesNegative.remove(patternFace);
        if (removed) {
            putToNBT(stack, true, patternFacesPositive);
            putToNBT(stack, false, patternFacesNegative);
        }
        return removed;
    }

    public static void clearAllPatternFaces(ItemStack stack) {
        putToNBT(stack, true, new ArrayList<>());
        putToNBT(stack, false, new ArrayList<>());
    }

    public static List<TesseractDirectedTarget> getAllPatternFaces(ItemStack stack) {
        var result = ImmutableList.<TesseractDirectedTarget>builder();
        var index = 0;
        for (var pf : getFromNBT(stack, true)) {
            result.add(new TesseractDirectedTarget(pf.pos(), pf.face(), ++index));
        }
        index = 0;
        for (var pf : getFromNBT(stack, false)) {
            result.add(new TesseractDirectedTarget(pf.pos(), pf.face(), -(++index)));
        }
        return result.build();
    }

    private record PatternFaceUnindexed(GlobalPos pos, Direction face) implements Comparable<PatternFaceUnindexed> {

        @Override
        public int compareTo(@NotNull TesseractTargetMarker.PatternFaceUnindexed o) {
            var i = pos.pos().compareTo(o.pos.pos());
            if (i != 0) return i;
            return Integer.compare(face.ordinal(), o.face.ordinal());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PatternFaceUnindexed that = (PatternFaceUnindexed) o;
            return Objects.equals(pos(), that.pos()) && face() == that.face();
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos(), face());
        }
    }
}
