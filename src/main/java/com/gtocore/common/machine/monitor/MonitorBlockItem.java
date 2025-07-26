package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.google.common.collect.ImmutableList;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MonitorBlockItem extends MetaMachineItem {

    private static final String TEXTURE_PATH_PREFIX = "textures/item/monitor/";
    private static final List<ResourceLocation> itemList = new ArrayList<>();

    public MonitorBlockItem(IMachineBlock block, Properties properties) {
        super(block, properties);
    }

    public static void addItem(ResourceLocation item) {
        if (!itemList.contains(item)) {
            itemList.add(item);
        }
    }

    public static List<ResourceLocation> getItemList() {
        return ImmutableList.copyOf(itemList);
    }

    public static ResourceLocation getTexturePath(ResourceLocation rl) {
        return rl.withPrefix(TEXTURE_PATH_PREFIX).withSuffix(".png");
    }

    @Override
    public @Nullable IRenderer getRenderer(@NotNull ItemStack stack) {
        return super.getRenderer(stack);
    }
}
