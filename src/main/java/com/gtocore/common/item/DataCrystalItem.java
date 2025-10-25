package com.gtocore.common.item;

import com.gtocore.api.lang.ComponentListSupplier;
import com.gtocore.api.placeholder.IPlaceholder;
import com.gtocore.data.recipe.research.AnalyzeData;

import com.gtolib.api.item.tool.IExDataItem;
import com.gtolib.utils.RLUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.gtocore.data.recipe.builder.research.ExResearchManager.*;
import static com.gtolib.utils.RegistriesUtils.getItem;

public class DataCrystalItem extends Item implements IExDataItem, IPlaceholder<Object, ItemStack, Void> {

    public DataCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean requireDataBank() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return;

        if (tag.contains(EMPTY_NBT_TAG)) {
            tooltip.add(Component.translatable("gtocore.tooltip.item.empty_data")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("gtocore.tooltip.item.empty_serial",
                    Component.literal(String.format("%08X", 0)).withStyle(ChatFormatting.YELLOW)));
        }

        // 处理扫描数据
        if (tag.contains(SCANNING_NBT_TAG)) {
            CompoundTag scanningTag = tag.getCompound(SCANNING_NBT_TAG);
            int serial = scanningTag.getInt(SCANNING_SERIAL_NBT_TAG);
            String scanningId = scanningTag.getString(SCANNING_ID_NBT_TAG);

            tooltip.add(Component.translatable("gtocore.tooltip.item.scanning_data")
                    .withStyle(ChatFormatting.AQUA));

            Object scanned = parseTargetFromScanningId(scanningId);
            if (scanned instanceof ItemStack itemStack) {
                tooltip.add(Component.translatable("gtocore.tooltip.item.scanned_things",
                        Component.literal(String.valueOf(itemStack.getCount())).withStyle(ChatFormatting.GREEN),
                        itemStack.getDisplayName().copy().withStyle(ChatFormatting.GOLD)));
            } else if (scanned instanceof FluidStack fluidStack) {
                tooltip.add(Component.translatable("gtocore.tooltip.item.scanned_things",
                        Component.literal(String.valueOf(fluidStack.getAmount())).withStyle(ChatFormatting.GREEN),
                        fluidStack.getDisplayName().copy().withStyle(ChatFormatting.LIGHT_PURPLE)));
            }

            tooltip.add(Component.translatable("gtocore.tooltip.item.scanning_serial",
                    Component.literal(String.format("%08X", serial)).withStyle(ChatFormatting.YELLOW)));
        }

        // 处理分析数据
        if (tag.contains(ANALYZE_NBT_TAG)) {
            CompoundTag analyzeTag = tag.getCompound(ANALYZE_NBT_TAG);
            int serial = analyzeTag.getInt(ANALYZE_SERIAL_NBT_TAG);
            String analyzeId = analyzeTag.getString(ANALYZE_ID_NBT_TAG);

            tooltip.add(Component.translatable("gtocore.tooltip.item.analyze_data")
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

            tooltip.add(Component.translatable("gtocore.tooltip.item.analyze_things",
                    Component.literal(I18n.get("gtocore.data." + analyzeId)).withStyle(ChatFormatting.GOLD)));

            tooltip.add(Component.translatable("gtocore.tooltip.item.analyze_serial",
                    Component.literal(String.format("%08X", serial)).withStyle(ChatFormatting.YELLOW)));

            ComponentListSupplier tooltipSupplier = AnalyzeData.INSTANCE.getTooltip(serial);
            if (tooltipSupplier != null) {
                tooltip.addAll(tooltipSupplier.get());
            }
        }
    }

    @Override
    public List<List<Object>> getTargetLists(ItemStack source) {
        Object target = getCurrentTarget(source, null);
        if (target == null) return Collections.emptyList();
        return Collections.singletonList(Collections.singletonList(target));
    }

    @Override
    public Object getCurrentTarget(ItemStack source, Void context) {
        CompoundTag tag = source.getTag();
        if (tag == null || !tag.contains(SCANNING_NBT_TAG)) return null;
        CompoundTag scanningTag = tag.getCompound(SCANNING_NBT_TAG);
        String scanningId = scanningTag.getString(SCANNING_ID_NBT_TAG);
        return parseTargetFromScanningId(scanningId);
    }

    @Nullable
    private static Object parseTargetFromScanningId(String idString) {
        String[] parts = idString.split("-", 3);
        if (parts.length != 3) return null;
        String countPart = parts[0];
        int count = Integer.parseInt(countPart.substring(0, countPart.length() - 1));
        String type = countPart.substring(countPart.length() - 1);
        String namespace = parts[1];
        String path = parts[2];
        if (type.equals("i")) {
            Item item = getItem(namespace, path);
            if (item != null && item != net.minecraft.world.item.Items.AIR) {
                return new ItemStack(item, count);
            }
        }
        if (type.equals("f")) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(RLUtils.fromNamespaceAndPath(namespace, path));
            if (fluid != null && fluid != Fluids.EMPTY) {
                return new FluidStack(fluid, count);
            }
        }
        return null;
    }
}
