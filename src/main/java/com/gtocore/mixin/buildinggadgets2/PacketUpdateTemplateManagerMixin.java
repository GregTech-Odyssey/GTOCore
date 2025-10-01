package com.gtocore.mixin.buildinggadgets2;

import com.direwolf20.buildinggadgets2.common.containers.TemplateManagerContainer;
import com.direwolf20.buildinggadgets2.common.network.packets.PacketUpdateTemplateManager;
import com.direwolf20.buildinggadgets2.common.worlddata.BG2Data;
import com.direwolf20.buildinggadgets2.util.GadgetNBT;
import com.direwolf20.buildinggadgets2.util.MiscHelpers;
import com.direwolf20.buildinggadgets2.util.datatypes.StatePos;
import com.gtocore.integration.ae.CreateEncodedPattern;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Mixin(value = PacketUpdateTemplateManager.class, remap = false)
public abstract class PacketUpdateTemplateManagerMixin {

    @Unique
    private static final Item ENCODED_PATTERN = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("ae2", "processing_pattern"));
    @Unique
    private static final Item BLANK_PATTERN = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("ae2", "blank_pattern"));

    @Inject(
            method = "handle",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void handleAe2PatternPreemptively(
            PacketUpdateTemplateManager message,
            Supplier<NetworkEvent.Context> context,
            CallbackInfo ci
    ) {

        int mode = ((PacketUpdateTemplateManagerMixin) (Object) message).getMode();
        String templateName = ((PacketUpdateTemplateManagerMixin) (Object) message).getTemplateName();
        ServerPlayer sender = context.get().getSender();
        if (sender == null) return;

        AbstractContainerMenu container = sender.containerMenu;
        if (!(container instanceof TemplateManagerContainer)) return;
        if (mode != 0) return;

        ItemStack templateStack = container.getSlot(1).getItem();

        if (BLANK_PATTERN == null || (!templateStack.is(BLANK_PATTERN) && !templateStack.is(ENCODED_PATTERN))) return;
        ItemStack gadgetStack = container.getSlot(0).getItem();
        UUID sourceUUID = GadgetNBT.getUUID(gadgetStack);
        BG2Data bg2Data = BG2Data.get(Objects.requireNonNull(sender.getServer()).overworld());
        ArrayList<StatePos> buildList = bg2Data.getCopyPasteList(sourceUUID, false);

        if (buildList == null || buildList.isEmpty()) {
            MiscHelpers.playSound(sender, Holder.direct(SoundEvent.createVariableRangeEvent(SoundEvents.WAXED_SIGN_INTERACT_FAIL.getLocation())));
            ci.cancel();
            return;
        }

        ItemStack encodedPattern = CreateEncodedPattern.FromBuildList(buildList, templateName);
        container.setItem(1, container.getStateId(), encodedPattern);
        MiscHelpers.playSound(sender, Holder.direct(SoundEvent.createVariableRangeEvent(SoundEvents.ENCHANTMENT_TABLE_USE.getLocation())));
        ci.cancel();
    }

    @Accessor("mode")
    public abstract int getMode();

    @Accessor("templateName")
    public abstract String getTemplateName();
}
