package com.gto.gtocore.common.item;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.DummyMachine;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.client.Tooltips;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.integration.kjs.GTKubeJSPlugin;
import com.gto.gtocore.utils.*;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.WidgetUtils;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.gui.editor.IEditableUI;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.gui.widget.PhantomFluidWidget;
import com.gregtechceu.gtceu.api.gui.widget.PhantomSlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.recipe.condition.CleanroomCondition;
import com.gregtechceu.gtceu.common.recipe.condition.DimensionCondition;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.core.mixins.TagValueAccessor;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.common.collect.Tables;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;

public final class RecipeEditorBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final RecipeEditorBehavior INSTANCE = new RecipeEditorBehavior();

    private static final Map<MetaMachine, DummyMachine> CACHE = new Object2ObjectOpenHashMap<>();
    private static final Map<BlockPos, DummyMachine> POS_CACHE = new Object2ObjectOpenHashMap<>();

    private boolean isGT;
    private DummyMachine machine;

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        if (Objects.requireNonNull(context.getPlayer()).isShiftKeyDown()) {
            // KJS转换
            StringBuilder stringBuilder = new StringBuilder();
            GTKubeJSPlugin.KJS_RECIPE.forEach(recipe -> {
                if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                    stringBuilder.append("\nVanillaRecipeHelper.addShapelessRecipe(provider, ");
                    stringBuilder.append("GTOCore.id(\"").append(StringUtils.decompose('_', shapelessRecipe.getId().getPath().replace("kjs/", ""))[1]).append("\"), ");
                    stringBuilder.append(StringConverter.fromItem(Ingredient.of(shapelessRecipe.getResultItem(null)), 0));
                    shapelessRecipe.getIngredients().forEach(i -> stringBuilder.append(", ").append(StringConverter.fromItem(i, 2)));
                    stringBuilder.append(");");
                } else if (recipe instanceof ShapedRecipe shapedRecipe) {
                    stringBuilder.append("\nVanillaRecipeHelper.addShapedRecipe(provider, ");
                    stringBuilder.append("GTOCore.id(\"").append(StringUtils.decompose('_', shapedRecipe.getId().getPath().replace("kjs/", ""))[1]).append("\"), ");
                    stringBuilder.append(StringConverter.fromItem(Ingredient.of(shapedRecipe.getResultItem(null)), 0)).append(",\n");
                    Map<String, Ingredient> kay = GTOUtils.reconstructKeys(shapedRecipe.getIngredients());
                    String[] pattern = GTOUtils.reconstructPattern(shapedRecipe.getIngredients(), kay, shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight());
                    for (String p : pattern) {
                        stringBuilder.append("\"").append(p).append("\",\n");
                    }
                    kay.forEach((k, v) -> stringBuilder.append("'").append(k).append("', ").append(StringConverter.fromItem(v, 2)).append(","));
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.append(");");
                }
            });
            GTKubeJSPlugin.KJS_GT_RECIPE.forEach(recipe -> {
                String recipeType;
                if (StringIndex.RECIPETYPE_MAP.containsKey(recipe.recipeType)) {
                    recipeType = StringIndex.RECIPETYPE_MAP.get(recipe.recipeType);
                } else {
                    recipeType = "GTORecipeTypes." + recipe.recipeType.registryName.getPath().toUpperCase() + "_RECIPES";
                }
                stringBuilder.append("\n").append(recipeType).append(".recipeBuilder(GTOCore.id(\"").append(StringUtils.decompose('/', recipe.getId().getPath())[1]).append("\"))\n");
                if (recipe.inputs.containsKey(ItemRecipeCapability.CAP)) {
                    for (Content content : recipe.inputs.get(ItemRecipeCapability.CAP)) {
                        String i = StringConverter.fromItem(ItemRecipeCapability.CAP.of(content.getContent()), 1);
                        if (i == null) {
                            GTOCore.LOGGER.error("输入物品为空：{}{}", recipe.getId(), content);
                        } else {
                            if (content.chance == ChanceLogic.getMaxChancedValue()) {
                                stringBuilder.append(".inputItems(").append(i).append(")").append("\n");
                            } else if (content.chance == 0) {
                                if (ItemRecipeCapability.CAP.of(content.getContent()).getItems()[0].is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                                    stringBuilder.append(".circuitMeta(").append(IntCircuitBehaviour.getCircuitConfiguration(ItemRecipeCapability.CAP.of(content.getContent()).getItems()[0])).append(")").append("\n");
                                } else {
                                    stringBuilder.append(".notConsumable(").append(i).append(")").append("\n");
                                }
                            } else {
                                stringBuilder.append(".chancedInput(").append(i).append(", ").append(content.chance).append(", ").append(content.tierChanceBoost).append(")").append("\n");
                            }
                        }
                    }
                }
                if (recipe.inputs.containsKey(FluidRecipeCapability.CAP)) {
                    for (Content content : recipe.inputs.get(FluidRecipeCapability.CAP)) {
                        String i = StringConverter.fromFluid(FluidRecipeCapability.CAP.of(content.getContent()));
                        if (i == null) {
                            GTOCore.LOGGER.error("输入流体为空：{}{}", recipe.getId(), content);
                        } else {
                            if (content.chance == ChanceLogic.getMaxChancedValue()) {
                                stringBuilder.append(".inputFluids(").append(i).append(")").append("\n");
                            } else {
                                stringBuilder.append(".notConsumableFluid(").append(i).append(")").append("\n");
                            }
                        }
                    }
                }
                if (recipe.outputs.containsKey(ItemRecipeCapability.CAP)) {
                    for (Content content : recipe.outputs.get(ItemRecipeCapability.CAP)) {
                        String i = StringConverter.fromItem(ItemRecipeCapability.CAP.of(content.getContent()), 1);
                        if (i == null) {
                            GTOCore.LOGGER.error("输出物品为空：{}{}", recipe.getId(), content);
                        } else {
                            if (content.chance == ChanceLogic.getMaxChancedValue()) {
                                stringBuilder.append(".outputItems(").append(i).append(")").append("\n");
                            } else {
                                stringBuilder.append(".chancedOutput(").append(i).append(", ").append(content.chance).append(", ").append(content.tierChanceBoost).append(")").append("\n");
                            }
                        }
                    }
                }
                if (recipe.outputs.containsKey(FluidRecipeCapability.CAP)) {
                    for (Content content : recipe.outputs.get(FluidRecipeCapability.CAP)) {
                        String i = StringConverter.fromFluid(FluidRecipeCapability.CAP.of(content.getContent()));
                        if (i == null) {
                            GTOCore.LOGGER.error("输出流体为空：{}{}", recipe.getId(), content);
                        } else {
                            if (content.chance == ChanceLogic.getMaxChancedValue()) {
                                stringBuilder.append(".outputFluids(").append(i).append(")").append("\n");
                            } else {
                                stringBuilder.append(".chancedFluidOutput(").append(i).append(", ").append(content.chance).append(", ").append(content.tierChanceBoost).append(")").append("\n");
                            }
                        }
                    }
                }
                long eu = RecipeHelper.getInputEUt(recipe);
                if (eu > 0) {
                    stringBuilder.append(".EUt(").append(eu).append(eu > Integer.MAX_VALUE ? "L" : "").append(")").append("\n");
                } else {
                    eu = RecipeHelper.getOutputEUt(recipe);
                    if (eu > 0) {
                        stringBuilder.append(".EUt(-").append(eu).append(eu > Integer.MAX_VALUE ? "L" : "").append(")").append("\n");
                    }
                }
                stringBuilder.append(".duration(").append(recipe.duration).append(")").append("\n");
                for (RecipeCondition condition : recipe.conditions) {
                    if (condition instanceof CleanroomCondition cleanroomCondition) {
                        CleanroomType cleanroomType = cleanroomCondition.getCleanroom();
                        String cleanroom = cleanroomType == GTOCleanroomType.LAW_CLEANROOM ? "GTOCleanroomType.LAW_CLEANROOM" : "CleanroomType." + cleanroomType.getName().toUpperCase();
                        stringBuilder.append(".cleanroom(").append(cleanroom).append(")").append("\n");
                    }
                    if (condition instanceof DimensionCondition dimensionCondition) {
                        stringBuilder.append(".dimension(").append(dimensionCondition.getDimension().toString()).append(")").append("\n");
                    }
                    if (condition instanceof GravityCondition gravityCondition) {
                        stringBuilder.append(".addCondition(").append("new GravityCondition(").append(gravityCondition.isZero()).append("))").append("\n");
                    }
                    if (condition instanceof VacuumCondition vacuumCondition) {
                        stringBuilder.append(".addCondition(").append("new VacuumCondition(").append(vacuumCondition.getTier()).append("))").append("\n");
                    }
                }
                if (!recipe.data.isEmpty()) {
                    if (recipe.data.contains("ebf_temp")) {
                        stringBuilder.append(".blastFurnaceTemp(").append(recipe.data.getInt("ebf_temp")).append(")").append("\n");
                    }
                    if (recipe.data.contains("eu_to_start")) {
                        stringBuilder.append(".fusionStartEU(").append(recipe.data.getInt("eu_to_start")).append(")").append("\n");
                    }
                    if (recipe.data.contains(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER)) {
                        stringBuilder.append(".addData(GTOValue.COMPONENT_ASSEMBLY_CASING_TIER, ").append(recipe.data.getInt(GTOValues.COMPONENT_ASSEMBLY_CASING_TIER)).append(")").append("\n");
                    }
                    if (recipe.data.contains(GTOValues.POWER_MODULE_TIER)) {
                        stringBuilder.append(".addData(GTOValue.POWER_MODULE_TIER, ").append(recipe.data.getInt(GTOValues.POWER_MODULE_TIER)).append(")").append("\n");
                    }
                    if (recipe.data.contains(GTOValues.STELLAR_CONTAINMENT_TIER)) {
                        stringBuilder.append(".addData(GTOValue.STELLAR_CONTAINMENT_TIER, ").append(recipe.data.getInt(GTOValues.STELLAR_CONTAINMENT_TIER)).append(")").append("\n");
                    }
                    if (recipe.data.contains("FRheat")) {
                        stringBuilder.append(".addData(\"FRheat\", ").append(recipe.data.getInt("FRheat")).append(")").append("\n");
                    }
                    if (recipe.data.contains("nano_forge_tier")) {
                        stringBuilder.append(".addData(\"nano_forge_tier\", ").append(recipe.data.getInt("nano_forge_tier")).append(")").append("\n");
                    }
                    if (recipe.data.contains("filter_casing")) {
                        stringBuilder.append(".addData(\"filter_casing\", ").append(recipe.data.getInt("filter_casing")).append(")").append("\n");
                    }
                    if (recipe.data.contains("radioactivity")) {
                        stringBuilder.append(".addData(\"radioactivity\", ").append(recipe.data.getInt("radioactivity")).append(")").append("\n");
                    }
                    if (recipe.data.contains("grindball")) {
                        stringBuilder.append(".addData(\"grindball\", ").append(recipe.data.getInt("grindball")).append(")").append("\n");
                    }
                    if (recipe.data.contains("ev_min")) {
                        stringBuilder.append(".addData(\"ev_min\", ").append(recipe.data.getInt("ev_min")).append(")").append("\n");
                    }
                    if (recipe.data.contains("ev_max")) {
                        stringBuilder.append(".addData(\"ev_max\", ").append(recipe.data.getInt("ev_max")).append(")").append("\n");
                    }
                    if (recipe.data.contains("evt")) {
                        stringBuilder.append(".addData(\"evt\", ").append(recipe.data.getInt("evt")).append(")").append("\n");
                    }
                    if (recipe.data.contains("module")) {
                        stringBuilder.append(".addData(\"module\", ").append(recipe.data.getInt("module")).append(")").append("\n");
                    }
                    if (recipe.data.contains("special")) {
                        stringBuilder.append(".addData(\"special\", ").append(recipe.data.getBoolean("special")).append(")").append("\n");
                    }
                }
                stringBuilder.append(".save(provider);\n");
            });
            if (!stringBuilder.isEmpty() && context.getLevel().isClientSide()) GTOCore.LOGGER.error(stringBuilder.toString());
            // 冲突检查
            if (GTOConfig.INSTANCE.recipeCheck) {
                for (GTRecipeType recipeType : GTRegistries.RECIPE_TYPES) {
                    if (recipeType == GTRecipeTypes.BREWING_RECIPES) continue;
                    if (recipeType == GTRecipeTypes.SCANNER_RECIPES) continue;
                    if (recipeType == GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES) continue;
                    Map<String, Set<String>> stringSetMap = new Object2ObjectOpenHashMap<>();
                    for (Set<GTRecipe> recipes : recipeType.getCategoryMap().values()) {
                        for (GTRecipe recipe : recipes) {
                            String id = recipe.id.toString();
                            Set<String> input = new ObjectOpenHashSet<>();
                            if (recipe.inputs.containsKey(ItemRecipeCapability.CAP)) {
                                for (Content content : recipe.inputs.get(ItemRecipeCapability.CAP)) {
                                    Ingredient ingredient = ItemRecipeCapability.CAP.of(content.getContent());
                                    Ingredient inner = ingredient;
                                    if (ingredient instanceof SizedIngredient sizedIngredient) {
                                        inner = sizedIngredient.getInner();
                                    }
                                    a:
                                    for (Ingredient.Value value : ((IngredientAccessor) inner).getValues()) {
                                        if (value instanceof Ingredient.ItemValue itemValue) {
                                            Collection<ItemStack> stacks = itemValue.getItems();
                                            if (stacks.isEmpty()) {
                                                GTOCore.LOGGER.error("配方 {} 存在空物品输入", id);
                                                continue;
                                            }
                                            for (ItemStack stack : stacks) {
                                                if (stack.isEmpty()) continue;
                                                if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                                                    input.add("c" + IntCircuitBehaviour.getCircuitConfiguration(stack));
                                                } else {
                                                    input.add(ItemUtils.getId(stack));
                                                }
                                                break a;
                                            }
                                        } else if (value instanceof Ingredient.TagValue tagValue) {
                                            TagKey<Item> tag = ((TagValueAccessor) tagValue).getTag();
                                            input.add(tag.location().toString());
                                            break;
                                        }
                                    }
                                }
                            }
                            if (recipe.inputs.containsKey(FluidRecipeCapability.CAP)) {
                                for (Content content : recipe.inputs.get(FluidRecipeCapability.CAP)) {
                                    FluidStack[] stacks = FluidRecipeCapability.CAP.of(content.getContent()).getStacks();
                                    if (stacks.length == 0) {
                                        GTOCore.LOGGER.error("配方 {} 存在空流体输入", id);
                                        continue;
                                    }
                                    input.add(FluidUtils.getId(stacks[0].getFluid()));
                                }
                            }
                            if (input.isEmpty()) continue;
                            stringSetMap.put(id, input);
                        }
                    }
                    Set<Set<String>> cache = new ObjectOpenHashSet<>();
                    stringSetMap.forEach((id, set) -> {
                        Map<String, Set<String>> map = new Object2ObjectOpenHashMap<>();
                        map.putAll(stringSetMap);
                        map.remove(id);
                        map.forEach((k, v) -> {
                            Set<String> object = new ObjectOpenHashSet<>();
                            object.add(id);
                            object.add(k);
                            if (cache.contains(object)) return;
                            if (set.containsAll(v)) {
                                cache.add(object);
                                GTOCore.LOGGER.error("\n{} 与 {} 冲突\n{}\n{}", id, k, set, v);
                            }
                        });
                    });
                }
            }
            return InteractionResult.CONSUME;
        }
        MetaMachine metaMachine = MetaMachine.getMachine(context.getLevel(), context.getClickedPos());
        if (metaMachine instanceof IRecipeLogicMachine) {
            isGT = true;
            machine = CACHE.computeIfAbsent(metaMachine, DummyMachine::createDummyMachine);
        } else if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof CraftingTableBlock) {
            isGT = false;
            machine = POS_CACHE.computeIfAbsent(context.getClickedPos(), p -> DummyMachine.createDummyMachine(BlockEntityType.CHEST, p, GTMachines.ASSEMBLER[1].defaultBlockState(), GTRecipeTypes.ASSEMBLER_RECIPES));
        } else {
            return InteractionResult.PASS;
        }
        IItemUIFactory.super.use(context.getItemInHand().getItem(), context.getLevel(), context.getPlayer(), context.getHand());
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        ItemStack heldItem = player.getItemInHand(usedHand);
        return InteractionResultHolder.success(heldItem);
    }

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder holder, Player entityPlayer) {
        return new ModularUI(176, 166, holder, entityPlayer).widget(new FancyMachineUIWidget(this, 176, 166));
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget widget) {
        GTRecipeTypeUI recipeUI = new GTRecipeTypeUI(machine.recipeType) {

            @Override
            protected WidgetGroup addInventorySlotGroup(boolean isOutputs, boolean isSteam, boolean isHighPressure) {
                int maxCount = 0;
                int totalR = 0;
                TreeMap<RecipeCapability<?>, Integer> map = new TreeMap<>(RecipeCapability.COMPARATOR);
                if (isOutputs) {
                    for (var value : machine.recipeType.maxOutputs.entrySet()) {
                        if (value.getKey().doRenderSlot) {
                            int val = value.getValue();
                            if (val > maxCount) {
                                maxCount = Math.min(val, 3);
                            }
                            totalR += (val + 2) / 3;
                            map.put(value.getKey(), val);
                        }
                    }
                } else {
                    for (var value : machine.recipeType.maxInputs.entrySet()) {
                        if (value.getKey().doRenderSlot) {
                            int val = value.getValue();
                            if (val > maxCount) {
                                maxCount = Math.min(val, 3);
                            }
                            totalR += (val + 2) / 3;
                            map.put(value.getKey(), val);
                        }
                    }
                }
                WidgetGroup group = new WidgetGroup(0, 0, maxCount * 18 + 8, totalR * 18 + 8);
                int index = 0;
                for (var entry : map.entrySet()) {
                    RecipeCapability<?> cap = entry.getKey();
                    boolean i = cap instanceof ItemRecipeCapability;
                    if (i || isGT) {
                        if (cap.getWidgetClass() == null) {
                            continue;
                        }
                        int capCount = entry.getValue();
                        for (int slotIndex = 0; slotIndex < capCount; slotIndex++) {
                            int finalSlotIndex = slotIndex;
                            var slot = i ? new MyPhantomSlotWidget() : new PhantomFluidWidget(null, 0, 0, 0, 18, 18, isOutputs ? () -> machine.exportFluids.getFluidInTank(finalSlotIndex) : () -> machine.importFluids.getFluidInTank(finalSlotIndex), isOutputs ? f -> machine.exportFluids.setFluidInTank(finalSlotIndex, f) : f -> machine.importFluids.setFluidInTank(finalSlotIndex, f));
                            slot.setSelfPosition(new Position((index % 3) * 18 + 4, (index / 3) * 18 + 4));
                            slot.setBackground(getOverlaysForSlot(isOutputs, cap, slotIndex == capCount - 1, isSteam, isHighPressure));
                            slot.setId(cap.slotName(isOutputs ? IO.OUT : IO.IN, slotIndex));
                            group.addWidget(slot);
                            index++;
                        }
                        index += (3 - (index % 3)) % 3;
                    }
                }
                return group;
            }

            @Override
            public IEditableUI<WidgetGroup, RecipeHolder> createEditableUITemplate(boolean isSteam, boolean isHighPressure) {
                return new IEditableUI.Normal<>(() -> {
                    var inputs = addInventorySlotGroup(false, isSteam, isHighPressure);
                    var outputs = addInventorySlotGroup(true, isSteam, isHighPressure);
                    var maxWidth = Math.max(inputs.getSize().width, outputs.getSize().width);
                    var group = new WidgetGroup(0, 0, 2 * maxWidth + 40, Math.max(inputs.getSize().height, outputs.getSize().height));
                    var size = group.getSize();
                    inputs.addSelfPosition((maxWidth - inputs.getSize().width) / 2, (size.height - inputs.getSize().height) / 2);
                    outputs.addSelfPosition(maxWidth + 40 + (maxWidth - outputs.getSize().width) / 2, (size.height - outputs.getSize().height) / 2);
                    group.addWidget(inputs);
                    group.addWidget(outputs);
                    var progressWidget = new ProgressWidget(ProgressWidget.JEIProgress, maxWidth + 10, size.height / 2 - 10, 20, 20, getProgressBarTexture());
                    progressWidget.setId("progress");
                    group.addWidget(progressWidget);
                    progressWidget.setProgressTexture(getProgressBarTexture());
                    return group;
                }, (template, recipeHolder) -> {
                    for (var capabilityEntry : recipeHolder.storages().rowMap().entrySet()) {
                        IO io = capabilityEntry.getKey();
                        for (var storagesEntry : capabilityEntry.getValue().entrySet()) {
                            RecipeCapability<?> cap = storagesEntry.getKey();
                            Object storage = storagesEntry.getValue();
                            Class<? extends Widget> widgetClass = cap.getWidgetClass();
                            if (widgetClass != null) {
                                WidgetUtils.widgetByIdForEach(template, "^%s_[0-9]+$".formatted(cap.slotName(io)), widgetClass, widget -> {
                                    var index = WidgetUtils.widgetIdIndex(widget);
                                    cap.applyWidgetInfo(widget, index, false, io, recipeHolder, machine.recipeType, null, null, storage, 0, 0);
                                    if (widget instanceof TankWidget tankWidget) {
                                        tankWidget.setAllowClickDrained(true).setAllowClickFilled(true);
                                    } else if (widget instanceof SlotWidget slotWidget) slotWidget.setCanTakeItems(true).setCanPutItems(true);
                                });
                            }
                        }
                    }
                });
            }
        };
        var editableUI = new EditableMachineUI("simple", GTOCore.id("re"), () -> {
            WidgetGroup template = recipeUI.createEditableUITemplate(false, false).createDefault();

            WidgetGroup group = new WidgetGroup(0, 0, template.getSize().width, Math.max(template.getSize().height, 78));
            template.setSelfPosition(new Position(0, (group.getSize().height - template.getSize().height) / 2));
            group.addWidget(template);
            return group;
        }, (template, m) -> {
            var storages = Tables.newCustomTable(new EnumMap<>(IO.class), LinkedHashMap<RecipeCapability<?>, Object>::new);
            storages.put(IO.IN, ItemRecipeCapability.CAP, machine.importItems);
            storages.put(IO.OUT, ItemRecipeCapability.CAP, machine.exportItems);
            if (isGT) {
                storages.put(IO.IN, FluidRecipeCapability.CAP, machine.importFluids);
                storages.put(IO.OUT, FluidRecipeCapability.CAP, machine.exportFluids);
            }
            recipeUI.createEditableUITemplate(false, false).setupUI(template, new GTRecipeTypeUI.RecipeHolder(() -> 0, storages, new CompoundTag(), Collections.emptyList(), false, false));
        });
        var template = editableUI.createCustomUI();
        if (template == null) {
            template = editableUI.createDefault();
        }
        editableUI.setupUI(template, machine);
        int x = template.getSize().width - getXOffset(machine.recipeType) - 18;
        int y = template.getSize().height - 10;
        if (isGT) {
            template.addWidget(new AETextInputButtonWidget(x - 48, y - 70, 76, 12)
                    .setText(String.valueOf(machine.id))
                    .setOnConfirm(machine::setId)
                    .setButtonTooltips(Component.literal("ID")));
            template.addWidget(new AETextInputButtonWidget(x - 36, y - 55, 64, 12)
                    .setText(String.valueOf(machine.circuit))
                    .setOnConfirm(machine::setCircuit)
                    .setButtonTooltips(Component.literal("Circuit")));
            template.addWidget(new AETextInputButtonWidget(x - 36, y - 40, 64, 12)
                    .setText(String.valueOf(machine.eut))
                    .setOnConfirm(machine::setEUt)
                    .setButtonTooltips(Component.literal("EUt")));
            template.addWidget(new AETextInputButtonWidget(x - 36, y - 25, 64, 12)
                    .setText(String.valueOf(machine.duration))
                    .setOnConfirm(machine::setDuration)
                    .setButtonTooltips(Component.literal("Duration")));
        }
        template.addWidget(new ButtonWidget(x, y, 16, 16, new GuiTextureGroup(GuiTextures.BUTTON, new TextTexture("X")), cd -> {
            if (cd.isRemote) return;
            StringBuilder stringBuilder = new StringBuilder();
            if (isGT) {
                stringBuilder.append("\nevent.recipes.gtceu.").append(machine.recipeType.registryName.getPath()).append("(\"gtocore:").append(machine.id).append("\")\n");
                for (int i = 0; i < machine.importItems.getSlots(); i++) {
                    ItemStack stack = machine.importItems.getStackInSlot(i);
                    if (stack.isEmpty()) continue;
                    stringBuilder.append(".itemInputs(\"");
                    if (stack.getCount() > 1) {
                        stringBuilder.append(stack.getCount()).append("x ");
                    }
                    stringBuilder.append(getItemId(stack.getItem())).append("\")\n");
                }
                for (int i = 0; i < machine.exportItems.getSlots(); i++) {
                    ItemStack stack = machine.exportItems.getStackInSlot(i);
                    if (stack.isEmpty()) continue;
                    stringBuilder.append(".itemOutputs(\"");
                    if (stack.getCount() > 1) {
                        stringBuilder.append(stack.getCount()).append("x ");
                    }
                    stringBuilder.append(getItemId(stack.getItem())).append("\")\n");
                }
                for (int i = 0; i < machine.importFluids.getSize(); i++) {
                    FluidStack stack = machine.importFluids.getFluidInTank(i);
                    if (stack.isEmpty()) continue;
                    stringBuilder.append(".inputFluids(\"").append(ForgeRegistries.FLUIDS.getKey(stack.getFluid())).append(" ").append(stack.getAmount()).append("\")\n");
                }
                for (int i = 0; i < machine.exportFluids.getSize(); i++) {
                    FluidStack stack = machine.exportFluids.getFluidInTank(i);
                    if (stack.isEmpty()) continue;
                    stringBuilder.append(".outputFluids(\"").append(ForgeRegistries.FLUIDS.getKey(stack.getFluid())).append(" ").append(stack.getAmount()).append("\")\n");
                }
                if (machine.circuit > 0) {
                    stringBuilder.append(".circuit(").append(machine.circuit).append(")\n");
                }
                if (machine.eut != 0) {
                    stringBuilder.append(".EUt(").append(machine.eut).append(")\n");
                }
                stringBuilder.append(".duration(").append(machine.duration).append(")\n");
            } else {
                stringBuilder.append("\nevent.shaped(\"").append(ItemUtils.getId(machine.exportItems.getStackInSlot(0))).append("\", [\n    \"");
                char c = 'A';
                Map<String, Character> map = new LinkedHashMap<>();
                for (int i = 0, j = 0; i < machine.importItems.getSlots(); i++, j++) {
                    ItemStack stack = machine.importItems.getStackInSlot(i);
                    String id = getItemId(stack.getItem());
                    if (!stack.isEmpty() && !map.containsKey(id)) {
                        map.put(id, c);
                        c++;
                    }
                    char d = stack.isEmpty() ? ' ' : map.get(id);
                    if (j > 2) {
                        stringBuilder.append("\",\n    \"").append(d);
                        j = 0;
                    } else {
                        stringBuilder.append(d);
                    }
                }
                stringBuilder.append("\"\n], {");
                map.forEach((k, v) -> stringBuilder.append("\n    ").append(v).append(":").append(" \"").append(k).append("\","));
                stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("\n})");
            }
            ConsoleJS.SERVER.info(stringBuilder.toString());
        }));
        return template;
    }

    private static String getItemId(Item item) {
        if (Tooltips.universalCircuitSet.contains(item)) {
            for (int tier : GTMachineUtils.ALL_TIERS) {
                if (GTOItems.UNIVERSAL_CIRCUIT[tier].is(item)) {
                    return "#gtceu:circuits/" + GTValues.VN[tier].toLowerCase();
                }
            }
        }
        return ItemUtils.getId(item);
    }

    private static int getXOffset(GTRecipeType recipe) {
        if (recipe.getRecipeUI().getOriginalWidth() != recipe.getRecipeUI().getJEISize().width) {
            return (recipe.getRecipeUI().getJEISize().width - recipe.getRecipeUI().getOriginalWidth()) / 2;
        }
        return 0;
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        sideTabs.setMainTab(this);
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(GTOItems.ULV_ROBOT_ARM.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.gtocore.recipe_editor");
    }

    private static class MyPhantomSlotWidget extends PhantomSlotWidget {

        @Override
        public ItemStack slotClickPhantom(Slot slot, int mouseButton, ClickType clickTypeIn, ItemStack stackHeld) {
            if (isRemote()) return slot.getItem();
            return super.slotClickPhantom(slot, mouseButton, clickTypeIn, stackHeld);
        }
    }
}
