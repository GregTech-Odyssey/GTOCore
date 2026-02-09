package com.gtocore.mixin.eae;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.util.prioritylist.IPartitionList;
import com.glodblock.github.extendedae.client.gui.widget.OutputResultSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OutputResultSlot.class)
public class OutputResultSlotMixin {

    @Shadow(remap = false)
    @Final
    private IEnergySource energySrc;

    @Shadow(remap = false)
    @Final
    private IActionSource mySrc;

    /**
     * @author ..
     * @reason 编译时使用正确的方法签名
     */
    @Overwrite(remap = false)
    private ItemStack extractItemsByRecipe(MEStorage src, Level level, Recipe<Container> r, ItemStack output, CraftingContainer ci, ItemStack providedTemplate, int slot, KeyCounter items, IPartitionList filter) {
        if (this.energySrc.extractAEPower(1, Actionable.SIMULATE, PowerMultiplier.CONFIG) > 0.9) {
            if (providedTemplate == null) {
                return ItemStack.EMPTY;
            }
            var ae_req = AEItemKey.of(providedTemplate);
            if (ae_req != null) {
                if (filter == null || filter.isListed(ae_req)) {
                    var extracted = src.extract(ae_req, 1, Actionable.MODULATE, this.mySrc);
                    if (extracted > 0) {
                        this.energySrc.extractAEPower(1, Actionable.MODULATE, PowerMultiplier.CONFIG);
                        return ae_req.toStack();
                    }
                }
            }
            var checkFuzzy = providedTemplate.hasTag() || providedTemplate.isDamageableItem();
            if (items != null && checkFuzzy) {
                for (var x : items) {
                    if (x.getKey() instanceof AEItemKey itemKey) {
                        if (providedTemplate.getItem() == itemKey.getItem() && !itemKey.matches(output)) {
                            ci.setItem(slot, itemKey.toStack());
                            if (r.matches(ci, level) && ItemStack.matches(r.assemble(ci, level.registryAccess()), output)) {
                                if (filter == null || filter.isListed(itemKey)) {
                                    var ex = src.extract(itemKey, 1, Actionable.MODULATE, this.mySrc);
                                    if (ex > 0) {
                                        this.energySrc.extractAEPower(1, Actionable.MODULATE, PowerMultiplier.CONFIG);
                                        return itemKey.toStack();
                                    }
                                }
                            }
                            ci.setItem(slot, providedTemplate);
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
