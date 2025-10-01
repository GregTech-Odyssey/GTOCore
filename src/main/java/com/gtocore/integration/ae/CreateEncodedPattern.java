package com.gtocore.integration.ae;

import appeng.api.stacks.GenericStack;
import appeng.block.networking.CableBusBlock;
import appeng.core.definitions.AEItems;
import com.direwolf20.buildinggadgets2.setup.Registration;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateEncodedPattern {
    public static ItemStack FromBuildList(ArrayList<StatePos> buildList, String templateName) {
        Item encodedPatternItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("ae2", "processing_pattern"));
        if (encodedPatternItem == null) return ItemStack.EMPTY;
        GenericStack[] inputList = buildList.stream()
                .filter(sp -> !(sp.state.getBlock() instanceof CableBusBlock))
                .map(sp -> sp.state.getFluidState().isSource()
                        ? GenericStack.fromFluidStack(new FluidStack(sp.state.getFluidState().getType(), 1000))
                        : GenericStack.fromItemStack(new ItemStack(sp.state.getBlock())))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        GenericStack::what,
                        Collectors.summingLong(GenericStack::amount)
                ))
                .entrySet().stream()
                .map(entry -> new GenericStack(entry.getKey(), entry.getValue()))
                .toArray(GenericStack[]::new);
        ItemStack barrierStack = new ItemStack(Registration.Template.get());
        if (!(templateName == null || templateName.trim().isEmpty())) {
            barrierStack.setHoverName(Component.literal(templateName));
        }
        GenericStack[] outputList = new GenericStack[]{GenericStack.fromItemStack(barrierStack)};
        return AEItems.PROCESSING_PATTERN.asItem().encode(inputList, outputList);
    }
}
