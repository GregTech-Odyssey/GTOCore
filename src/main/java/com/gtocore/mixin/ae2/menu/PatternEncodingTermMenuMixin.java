package com.gtocore.mixin.ae2.menu;

import com.gtocore.api.ae2.pattern.IEncodingLogic;

import com.gtolib.api.ae2.IPatterEncodingTermMenu;
import com.gtolib.api.ae2.pattern.PatternUtils;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.ClientUtil;
import com.gtolib.utils.RLUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.core.definitions.AEItems;
import appeng.helpers.IMenuCraftingPacket;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.guisync.GuiSync;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.parts.encoding.PatternEncodingLogic;
import appeng.util.ConfigInventory;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PatternEncodingTermMenu.class)
public abstract class PatternEncodingTermMenuMixin extends MEStorageMenu implements IMenuCraftingPacket, IPatterEncodingTermMenu {

    @Unique
    @GuiSync(122)
    public boolean gtolib$extraInfoEnabled = true;
    @Shadow(remap = false)
    @Final
    private ConfigInventory encodedInputsInv;
    @Shadow(remap = false)
    @Final
    private ConfigInventory encodedOutputsInv;
    @Final
    @Shadow(remap = false)
    private RestrictedInputSlot encodedPatternSlot;
    @Final
    @Shadow(remap = false)
    private RestrictedInputSlot blankPatternSlot;
    @Shadow(remap = false)
    @Final
    private PatternEncodingLogic encodingLogic;
    @Unique
    @GuiSync(120)
    public String gtocore$recipe = "";

    @Unique
    private UUID gtocore$UUID;

    protected PatternEncodingTermMenuMixin(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
        super(menuType, id, ip, host);
    }

    @Unique
    private IEncodingLogic gtolib$logic() {
        return ((IEncodingLogic) encodingLogic);
    }

    @Override
    public void gtolib$addRecipe(String id) {
        if (isClientSide()) {
            sendClientAction("addRecipe", id);
        } else gtolib$logic().gtocore$setRecipe(id);
    }

    @Override
    public void gtolib$addUUID(UUID id) {
        if (isClientSide()) {
            sendClientAction("addUUID", id);
        } else gtocore$UUID = id;
    }

    @Override
    public void gtolib$clickRecipeInfo() {
        if (isClientSide()) {
            sendClientAction("clickRecipeInfo");
            return;
        }
        if (this.gtolib$extraInfoEnabled && !gtolib$logic().gtocore$getRecipe().isEmpty()) {
            gtolib$logic().gtocore$clearExtraRecipeInfo();
            return;
        }
        gtolib$logic().gtocore$clearExtraRecipeInfo();
        this.gtolib$extraInfoEnabled = !this.gtolib$extraInfoEnabled;
    }

    @Unique
    private static final String TITLE_ENABLED = "gtocore.pattern.recipeInfoButton.title.enabled";
    @Unique
    private static final String TITLE_DISABLED = "gtocore.pattern.recipeInfoButton.title.disabled";
    @Unique
    private static final String CLICK_TO_ENABLE = "gtocore.pattern.recipeInfoButton.clickToEnable";
    @Unique
    private static final String CLICK_TO_DISABLE = "gtocore.pattern.recipeInfoButton.clickToDisable";
    @Unique
    private static final String CLICK_TO_CLEAR = "gtocore.pattern.recipeInfoButton.clickToClear";

    @Override
    public Component gtolib$getRecipeInfoTooltip() {
        var title = Component.empty();
        title.append(this.gtolib$extraInfoEnabled ? Component.translatable(TITLE_ENABLED) : Component.translatable(TITLE_DISABLED));
        title.append("\n");
        if (!this.gtolib$extraInfoEnabled) {
            return title.append(Component.translatable(CLICK_TO_ENABLE));
        }
        if (!gtocore$recipe.isEmpty()) {
            var tooltip = Component.empty();
            tooltip.append(Component.translatable("gtocore.pattern.recipe")).append("\n");
            var key = RLUtils.parse(gtocore$recipe.split("/")[0]).toLanguageKey();
            tooltip.append(Component.translatable("gtocore.pattern.type", Component.translatable(key))).append("\n");
            return title.append(tooltip.append(Component.translatable(CLICK_TO_CLEAR)));
        } else {
            return title.append(Component.translatable(CLICK_TO_DISABLE));
        }
    }

    @Inject(method = "encodeProcessingPattern", at = @At("RETURN"), remap = false)
    private void encodeProcessingPatternHook(CallbackInfoReturnable<ItemStack> cir) {
        if (gtolib$extraInfoEnabled) {
            if (!gtolib$logic().gtocore$getRecipe().isEmpty()) {
                cir.getReturnValue().getOrCreateTag().putString("recipe", gtolib$logic().gtocore$getRecipe());
            }
        }
        if (gtocore$UUID != null) {
            cir.getReturnValue().getOrCreateTag().putUUID("uuid", gtocore$UUID);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/IPatternTerminalMenuHost;Z)V",
            at = @At("TAIL"),
            remap = false)
    private void initHooks(MenuType<?> menuType, int id, Inventory ip, IPatternTerminalMenuHost host, boolean bindInventory, CallbackInfo ci) {
        registerClientAction("modifyPatter", Integer.class, this::gtolib$modifyPatter);
        registerClientAction("clearSecOutput", this::gtolib$clearSecOutput);
        blankPatternSlot.setStackLimit(1);
        registerClientAction("addRecipe", String.class, this::gtolib$addRecipe);
        registerClientAction("clickRecipeInfo", this::gtolib$clickRecipeInfo);
        registerClientAction("addUUID", UUID.class, this::gtolib$addUUID);
    }

