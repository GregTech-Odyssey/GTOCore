package com.gtocore.mixin.ae2.menu;

import com.gtolib.api.ae2.GTOSettings;
import com.gtolib.api.ae2.IPatternAccessTermMenu;
import com.gtolib.api.ae2.ShowMolecularAssembler;
import com.gtolib.api.ae2.me2in1.Me2in1Menu;

import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.ShowPatternProviders;
import appeng.api.inventories.InternalInventory;
import appeng.api.util.IConfigurableObject;
import appeng.core.definitions.AEBlocks;
import appeng.crafting.pattern.CraftingPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternAccessTermMenu;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(PatternAccessTermMenu.class)
public abstract class PatternAccessTermMenuMixin implements IPatternAccessTermMenu {

    @Unique
    @GuiSync(2)
    private ShowMolecularAssembler gtolib$showMolecularAssembler;

    @Unique
    private ShowMolecularAssembler gtolib$lastShownMolecularAssembler;

    @Shadow(remap = false)
    protected abstract boolean isFull(PatternContainer logic);

    @Shadow(remap = false)
    public abstract ShowPatternProviders getShownProviders();

    @Shadow(remap = false)
    @Final
    private IConfigurableObject host;

    @Shadow(remap = false)
    @Final
    private Set<PatternContainer> pinnedHosts;

    @Shadow(remap = false)
    protected boolean updatePatterns;

    @Override
    public @NotNull ShowMolecularAssembler gtolib$getShownMolecularAssemblers() {
        return gtolib$showMolecularAssembler;
    }

    @Inject(method = "<init>*", at = @At("TAIL"), remap = false)
    private void gtolib$init(CallbackInfo ci) {
        gtolib$showMolecularAssembler = ShowMolecularAssembler.ALL;
    }

    @Inject(method = "broadcastChanges",
            at = @At(value = "FIELD",
                     target = "Lappeng/menu/implementations/PatternAccessTermMenu;showPatternProviders:Lappeng/api/config/ShowPatternProviders;",  // 使用通配符
                     opcode = Opcodes.PUTFIELD,
                     shift = At.Shift.AFTER,
                     remap = false))
    private void afterBroadcastChanges(CallbackInfo ci) {
        if (this.host.getConfigManager().hasSetting(GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS)) {
            gtolib$showMolecularAssembler = this.host.getConfigManager().getSetting(GTOSettings.TERMINAL_SHOW_MOLECULAR_ASSEMBLERS);
        } else {
            gtolib$showMolecularAssembler = ShowMolecularAssembler.ALL;
        }
    }

    @Inject(method = "broadcastChanges", at = @At(value = "INVOKE", target = "Lappeng/menu/AEBaseMenu;broadcastChanges()V"))
    private void broadcastChanges(CallbackInfo ci) {
        if (gtolib$lastShownMolecularAssembler != gtolib$showMolecularAssembler) {
            gtolib$lastShownMolecularAssembler = gtolib$showMolecularAssembler;
            updatePatterns = true;
        }
    }

    @Unique
    private boolean gtolib$originalIsVisible(PatternContainer container) {
        boolean isVisible = container.isVisibleInTerminal();

        return switch (getShownProviders().ordinal()) {
            case 0 -> isVisible;
            case 1 -> isVisible && (pinnedHosts.contains(container) || !isFull(container));
            default -> true;
        };
    }

    @Unique
    private static boolean gtolib$isCraftingContainer(PatternContainer container) {
        Set<Item> MolecularAssemblerItem = Set.of(
                AEBlocks.MOLECULAR_ASSEMBLER.asItem().asItem(),
                EPPItemAndBlock.EX_ASSEMBLER.asItem());
        InternalInventory inventory = container.getTerminalPatternInventory();
        if (!inventory.isEmpty()) {
            for (int j = 0; j < inventory.size(); j++) {
                ItemStack item = inventory.getStackInSlot(j);
                if (!item.isEmpty()) {
                    return item.getItem() instanceof CraftingPatternItem;
                }
            }
        }
        if (container.getTerminalGroup().icon() != null) {
            return MolecularAssemblerItem.contains(container.getTerminalGroup().icon().getItem());
        }
        return false;
    }

    @Unique
    private boolean gtolib$isMolecularAssembler(PatternContainer container) {
        return switch (gtolib$getShownMolecularAssemblers().ordinal()) {
            case 0 -> gtolib$isCraftingContainer(container);
            case 1 -> !gtolib$isCraftingContainer(container);
            default -> true;
        };
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private boolean isVisible(PatternContainer container) {
        return gtolib$isMolecularAssembler(container) && gtolib$originalIsVisible(container);
    }

    @Redirect(
              method = "doAction",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z",
                       remap = true),
              remap = false)
    private boolean modifyDoActionAdd(Inventory playerInv, ItemStack stack) {
        if ((Object) this instanceof Me2in1Menu menu) {
            var after = menu.getEncoding().transferPatternToBuffer(stack);
            if (after.isEmpty()) {
                // If the pattern is transferred to the buffer, we don't need to add it to the player's inventory.
                return true;
            }
        }
        return playerInv.add(stack);
    }

    @Mixin(targets = "appeng.menu.implementations.PatternAccessTermMenu$ContainerTracker")
    public static class ContainerTrackerMixin {

        /**
         * @author
         * @reason
         */
        @Overwrite(remap = false)
        private static boolean isDifferent(ItemStack a, ItemStack b) {
            if (ItemStackHashStrategy.ITEM.equals(a, b)) {
                var at = a.getTag();
                var bt = b.getTag();
                if (at == null && bt == null) return false;
                if (at == null || bt == null) return true;
                var oa = at.tags.get("out");
                var ob = bt.tags.get("out");
                if (oa instanceof ListTag la && la.get(0) instanceof CompoundTag ca && ca.tags.get("id") instanceof StringTag sa && ob instanceof ListTag lb && lb.get(0) instanceof CompoundTag cb && cb.tags.get("id") instanceof StringTag sb) {
                    return !sa.equals(sb);
                }
                return false;
            }
            return true;
        }
    }
}
