package com.gtocore.common.item;

import com.gtolib.api.item.tool.ae2.patternTool.Ae2BaseProcessingPattern;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import appeng.api.inventories.InternalInventory;
import appeng.api.parts.IPart;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.parts.crafting.PatternProviderPart;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import static com.gtocore.common.data.GTOItems.PATTERN_MODIFIER_PRO;

public final class PatternModifierProBehavior implements IItemUIFactory, IFancyUIProvider {

    public static final PatternModifierProBehavior INSTANCE = new PatternModifierProBehavior();
    private int Ae2PatternGeneratorScale = 1;
    private long Ae2PatternGeneratorMaxItemStack = 1000000L;
    private long Ae2PatternGeneratorMaxFluidStack = 1000000L;
    private int Ae2PatternGeneratorDivScale = 1;
    private int Ae2PatternGeneratorAppliedNumber = 1;

    @Override
    public ModularUI createUI(HeldItemUIFactory.HeldItemHolder heldItemHolder, Player player) {
        return new ModularUI(206, 124, heldItemHolder, player).widget(new FancyMachineUIWidget(this, 206, 124)).background(GuiTextures.BACKGROUND);
    }

    private void setAe2PatternGeneratorAppliedNumber(String s) {
        Ae2PatternGeneratorAppliedNumber = Math.min(16, Integer.parseInt(s));
    }

    private void setAe2PatternGeneratorMaxFluidStack(String s) {
        Ae2PatternGeneratorMaxFluidStack = Integer.parseInt(s);
    }

    private void setAe2PatternGeneratorMaxItemStack(String s) {
        Ae2PatternGeneratorMaxItemStack = Integer.parseInt(s);
    }

    private void setAe2PatternGeneratorDivScale(String s) {
        Ae2PatternGeneratorDivScale = Math.min(Integer.MAX_VALUE, Integer.parseInt(s));
    }

    private void setAe2PatternGeneratorScale(String s) {
        Ae2PatternGeneratorScale = Math.min(Integer.MAX_VALUE, Integer.parseInt(s));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player instanceof ServerPlayer serverPlayer) {
            HeldItemUIFactory.INSTANCE.openUI(serverPlayer, usedHand);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            /*
             * shift右键乘法逻辑
             */
            if (serverPlayer.isShiftKeyDown()) {
                BlockPos pos = context.getClickedPos();
                Level level = context.getLevel();
                BlockEntity tile = level.getBlockEntity(pos);
                InternalInventory internalInventory;
                if (tile instanceof CableBusBlockEntity cable) {
                    // 获取点击方块坐标
                    Vec3 hitVec = context.getClickLocation();
                    // 计算具体点击位置
                    Vec3 hitInBlock = new Vec3(hitVec.x - pos.getX(), hitVec.y - pos.getY(), hitVec.z - pos.getZ());
                    IPart part = cable.getCableBus().selectPartLocal(hitInBlock).part;
                    internalInventory = (part instanceof PatternProviderPart providerPart) ? providerPart.getLogic().getPatternInv() : null;
                } else {
                    internalInventory = (tile instanceof PatternProviderBlockEntity providerBlock) ? providerBlock.getLogic().getPatternInv() : null;
                }
                if (internalInventory == null) {
                    serverPlayer.displayClientMessage(Component.literal("只能对着样板供应器使用").withStyle(), true);
                    return InteractionResult.FAIL;
                }
                for (int i = 0; i < Ae2PatternGeneratorAppliedNumber; ++i) {
                    Int2ObjectOpenHashMap<ItemStack> newItemStackHashMap = new Int2ObjectOpenHashMap<>();
                    for (int slot = 0; slot < internalInventory.size(); slot++) {
                        ItemStack itemStack = internalInventory.getStackInSlot(slot);
                        if (!itemStack.isEmpty()) {
                            ItemStack patternItemStack = getNewPatternItemStack(serverPlayer, itemStack);
                            newItemStackHashMap.put(slot, patternItemStack);
                        }
                    }
                    newItemStackHashMap.forEach((slot, itemStack) -> {
                        internalInventory.extractItem(slot, 1, false);
                        internalInventory.insertItem(slot, itemStack, false);
                    });
                }
                serverPlayer.displayClientMessage(Component.literal("已更新内部的样板，应用了%s次".formatted(Ae2PatternGeneratorAppliedNumber)), true);
            }
            if (!serverPlayer.isShiftKeyDown()) {
                serverPlayer.displayClientMessage(Component.literal("右键空气打开GUI"), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private ItemStack getNewPatternItemStack(ServerPlayer serverPlayer, ItemStack itemStack) {
        Ae2BaseProcessingPattern ae2BaseProcessingPattern = new Ae2BaseProcessingPattern(itemStack, serverPlayer);
        ae2BaseProcessingPattern.setScale(Ae2PatternGeneratorScale, false, Ae2PatternGeneratorMaxItemStack, Ae2PatternGeneratorMaxFluidStack);
        ae2BaseProcessingPattern.setScale(Ae2PatternGeneratorDivScale, true);
        return ae2BaseProcessingPattern.getPatternItemStack();
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
        WidgetGroup a = new WidgetGroup(0, 0, 206, 124);
        a.addWidget(new ImageWidget(8, 8, 190, 108, GuiTextures.DISPLAY))
                .addWidget(new LabelWidget(12, 12, "item.gtocore.pattern_modifier_pro.name"))
                .addWidget(new LabelWidget(12, 22, "gtocore.patternModifierPro.0"))
                .addWidget(new TextFieldWidget(-60, 40 + 2, 60, 12, () -> String.valueOf(Ae2PatternGeneratorScale), this::setAe2PatternGeneratorScale))
                .addWidget(new LabelWidget(12, 40 + 4, "gtocore.patternModifierPro.1"))
                .addWidget(new TextFieldWidget(-60, 54 + 2, 60, 12, () -> String.valueOf(Ae2PatternGeneratorDivScale), this::setAe2PatternGeneratorDivScale))
                .addWidget(new LabelWidget(12, 54 + 4, "gtocore.patternModifierPro.2"))
                .addWidget(new TextFieldWidget(-60, 68 + 2, 60, 12, () -> String.valueOf(Ae2PatternGeneratorMaxItemStack), this::setAe2PatternGeneratorMaxItemStack))
                .addWidget(new LabelWidget(12, 68 + 4, "gtocore.patternModifierPro.3"))
                .addWidget(new TextFieldWidget(-60, 82 + 2, 60, 12, () -> String.valueOf(Ae2PatternGeneratorMaxFluidStack), this::setAe2PatternGeneratorMaxFluidStack))
                .addWidget(new LabelWidget(12, 82 + 4, "gtocore.patternModifierPro.4"))
                .addWidget(new TextFieldWidget(-60, 96 + 2, 60, 12, () -> String.valueOf(Ae2PatternGeneratorAppliedNumber), this::setAe2PatternGeneratorAppliedNumber))
                .addWidget(new LabelWidget(12, 96 + 4, "gtocore.patternModifierPro.5"));
        return a;
    }

    @Override
    public void attachSideTabs(TabsWidget configuratorPanel) {
        configuratorPanel.setMainTab(this);
    }

    @Override
    public IGuiTexture getTabIcon() {
        return new ItemStackTexture(PATTERN_MODIFIER_PRO.asStack());
    }

    @Override
    public Component getTitle() {
        return Component.literal("样板修改器Pro");
    }
}