    @Override
    public void gtolib$modifyPatter(Integer data) {
        if (isClientSide()) {
            sendClientAction("modifyPatter", data);
        } else {
            // modify
            PatternUtils.mulPatternEncodingArea(encodedInputsInv, encodedOutputsInv, data);
        }
    }

    @Unique
    public void gtolib$clearSecOutput() {
        if (isClientSide()) {
            sendClientAction("clearSecOutput");
        } else {
            for (int i = 1; i <= 8; i++) {
                encodedOutputsInv.setStack(i, null);
            }
        }
    }

    @Inject(method = "encode", at = @At(value = "INVOKE", target = "Lappeng/menu/me/items/PatternEncodingTermMenu;sendClientAction(Ljava/lang/String;)V"), remap = false)
    private void encode(CallbackInfo ci) {
        gtolib$addUUID(ClientUtil.getUUID());
    }

    @Inject(method = "encode", at = @At(value = "INVOKE", target = "Lappeng/menu/slot/RestrictedInputSlot;set(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1, remap = true), remap = false, cancellable = true)
    private void encoding(CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        var player = getPlayer();
        if (player instanceof IEnhancedPlayer enhancedPlayer) {
            if (enhancedPlayer.getPlayerData().shiftState) {
                var inventory = player.getInventory();
                if (inventory.add(stack)) {
                    encodedPatternSlot.clearStack();
                    ci.cancel();
                }
            }
        }
    }

    // 按住Shift时将玩家物品栏的已编码样板清空为空白样板
    @Inject(method = "clearPattern", at = @At("HEAD"), remap = false)
    private void clearInventoryPattern(CallbackInfo ci) {
        var player = getPlayer();
        if (player instanceof IEnhancedPlayer enhancedPlayer) {
            if (enhancedPlayer.getPlayerData().shiftState) {
                var inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemStack = inventory.getItem(i);
                    if (PatternDetailsHelper.isEncodedPattern(itemStack)) {
                        inventory.setItem(i, AEItems.BLANK_PATTERN.stack(itemStack.getCount()));
                    }
                }
            }
        }
    }

    @Inject(method = "broadcastChanges", at = @At("TAIL"))
    public void broadcastChanges(CallbackInfo ci) {
        if (isServerSide()) {
            this.gtocore$recipe = gtolib$logic().gtocore$getRecipe();
        }
    }

    @Shadow(remap = false)
    protected abstract boolean isPattern(ItemStack output);

    @Shadow(remap = false)
    public abstract void encode();

    @Inject(method = "encode",
            at = @At(value = "INVOKE",
                     target = "Lappeng/menu/me/items/PatternEncodingTermMenu;isPattern(Lnet/minecraft/world/item/ItemStack;)Z",
                     remap = false),
            remap = false,
            cancellable = true)
    private void tryAutoRefillBlankPattern(CallbackInfo ci, @Local(ordinal = 1) ItemStack blankPattern) {
        // 如果空白样板槽位没有有效的样板，尝试自动补充
        if (!isPattern(blankPattern)) {
            if (gtolib$tryRefillBlankPattern()) {
                // 补充成功，重新执行encode逻辑
                ci.cancel();
                encode();
            }
        }
    }

    @Unique
    private boolean gtolib$tryRefillBlankPattern() {
        var host = getHost();
        if (host == null) return false;

        MEStorage inventory = host.getInventory();
        if (inventory == null) return false;

        AEItemKey blankPattern = AEItemKey.of(AEItems.BLANK_PATTERN);
        long supplier = inventory.getAvailableStacks().get(blankPattern);
        if (supplier <= 0) return false;
        var stock = blankPatternSlot.getItem().getCount();
        var demand = Math.min(supplier, 1 - stock);
        if (demand <= 0) return false;

        var extracted = inventory.extract(blankPattern, demand, Actionable.MODULATE, getActionSource());
        if (extracted <= 0) return false;

        blankPatternSlot.set(blankPattern.toStack().copyWithCount(stock + (int) extracted));

        return true;
    }

    @Redirect(
              method = "transferStackToMenu",
              at = @At(value = "INVOKE", target = "Lappeng/menu/slot/RestrictedInputSlot;mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", remap = true),
              remap = false)
    private boolean gtolib$modifyTransferStackToMenu(RestrictedInputSlot instance, ItemStack itemStack) {
        // 空白样板槽现在是幽灵槽位，无需手动补充
        return itemStack.getItem() != AEItems.BLANK_PATTERN.asItem() && instance.mayPlace(itemStack);
    }
}
