package com.gtocore.mixin.ae2.menu;

import com.gtocore.api.ae2.pattern.IEncodingLogic;
import com.gtocore.client.Message;
import com.gtocore.common.machine.multiblock.electric.SuperMolecularAssemblerMachine;
import com.gtocore.common.machine.multiblock.part.ae.MECraftPatternPartMachine;
import com.gtocore.common.machine.multiblock.part.ae.MEPartInv;
import com.gtocore.integration.ae.hooks.IExtendedPatternContainer;
import com.gtocore.integration.ae.hooks.IExtendedPatternEncodingTerm;

import com.gtolib.api.ae2.IPatterEncodingTermMenu;
import com.gtolib.api.ae2.pattern.PatternUtils;
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
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.ITerminalHost;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.AEPatternDecoder;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.IMenuCraftingPacket;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.guisync.GuiSync;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.parts.encoding.PatternEncodingLogic;
import appeng.util.ConfigInventory;
import appeng.util.inv.AppEngInternalInventory;

import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixPattern;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(PatternEncodingTermMenu.class)
public abstract class PatternEncodingTermMenuMixin extends MEStorageMenu implements IMenuCraftingPacket, IPatterEncodingTermMenu, IExtendedPatternEncodingTerm.Menu {

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

    @Shadow(remap = false)
    @Final
    private ConfigInventory encodedInputsInv;
    @Shadow(remap = false)
    @Final
    private ConfigInventory encodedOutputsInv;
    @Shadow(remap = false)
    @Final
    private PatternEncodingLogic encodingLogic;

