package com.gtocore.mixin.ae2.menu;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.OrderItem;
import com.gtocore.integration.ae.hooks.ICraftAmountMenu;
import com.gtocore.integration.ae.hooks.ITemporaryCraftableService;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.crafting.CraftAmountMenu;
import appeng.menu.me.crafting.CraftConfirmMenu;
import appeng.menu.slot.AppEngSlot;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(CraftAmountMenu.class)
public abstract class CraftAmountMenuMixin extends AEBaseMenu implements ICraftAmountMenu {

    @Shadow(remap = false)
    @Final
    private AppEngSlot craftingItem;
    @Shadow(remap = false)
    private AEKey whatToCraft;

    @Shadow(remap = false)
    public abstract Level getLevel();

    @Unique
    private KeyCounter gto$whatToCraft = null;

    public CraftAmountMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Override
    public void gto$setWhatToCraft(KeyCounter whatToCraft, long initialAmount) {
        this.gto$whatToCraft = Objects.requireNonNull(whatToCraft, "whatToCraft");
        assert !whatToCraft.isEmpty() : "whatToCraft cannot be empty";
        var tempOrderItem = GTOItems.TEMP_ORDER.asStack();
        var iconItem = whatToCraft.getFirstEntry().getKey() instanceof AEItemKey aeItemKey ? aeItemKey.getReadOnlyStack().copy() : GTOItems.ORDER.asStack();
        iconItem.setHoverName(whatToCraft.getFirstEntry().getKey().getDisplayName());
        OrderItem.setTarget(tempOrderItem, iconItem);
        this.whatToCraft = AEItemKey.of(tempOrderItem);
        if (this.whatToCraft != null) {
            this.craftingItem.set(GenericStack.wrapInItemStack(this.whatToCraft, initialAmount));
        }
    }

    @WrapOperation(method = "confirm",
                   at = @At(
                            value = "INVOKE",
                            target = "Lappeng/menu/me/crafting/CraftConfirmMenu;planJob(Lappeng/api/stacks/AEKey;JLappeng/api/networking/crafting/CalculationStrategy;)Z",
                            remap = false),
                   remap = false)
    private boolean onConfirm(CraftConfirmMenu instance, AEKey what, long amount, CalculationStrategy strategy, Operation<Boolean> original) {
        check:
        if (this.gto$whatToCraft != null) {
            var gridNode = getActionHost().getActionableNode();
            if (gridNode == null) break check;
            var grid = gridNode.getGrid();
            var service = ((ITemporaryCraftableService) grid.getCraftingService());
            var processPattern = PatternDetailsHelper.encodeProcessingPattern(
                    gto$whatToCraft.entrySet().stream()
                            .map(key -> new GenericStack(key.what(), key.amount()))
                            .toArray(GenericStack[]::new),
                    new GenericStack[] { new GenericStack(whatToCraft, 1) });
            service.gto$setTempPatternDetails(PatternDetailsHelper.decodePattern(processPattern, getLevel()));
        }
        return original.call(instance, what, amount, strategy);
    }
}
