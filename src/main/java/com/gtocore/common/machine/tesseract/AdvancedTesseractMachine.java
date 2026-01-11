package com.gtocore.common.machine.tesseract;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.ae2.IPatternProviderLogic;
import com.gtolib.api.ae2.PatternProviderTargetCache;
import com.gtolib.api.ae2.machine.ICustomCraftingMachine;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.holder.BooleanHolder;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.helpers.patternprovider.PatternProviderTarget;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static com.gtocore.common.machine.tesseract.DirectedTesseractMachine.HIGHLIGHT_TEXT;

public class AdvancedTesseractMachine extends MetaMachine implements IFancyUIMachine, IMachineLife, ICustomCraftingMachine, IMultiTesseract {

    public static final Multiset<ImmutableList<Long>> HIGHLIGHTS = HashMultiset.create();

    private final WeakReference<BlockEntity>[] blockEntityReference = new WeakReference[20];

    @Persisted
    @DescSynced
    public final List<BlockPos> poss = new ArrayList<>(20);

    @Persisted
    protected NotifiableItemStackHandler inventory;

    @Persisted
    private boolean roundRobin;

    @Getter
    private final List<IItemHandler> itemHandlers = new ArrayList<>(20);
    @Getter
    private final List<IFluidHandler> fluidHandlers = new ArrayList<>(20);

    @Getter
    @Setter
    private boolean called;