    @Unique
    @GuiSync(122)
    public boolean gtolib$extraInfoEnabled = true;
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
        }
        gto$lastRecipeType = GTRegistries.RECIPE_TYPES.get(RLUtils.parse(id.split("/")[0]));
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
        registerClientAction("addRecipe", String.class, this::gtolib$addRecipe);
        registerClientAction("clickRecipeInfo", this::gtolib$clickRecipeInfo);
        registerClientAction("addUUID", UUID.class, this::gtolib$addUUID);
        registerClientAction("sendPattern", Integer.class, this::gtolib$sendPattern);
        registerClientAction("sendPatternRequest", String.class, this::gtolib$sendEncodeRequest);
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

    @Inject(method = "broadcastChanges", at = @At("TAIL"))
    public void broadcastChanges(CallbackInfo ci) {
        if (isServerSide()) {
            this.gtocore$recipe = gtolib$logic().gtocore$getRecipe();
        }
    }

    @Shadow(remap = false)
    @Nullable
    protected abstract ItemStack encodePattern();

    @Unique
    private List<IExtendedPatternContainer> gto$getPatternContainers(String recipeLocName) {
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

        machines.removeIf(container -> gto$shouldRemoveContainer(container, stack, primaryOutput, sameCluster));
        var containerComparator = (gto$isCraft ? gto$craftFirst(stack) : gto$recipeFirst(gto$lastRecipeType, recipeLocName, stack)).reversed();

        machines.sort(containerComparator);
        return machines;
    }

    @Unique
    private static Comparator<IExtendedPatternContainer> gto$craftFirst(ItemStack patternStack) {
        return Comparator.comparing((IExtendedPatternContainer p) -> gto$canAddPattern(p, patternStack))
                .thenComparing(IExtendedPatternContainer::gto$isCraftingContainer);
    }

    @Unique
    private static Comparator<IExtendedPatternContainer> gto$recipeFirst(GTRecipeType recipeType, String recipeLocName, ItemStack patternStack) {
        if (recipeType == null) {
            return Comparator.comparing((IExtendedPatternContainer p) -> gto$canAddPattern(p, patternStack));
        }
        var c = Comparator.comparing((IExtendedPatternContainer p) -> gto$canAddPattern(p, patternStack))
                .thenComparing((IExtendedPatternContainer p) -> p.getSupportedRecipeTypes().contains(recipeType));
        if (recipeLocName != null && !recipeLocName.isEmpty()) {
            c = c.thenComparing((IExtendedPatternContainer p) -> gto$matchesRecipeName(p, recipeLocName));
        }
        return c;
    }

    @Unique
    private static boolean gto$matchesRecipeName(IExtendedPatternContainer container, String recipeType) {
        var name = container.getTerminalGroup().name().getString().toLowerCase(Locale.ROOT);
        var recipeNames = new LinkedHashSet<String>();
        recipeNames.add(recipeType.toLowerCase(Locale.ROOT));
        for (var recipeName : recipeNames) {
            if (!recipeName.isEmpty() && name.contains(recipeName)) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private static boolean gto$canAddPattern(IExtendedPatternContainer container, ItemStack patternStack) {
        return container.getTerminalPatternInventory().simulateAdd(patternStack).isEmpty();
    }

    @Unique
    private boolean gto$shouldRemoveContainer(IExtendedPatternContainer container, ItemStack patternStack, Object primaryOutput,
                                              Set<Object> sameCluster) {
        if (!container.isVisibleInTerminal()) {
            return true;
        }

        var patternInv = container.getTerminalPatternInventory();
        var hasSpace = gto$canAddPattern(container, patternStack);
        if (patternInv instanceof AppEngInternalInventory aeInv &&
                aeInv.getHost() instanceof TileAssemblerMatrixPattern matrixPattern) {
            return gto$shouldRemoveMatrixContainer(matrixPattern, container, patternStack, primaryOutput, sameCluster, hasSpace);
        }
        if (patternInv instanceof MEPartInv inv &&
                inv.getMachine() instanceof MECraftPatternPartMachine mecppm &&
                mecppm.getController() instanceof SuperMolecularAssemblerMachine smaMachine) {
            return gto$shouldRemoveSmaContainer(smaMachine, container, patternStack, primaryOutput, sameCluster, hasSpace);
        }

        return hasSpace && gto$containsPrimaryOutput(container, primaryOutput, getPlayer().level());
    }

    @Unique
    private boolean gto$shouldRemoveMatrixContainer(TileAssemblerMatrixPattern matrixPattern, IExtendedPatternContainer container,
                                                    ItemStack patternStack, Object primaryOutput, Set<Object> sameCluster,
                                                    boolean hasSpace) {
        var matrix = matrixPattern.getCluster();
        if (matrix == null) {
            return false;
        }

        if (sameCluster.contains(matrix)) {
            return true;
        }

        var clusterContainers = matrix.getPatterns().stream()
                .filter(IExtendedPatternContainer.class::isInstance)
                .map(IExtendedPatternContainer.class::cast)
                .toList();
        var clusterHasSpace = clusterContainers.stream().anyMatch(p -> gto$canAddPattern(p, patternStack));
        if (!hasSpace && clusterHasSpace) {
            return true;
        }
        sameCluster.add(matrix);
        if (!clusterHasSpace) {
            return false;
        }

        for (var c : clusterContainers) {
            if (gto$containsPrimaryOutput(c, primaryOutput, getPlayer().level())) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private boolean gto$shouldRemoveSmaContainer(SuperMolecularAssemblerMachine smaMachine, IExtendedPatternContainer container,
                                                 ItemStack patternStack, Object primaryOutput, Set<Object> sameCluster,
                                                 boolean hasSpace) {
        if (sameCluster.contains(smaMachine)) {
            return true;
        }

        var clusterContainers = Arrays.stream(smaMachine.getParts())
                .filter(IExtendedPatternContainer.class::isInstance)
                .map(IExtendedPatternContainer.class::cast)
                .toList();
        var clusterHasSpace = clusterContainers.stream().anyMatch(p -> gto$canAddPattern(p, patternStack));
        if (!hasSpace && clusterHasSpace) {
            return true;
        }
        sameCluster.add(smaMachine);
        if (!clusterHasSpace) {
            return false;
        }

        for (var c : clusterContainers) {
            if (gto$containsPrimaryOutput(c, primaryOutput, getPlayer().level())) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private static boolean gto$containsPrimaryOutput(IExtendedPatternContainer container, Object primaryOutput,
                                                     net.minecraft.world.level.Level level) {
        for (var pattern : container.getTerminalPatternInventory()) {
            var details = AEPatternDecoder.INSTANCE.decodePattern(pattern, level, false);
            if (details != null && details.getPrimaryOutput().what() == primaryOutput) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private static boolean gto$isFull(IExtendedPatternContainer container, ItemStack patternStack) {
        var patternInv = container.getTerminalPatternInventory();
        if (patternInv instanceof AppEngInternalInventory aeInv &&
                aeInv.getHost() instanceof TileAssemblerMatrixPattern matrixPattern) {
            var matrix = matrixPattern.getCluster();
            if (matrix == null) {
                return !gto$canAddPattern(container, patternStack);
            }
            return matrix.getPatterns().stream()
                    .filter(IExtendedPatternContainer.class::isInstance)
                    .map(IExtendedPatternContainer.class::cast)
                    .noneMatch(p -> gto$canAddPattern(p, patternStack));
        }
        if (patternInv instanceof MEPartInv inv &&
                inv.getMachine() instanceof MECraftPatternPartMachine mecppm &&
                mecppm.getController() instanceof SuperMolecularAssemblerMachine smaMachine) {
            return Arrays.stream(smaMachine.getParts())
                    .filter(IExtendedPatternContainer.class::isInstance)
                    .map(IExtendedPatternContainer.class::cast)
                    .noneMatch(p -> gto$canAddPattern(p, patternStack));
        }
        return !gto$canAddPattern(container, patternStack);
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
        var containers = gto$currentContainers;
        if (containers == null) {
            return;
        }
        if (index < 0 || index >= containers.size()) {
            return;
        }
        var container = containers.get(index);
        if (container.isOutOfService()) return;

        var patternStack = gto$patternStack;
        if (patternStack == null) return;
        if (!gto$canAddPattern(container, patternStack)) return;

        var me = grid.getStorageService().getInventory();
        var blank = AEItemKey.of(AEItems.BLANK_PATTERN);
        var extractedBlankPatterns = me.extract(blank, 1, Actionable.MODULATE, getActionSource());
        if (extractedBlankPatterns == 0) {
            return;
        }
        // 如果没有成功插入样板则回滚
        var remainder = container.getTerminalPatternInventory().addItems(patternStack);
        if (!remainder.isEmpty()) {
            me.insert(blank, extractedBlankPatterns, Actionable.MODULATE, getActionSource());
        }
    }

    @Unique
    private void gtolib$sendEncodeRequest(String recipeLocName) {
        if (isClientSide()) {
            sendClientAction("sendPatternRequest", recipeLocName);
            return;
        }
        var patternStack = encodePattern();
        if (patternStack == null) return;
        gto$patternStack = patternStack;
        gto$currentContainers = gto$getPatternContainers(recipeLocName);
        if (gto$currentContainers.isEmpty()) return;
        Message.sendPatternDestination((ServerPlayer) getPlayer(), gto$currentContainers.stream()
                .map(container -> new Message.PatternDestination(
                        container.getTerminalGroup(),
                        gto$isFull(container, patternStack)))
                .toArray(Message.PatternDestination[]::new));
    }

    @Override
    public void gtolib$sendEncodeRequest() {
        if (isClientSide()) {
            if (gto$lastRecipeType == null) {
                sendClientAction("sendPatternRequest", "");
            } else {
                sendClientAction("sendPatternRequest",
                        Component.translatable("gtceu." + gto$lastRecipeType.registryName.getPath()).getString());
            }
        }
    }
}
