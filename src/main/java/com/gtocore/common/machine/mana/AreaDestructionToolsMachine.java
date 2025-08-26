package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.utils.Explosion.ChunkExplosion;
import com.gtolib.utils.Explosion.CylinderExplosion;
import com.gtolib.utils.Explosion.SphereExplosion;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AreaDestructionToolsMachine extends MetaMachine implements IFancyUIMachine {

    @Persisted
    private final NotifiableItemStackHandler inventory;

    @Persisted
    private final Object2IntMap<Item> itemStatistics = new Object2IntOpenHashMap<>();

    int model = 0;
    int ExplosiveEnergy = 0;
    int ExplosiveYield = 0;

    public AreaDestructionToolsMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 9, IO.BOTH);
    }

    private void updateItemStatistics() {
        Level level = getLevel();
        if (level == null) return;
        itemStatistics.clear();
        model = 0;
        ExplosiveEnergy = 0;
        ExplosiveYield = 0;

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) itemStatistics.mergeInt(stack.getItem(), stack.getCount(), Integer::sum);
        }

        for (Object2IntMap.Entry<Item> entry : itemStatistics.object2IntEntrySet()) {
            if (entry.getKey() == GTItems.SHAPE_MOLD_BALL.asItem()) model = 1;
            else if (entry.getKey() == GTItems.SHAPE_MOLD_CYLINDER.asItem()) model = 2;
            else if (entry.getKey() == GTItems.SHAPE_MOLD_BLOCK.asItem()) model = 3;
            else if (entry.getKey() == GTBlocks.INDUSTRIAL_TNT.asItem()) ExplosiveEnergy += 30 * entry.getIntValue();
            else if (entry.getKey() == GTOBlocks.NUKE_BOMB.asItem()) ExplosiveEnergy += 2048 * entry.getIntValue();
            else if (entry.getKey() == GTOBlocks.NAQUADRIA_CHARGE.asItem()) ExplosiveEnergy += 3200 * entry.getIntValue();
            else if (entry.getKey() == GTOBlocks.LEPTONIC_CHARGE.asItem()) ExplosiveEnergy += 2048000 * entry.getIntValue();
            else if (entry.getKey() == GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem()) ExplosiveEnergy += 32000000 * entry.getIntValue();
        }

        if (model == 1) ExplosiveYield = (int) Math.cbrt((double) ExplosiveEnergy / 4) * 10;
        else if (model == 2) ExplosiveYield = (int) Math.sqrt((double) ExplosiveEnergy / level.getHeight()) * 18;
        else if (model == 3) ExplosiveYield = (int) Math.sqrt((double) ExplosiveEnergy / level.getHeight()) * 16;
    }

    private void triggerExplosion() {
        updateItemStatistics();
        BlockPos pos = getPos();
        Level level = getLevel();
        if (level == null) return;

        if (model == 0) return;
        else if (model == 1) SphereExplosion.explosion(pos, level, ExplosiveYield, true, true, false);
        else if (model == 2) CylinderExplosion.explosion(pos, level, ExplosiveYield, true, true, false);
        else if (model == 3) ChunkExplosion.explosion(pos, level, ExplosiveYield, true, true, false);

        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    // 创建UI组件
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(GuiTextures.DISPLAY)
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick)));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int slotIndex = y * 3 + x;
                group.addWidget(new SlotWidget(inventory, slotIndex, 133 + x * 18, 68 + y * 18, true, true).setBackground(GuiTextures.SLOT));
            }
        }

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private void addDisplayText(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.update_stats")
                .append(ComponentPanelWidget.withButton(Component.literal(" [\uD83D\uDD04]"), "updatestats")));

        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.detonate_instruction")
                .append(ComponentPanelWidget.withButton(Component.literal(" [\uD83D\uDCA5]"), "detonate")));

        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.model." + model));
        textList.add(Component.translatable("gtocore.machine.area_destruction_tools.explosive_yield", ExplosiveYield));
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("updatestats".equals(componentData)) {
                updateItemStatistics();
            } else if ("detonate".equals(componentData)) {
                triggerExplosion();
            }
        }
    }

    // 其他必要的方法
    @Override
    public void onLoad() {
        super.onLoad();
        updateItemStatistics();
    }
}
