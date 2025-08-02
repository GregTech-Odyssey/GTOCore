package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.api.gui.widget.ProspectingMapWidget;
import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;
import com.gregtechceu.gtceu.api.item.component.IItemLifeCycle;
import com.gregtechceu.gtceu.api.item.component.forge.IComponentCapability;
import com.gregtechceu.gtceu.common.item.ProspectorScannerBehavior;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

public final class ManaProspectorBehavior extends ProspectorScannerBehavior implements IComponentCapability, IItemLifeCycle, IDurabilityBar {

    private static final String MANA_KEY = "mana";
    private static final int CONSUMPTION_PER_TICK = 40;
    private static final int CONSUMPTION_PER_CHUNK = 2000;

    private final int radius;
    private int maxMana;
    private double costMultiplier;

    private ManaProspectorBehavior(int radius, ProspectorMode<?>... modes) {
        super(radius, 0, modes);
        this.radius = radius;
    }

    public static ManaProspectorBehavior create(Tier tier) {
        ManaProspectorBehavior behavior = new ManaProspectorBehavior(tier.radius, tier.modes);
        behavior.maxMana = tier.maxMana;
        behavior.costMultiplier = tier.manaCostMultiplier;
        return behavior;
    }

    private static int getMana(ItemStack stack) {
        // Placeholder for actual mana retrieval logic
        return stack.getOrCreateTag().getInt(MANA_KEY);
    }

    private static void addMana(ItemStack stack, int mana) {
        // Placeholder for actual mana setting logic
        stack.getOrCreateTag().putInt(MANA_KEY, Math.max(0, getMana(stack) + mana));
    }

    private static boolean checkAndConsumeMana(ItemStack stack, int consumption) {
        if (getMana(stack) < consumption) {
            return false;
        }
        addMana(stack, -consumption);
        return true;
    }

    @SuppressWarnings("resource")
    private static void preCancelScan(Player player, ItemStack stack) {
        player.playSound(BotaniaSounds.gaiaTeleport, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
        player.sendSystemMessage(Component.translatable("behavior.prospector.not_enough_energy"));
        player.closeContainer();
    }

    @Override
    public boolean drainEnergy(@NotNull ItemStack stack, boolean simulate) {
        int consumption = (int) (CONSUMPTION_PER_CHUNK * costMultiplier);
        if (getMana(stack) < consumption) {
            return false; // Not enough mana
        }
        if (!simulate) {
            addMana(stack, -consumption);
        }
        return true; // Successfully drained energy
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(stack); // No action on client side
        }
        if (!player.isShiftKeyDown()) {
            if (getMana(stack) < CONSUMPTION_PER_CHUNK * costMultiplier * radius * radius) {
                preCancelScan(player, stack);
                return InteractionResultHolder.fail(stack); // Not enough mana to start scanning
            }
        }
        return super.use(item, level, player, usedHand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.getGameTime() % 10 != 0 || level.isClientSide()) {
            return; // apply check every 10 ticks and only on server side
        }
        if (entity instanceof Player player && !player.isCreative()) {
            // check if the player is opening the prospector menu
            if (isSelected && player.containerMenu instanceof ModularUIContainer ui && ui.getModularUI().getFlatVisibleWidgetCollection().stream().anyMatch(ProspectingMapWidget.class::isInstance)) {
                if (!checkAndConsumeMana(stack, (int) (CONSUMPTION_PER_TICK * 10 * costMultiplier))) {
                    preCancelScan(player, stack);
                    player.closeContainer();
                }
            }
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(ItemStack itemStack, @NotNull Capability<T> cap) {
        if (cap == BotaniaForgeCapabilities.MANA_ITEM) {
            return BotaniaForgeCapabilities.MANA_ITEM.orEmpty(cap, LazyOptional.of(
                    () -> new ManaItem() {

                        @Override
                        public int getMana() {
                            return ManaProspectorBehavior.getMana(itemStack);
                        }

                        @Override
                        public int getMaxMana() {
                            return maxMana;
                        }

                        @Override
                        public void addMana(int mana) {
                            ManaProspectorBehavior.addMana(itemStack, mana);
                        }

                        @Override
                        public boolean canReceiveManaFromPool(BlockEntity pool) {
                            return true;
                        }

                        @Override
                        public boolean canReceiveManaFromItem(ItemStack otherStack) {
                            return true;
                        }

                        @Override
                        public boolean canExportManaToPool(BlockEntity pool) {
                            return false;
                        }

                        @Override
                        public boolean canExportManaToItem(ItemStack otherStack) {
                            return false;
                        }

                        @Override
                        public boolean isNoExport() {
                            return true;
                        }
                    }));
        }
        return LazyOptional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        int mana = getMana(stack);
        String formattedManaPool = String.format("%.2f", mana / (double) ManaPoolBlockEntity.MAX_MANA);
        String formattedMaxMana = String.format("%.2f", maxMana / (double) ManaPoolBlockEntity.MAX_MANA);
        tooltipComponents.add(Component.translatable("gtocore.tooltip.item.prospector.mana.1", mana, maxMana));
        tooltipComponents.add(Component.translatable("gtocore.tooltip.item.prospector.mana.2", formattedManaPool, formattedMaxMana));
    }

    @Override
    public float getDurabilityForDisplay(ItemStack stack) {
        return Mth.clamp(getMana(stack) / (float) maxMana, 0.0F, 1.0F);
    }

    @Override
    public int getMaxDurability(ItemStack stack) {
        return maxMana;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x98edff;
    }

    public record Tier(int radius, ProspectorMode<?>[] modes, int maxMana, double manaCostMultiplier) {}

    public static final Tier ULV = new Tier(1, new ProspectorMode[] { ProspectorMode.ORE }, 500000, 1); // half pool
    public static final Tier LV = new Tier(2, new ProspectorMode[] { ProspectorMode.ORE, ProspectorMode.FLUID }, 2000000, 0.8); // 2
                                                                                                                                // pools
    public static final Tier HV = new Tier(3, new ProspectorMode[] { ProspectorMode.ORE, ProspectorMode.FLUID, ProspectorMode.BEDROCK_ORE }, 8000000, 0.6); // 8
                                                                                                                                                            // pools
}