    public AdvancedTesseractMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 20, IO.NONE, IO.NONE);
        inventory.storage.setOnContentsChangedAndfreeze(() -> {
            called = false;
            poss.clear();
            for (int i = 0; i < 20; i++) {
                blockEntityReference[i] = null;
                ItemStack card = inventory.storage.getStackInSlot(i);
                if (card.isEmpty()) continue;
                CompoundTag posTags = card.getTag();
                if (posTags == null || !posTags.contains("x") || !posTags.contains("y") || !posTags.contains("z")) continue;
                var pos = new BlockPos(posTags.getInt("x"), posTags.getInt("y"), posTags.getInt("z"));
                if (pos.equals(getPos())) continue;
                if (!poss.contains(pos)) {
                    poss.add(pos);
                }
            }
        });
    }

    @Override
    protected InteractionResult onScrewdriverClick(Player playerIn, InteractionHand hand, Direction gridSide, BlockHitResult hitResult) {
        if (!super.onScrewdriverClick(playerIn, hand, gridSide, hitResult).shouldSwing()) {
            roundRobin = !roundRobin;
            playerIn.displayClientMessage(Component.translatable(roundRobin ? "tooltip.ad_astra.distribution_mode.round_robin" : "tooltip.ad_astra.distribution_mode.sequential"), true);
            return InteractionResult.sidedSuccess(playerIn.level().isClientSide);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        IFancyUIMachine.super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GuiTextures.LIGHT_ON, GuiTextures.LIGHT_ON, () -> false,
                (clickData, pressed) -> {
                    if (clickData.isRemote && getLevel() != null) {
                        HIGHLIGHTS.add(poss.stream().map(BlockPos::asLong).collect(ImmutableList.toImmutableList()), 200);
                    }
                })
                .setTooltipsSupplier(pressed -> Collections.singletonList(Component.translatable(HIGHLIGHT_TEXT))));
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 18 * 5 + 16, 18 * 4 + 16);
        var container = new WidgetGroup(4, 4, 18 * 5 + 8, 18 * 4 + 8);
        int index = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                container.addWidget(new SlotWidget(inventory.storage, index++, 4 + x * 18, 4 + y * 18, true, true).setBackgroundTexture(GuiTextures.SLOT));
            }
        }
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);
        return group;
    }

    @Override
    public @Nullable IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        var cap = getCapability(ForgeCapabilities.ITEM_HANDLER, side);
        return cap != null ? cap.orElse(null) instanceof IItemHandlerModifiable m ? m : null : null;
    }

    @Override
    public @Nullable IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        var cap = getCapability(ForgeCapabilities.FLUID_HANDLER, side);
        return cap != null ? cap.orElse(null) instanceof IFluidHandlerModifiable m ? m : null : null;
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(int i) {
        return getBlockEntity(poss.get(i), i);
    }

    @Override
    public int getTotalBlockEntities() {
        return poss.size();
    }

    public @Nullable BlockEntity getBlockEntity(@Nullable BlockPos pos, int i) {
        if (pos == null) return null;
        var reference = blockEntityReference[i];
        if (reference == null) {
            var be = getLevel().getBlockEntity(pos);
            if (be != null) {
                blockEntityReference[i] = new WeakReference<>(be);
                return be;
            } else {
                poss.set(i, null);
            }
        } else {
            var blockEntity = reference.get();
            if (blockEntity == null || blockEntity.isRemoved()) {
                blockEntity = getLevel().getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntityReference[i] = new WeakReference<>(blockEntity);
                    return blockEntity;
                } else {
                    poss.set(i, null);
                }
            } else {
                return blockEntity;
            }
        }
        return null;
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    @Override
    public boolean customPush() {
        return roundRobin;
    }

    @Override
    public IPatternProviderLogic.PushResult pushPattern(IPatternProviderLogic logic, IActionSource actionSource, BooleanHolder success, Operate operate, Set<AEKey> patternInputs, IPatternDetails patternDetails, ObjectHolder<KeyCounter[]> inputHolder, Supplier<IPatternProviderLogic.PushResult> pushPatternSuccess, BooleanSupplier canPush, Direction direction, Direction adjBeSide) {
        var size = poss.size();
        List<PatternProviderTarget> targets = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            var targetPos = poss.get(i);
            if (targetPos == null) {
                continue;
            }
            var target = PatternProviderTargetCache.find(getBlockEntity(targetPos, i), logic, adjBeSide, actionSource, targetPos.asLong());
            if (target == null) continue;
            targets.add(target);
        }
        int count = 1000;
        while (count > 0) {
            count--;
            boolean done = true;
            for (var target : targets) {
                if (target.containsPatternInput(patternInputs)) continue;
                var result = operate.pushTarget(patternDetails, inputHolder, pushPatternSuccess, canPush, direction, target, false);
                if (result.success()) success.value = true;
                if (result.needBreak()) return result;
                if (result == IPatternProviderLogic.PushResult.SUCCESS) done = false;
            }
            if (done) break;
        }
        return IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH;
    }

    @Override
    public boolean onMarkerInteract(Player player, List<TesseractDirectedTarget> targets) {
        if (targets.isEmpty()) {
            ;
            return false;
        }
        if (getLevel() == null || getLevel().isClientSide()) {
            return true;
        }
        int availableCards = Arrays.stream(inventory.storage.stacks).filter(i -> !i.isEmpty()).toArray().length;
        inventory.storage.clear();
        var iterator = targets.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            var target = iterator.next();
            if (i >= 20) break;
            var pos = target.pos().pos();
            if (pos.equals(getPos())) {
                iterator.remove();
                continue;
            }
            ItemStack card = ItemStack.EMPTY;
            if (availableCards > 0) {
                availableCards--;
                card = GTOItems.COORDINATE_CARD.asItem().getDefaultInstance();
            }
            if (card.isEmpty()) {
                var idx = player.getInventory().findSlotMatchingItem(GTOItems.COORDINATE_CARD.asItem().getDefaultInstance());
                if (idx < 0) {
                    card = ItemStack.EMPTY;
                } else {
                    card = player.getInventory().removeItem(idx, 1);
                }
            }
            ae:
            if (card.isEmpty()) {
                var meStorage = IEnhancedPlayer.getMEStorageService((ServerPlayer) player);
                if (meStorage == null) {
                    break ae;
                }
                var cardNum = meStorage.getInventory().extract(AEItemKey.of(GTOItems.COORDINATE_CARD.asItem()), 1, Actionable.MODULATE, IActionSource.ofPlayer(player));
                if (cardNum <= 0) {
                    break ae;
                }
                card = GTOItems.COORDINATE_CARD.asItem().getDefaultInstance();
            }
            if (card.isEmpty()) {
                player.displayClientMessage(Component.translatable(WRITE_FAIL_TEXT), true);
                return true;
            }
            CompoundTag posTags = card.getOrCreateTag();
            posTags.putInt("x", pos.getX());
            posTags.putInt("y", pos.getY());
            posTags.putInt("z", pos.getZ());
            inventory.storage.setStackInSlot(i, card);
            i++;
        }
        if (availableCards > 0) {
            for (; availableCards > 0; availableCards--) {
                Block.popResource(getLevel(), getPos(), GTOItems.COORDINATE_CARD.asItem().getDefaultInstance());
            }
        }
        player.displayClientMessage(Component.translatable(WRITE_SUCCESS_TEXT), true);
        return true;
    }
}
