package com.gtocore.mixin.ae2.menu;

import com.gtocore.api.ae2.pattern.IEncodingLogic;
import com.gtocore.client.Message;
import com.gtocore.common.machine.multiblock.electric.SuperMolecularAssemblerMachine;
import com.gtocore.common.machine.multiblock.part.ae.MECraftPatternPartMachine;
import com.gtocore.integration.ae.hooks.IExtendedPatternContainer;
import com.gtocore.integration.ae.hooks.IExtendedPatternEncodingTerm;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.IPatterEncodingTermMenu;
import com.gtolib.api.ae2.pattern.PatternUtils;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.ClientUtil;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.AEPatternDecoder;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.IMenuCraftingPacket;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.helpers.patternprovider.PatternContainer;
import appeng.menu.guisync.GuiSync;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.parts.encoding.PatternEncodingLogic;
import appeng.util.ConfigInventory;
import appeng.util.inv.AppEngInternalInventory;
import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixPattern;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(PatternEncodingTermMenu.class)
public abstract class PatternEncodingTermMenuMixin extends MEStorageMenu implements IMenuCraftingPacket, IPatterEncodingTermMenu, IExtendedPatternEncodingTerm.Menu {

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
    private GTRecipeType gto$lastRecipeType = null;
    @Unique
    private boolean gto$isCraft = false;
    @Unique
    private List<IExtendedPatternContainer> gto$currentContainers = null;
    @Unique
    private ItemStack gto$patternStack;

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
        } else {
            gtolib$logic().gtocore$setRecipe(id);
            gto$lastRecipeType = GTRegistries.RECIPE_TYPES.get(RLUtils.parse(id.split("/")[0]));
        }
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
        registerClientAction("sendPattern", Integer.class, this::gtolib$sendPattern);
        registerClientAction("sendPatternRequest", this::gtolib$sendEncodeRequest);
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

    @Inject(method = "encodePattern", at = @At(value = "RETURN"), remap = false)
    private void onEncodeSucceeded(CallbackInfoReturnable<ItemStack> cir) {
        var stack = cir.getReturnValue();
        if (stack == null || stack.isEmpty()) return;
        gto$isCraft = !(stack.getItem() instanceof ProcessingPatternItem);
    }

    @Inject(method = "encode", at = @At(value = "INVOKE", target = "Lappeng/menu/slot/RestrictedInputSlot;set(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1, remap = true), remap = false, cancellable = true)
    private void encoding(CallbackInfo ci, @Local(name = "encodedPattern") ItemStack stack) {
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

    @Shadow(remap = false)
    @Nullable
    protected abstract ItemStack encodePattern();

    @Redirect(method = "encode",
              at = @At(value = "INVOKE",
                       target = "Lappeng/menu/slot/RestrictedInputSlot;getItem()Lnet/minecraft/world/item/ItemStack;",
                       ordinal = 1))
    private ItemStack fetchPattern(RestrictedInputSlot instance) {
        var blankPattern = instance.getItem();
        GTOCore.LOGGER.info("Fetching blank pattern from slot: {}", blankPattern);
        if (!isPattern(blankPattern) && gtolib$tryExtractBlankPattern()) {
            return new ItemStack(AEItems.BLANK_PATTERN, 1);
        }
        return blankPattern;
    }

    @Unique
    private boolean gtolib$tryExtractBlankPattern() {
        var host = getHost();
        if (host == null) return false;

        MEStorage inventory = host.getInventory();
        if (inventory == null) return false;

        AEItemKey blankPattern = AEItemKey.of(AEItems.BLANK_PATTERN);

        var extracted = inventory.extract(blankPattern, 1, Actionable.MODULATE, getActionSource());
        return extracted > 0;
    }

    @Redirect(
              method = "transferStackToMenu",
              at = @At(value = "INVOKE", target = "Lappeng/menu/slot/RestrictedInputSlot;mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", remap = true),
              remap = false)
    private boolean gtolib$modifyTransferStackToMenu(RestrictedInputSlot instance, ItemStack itemStack) {
        // 空白样板槽现在是幽灵槽位，无需手动补充
        return itemStack.getItem() != AEItems.BLANK_PATTERN.asItem() && instance.mayPlace(itemStack);
    }

    @Unique
    private List<IExtendedPatternContainer> gto$getPatternContainers() {
        var gridNode = getActionHost().getActionableNode();
        if (gridNode == null) {
            return List.of();
        }
        var grid = gridNode.getGrid();
        if (grid == null) {
            return List.of();
        }
        var stack = gto$patternStack;
        if (stack == null) return List.of();
        ArrayList<IExtendedPatternContainer> machines = new ArrayList<>(grid.size() / 2 + 1);
        for (var machineClass : grid.getMachineClasses()) {
            if (IExtendedPatternContainer.class.isAssignableFrom(machineClass)) {
                machines.addAll((Collection<? extends IExtendedPatternContainer>) grid.getActiveMachines(machineClass));
            }
        }
        var thisPatternDetails = AEPatternDecoder.INSTANCE.decodePattern(stack, getPlayer().level(), false);
        if (thisPatternDetails == null) return List.of();
        var primaryOutput = thisPatternDetails.getPrimaryOutput().what();
        Set<Object> sameCluster = new HashSet<>();

        machines.removeIf(container -> {
            var patternInv = container.getTerminalPatternInventory();

            if (!container.isVisibleInTerminal() ||
                    patternInv.simulateAdd(stack) == stack)
                return true;
            if (patternInv instanceof AppEngInternalInventory aeInv &&
                    aeInv.getHost() instanceof TileAssemblerMatrixPattern matrixPattern) {
                var matrix = matrixPattern.getCluster();
                if (matrix == null) return false;
                if (sameCluster.contains(matrix)) return true;
                sameCluster.add(matrix);
                return matrix.getPatterns()
                        .stream()
                        .flatMap(m -> m.getAvailablePatterns().stream())
                        .anyMatch(p -> p.getPrimaryOutput().what() == primaryOutput);
            }
            if (patternInv instanceof MECraftPatternPartMachine mecppm &&
                    mecppm.getController() instanceof SuperMolecularAssemblerMachine smaMachine) {
                if (sameCluster.contains(smaMachine)) return true;
                sameCluster.add(smaMachine);
                return Arrays.stream(smaMachine.getParts())
                        .filter(m -> m instanceof MECraftPatternPartMachine)
                        .map(m -> (MECraftPatternPartMachine) m)
                        .flatMap(m -> m.getAvailablePatterns().stream())
                        .anyMatch(p -> p.getPrimaryOutput().what() == primaryOutput);
            }
            for (var paattern : patternInv) {
                var details = AEPatternDecoder.INSTANCE.decodePattern(paattern, getPlayer().level(), false);
                if (details == null) continue;
                if (details.getPrimaryOutput().what() == primaryOutput) {
                    return true;
                }
            }
            return false;

        });
        var containerComparator = (gto$isCraft ? gto$CRAFT_FIRST : gto$recipeFirst(gto$lastRecipeType)).reversed();

        machines.sort(containerComparator);
        return machines;
    }

    @Unique
    private static final Comparator<IExtendedPatternContainer> gto$CRAFT_FIRST = Comparator.comparing(IExtendedPatternContainer::hasEmptyPatternSlot)
            .thenComparing(IExtendedPatternContainer::gto$isCraftingContainer);

    @Unique
    private static Comparator<IExtendedPatternContainer> gto$recipeFirst(GTRecipeType recipeType) {
        if (recipeType == null) {
            return Comparator.comparing(IExtendedPatternContainer::hasEmptyPatternSlot);
        }
        return Comparator.comparing(IExtendedPatternContainer::hasEmptyPatternSlot)
                .thenComparing((IExtendedPatternContainer p) -> p.getSupportedRecipeTypes().contains(recipeType));
    }

    @Override
    public void gtolib$sendPattern(int index) {
        if (isClientSide()) {
            sendClientAction("sendPattern", index);
            return;
        }
        var gridNode = getActionHost().getActionableNode();
        if (gridNode == null) {
            return;
        }
        var grid = gridNode.getGrid();
        if (grid == null) {
            return;
        }
        if (grid.getStorageService().getInventory().extract(AEItemKey.of(AEItems.BLANK_PATTERN), 1, Actionable.MODULATE, getActionSource()) == 0) {
            return;
        }
        var containers = gto$currentContainers;
        if (index < 0 || index >= containers.size()) {
            return;
        }
        var container = containers.get(index);
        if (container.isOutOfService()) return;

        var patternStack = gto$patternStack;
        if (patternStack == null) return;
        container.getTerminalPatternInventory().addItems(patternStack);
    }

    @Override
    public void gtolib$sendEncodeRequest() {
        if (isClientSide()) {
            sendClientAction("sendPatternRequest");
            return;
        }
        var patternStack = encodePattern();
        if (patternStack == null) return;
        gto$patternStack = patternStack;
        gto$currentContainers = gto$getPatternContainers();
        if (gto$currentContainers.isEmpty()) return;
        Message.sendPatternDestination((ServerPlayer) getPlayer(), gto$currentContainers.stream()
                .map(PatternContainer::getTerminalGroup)
                .toArray(PatternContainerGroup[]::new));
    }
}
