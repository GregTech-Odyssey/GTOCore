package com.gtocore.common.item;

import com.gtolib.api.item.tool.IExDataItem;

import com.gregtechceu.gtceu.api.item.component.IAddInformation;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.gtolib.api.recipe.research.ExResearchManager.*;
import static com.gtolib.utils.RegistriesUtils.getItem;

public class ExDataItemBehavior implements IAddInformation, IExDataItem {

    @Override
    public boolean requireDataBank() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return;

        // 处理扫描数据
        if (tag.contains(SCANNING_NBT_TAG)) {
            CompoundTag scanningTag = tag.getCompound(SCANNING_NBT_TAG);

            int serial = scanningTag.getInt(SCANNING_SERIAL_NBT_TAG);
            String scanningId = scanningTag.getString(SCANNING_ID_NBT_TAG);

            tooltip.add(Component.translatable("gtocore.tooltip.item.scanning_data")
                    .withStyle(ChatFormatting.AQUA));

            // 解析并显示物品/流体信息
            Optional<Component> info = parseItemOrFluidInfo(scanningId);
            info.ifPresent(tooltip::add);

            tooltip.add(Component.translatable("gtocore.tooltip.item.scanning_serial",
                    Component.literal(String.format("%08X", serial)).withStyle(ChatFormatting.YELLOW)));

        }

        // 处理分析数据
        if (tag.contains(ANALYZE_NBT_TAG)) {
            CompoundTag analyzeTag = tag.getCompound(ANALYZE_NBT_TAG);

            int serial = analyzeTag.getInt(ANALYZE_SERIAL_NBT_TAG);
            String analyzeId = analyzeTag.getString(ANALYZE_ID_NBT_TAG);

            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("gtocore.tooltip.item.analyze_data")
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

            // 解析并显示物品/流体信息
            Optional<Component> info = parseItemOrFluidInfo(analyzeId);
            info.ifPresent(tooltip::add);

            tooltip.add(Component.translatable("gtocore.tooltip.item.analyze_serial",
                    Component.literal(String.format("%08X", serial)).withStyle(ChatFormatting.YELLOW)));

        }
    }

    /**
     * 解析物品或流体信息
     *
     * @param idString 格式为 "数量x-命名空间-路径" 的字符串
     * @return 包含解析结果的文本组件（如果成功）
     */
    public static Optional<Component> parseItemOrFluidInfo(String idString) {
        // 分割字符串为三部分: [数量x, 命名空间, 路径]
        String[] parts = idString.split("-", 3);
        if (parts.length != 3) return Optional.empty();

        // 提取数量 (移除末尾的 'x')
        String countPart = parts[0];
        int count = Integer.parseInt(countPart.substring(0, countPart.length() - 1));
        String namespace = parts[1];
        String path = parts[2];

        // 尝试作为物品解析
        Item item = getItem(namespace, path);
        if (item != Items.BARRIER) {
            ItemStack stack = new ItemStack(item, 1);
            return Optional.of(Component.translatable("gtocore.tooltip.item.scanned_things",
                    Component.literal(String.valueOf(count)).withStyle(ChatFormatting.GREEN),
                    stack.getDisplayName().copy().withStyle(ChatFormatting.GOLD)));
        }

        // 尝试作为流体解析
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(namespace, path));
        if (fluid != null) {
            FluidStack stack = new FluidStack(fluid, 1);
            return Optional.of(Component.translatable("gtocore.tooltip.item.scanned_things",
                    Component.literal(String.valueOf(count)).withStyle(ChatFormatting.GREEN),
                    stack.getDisplayName().copy().withStyle(ChatFormatting.BLUE)));
        }

        return Optional.empty();
    }
}
