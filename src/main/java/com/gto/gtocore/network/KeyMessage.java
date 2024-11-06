package com.gto.gtocore.network;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyMessage {

    private final int type;
    private final int pressedMillis;

    public KeyMessage(int type, int pressedMillis) {
        this.type = type;
        this.pressedMillis = pressedMillis;
    }

    public KeyMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt());
    }

    public static void buffer(KeyMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedMillis);
    }

    public static void handler(KeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player sender = context.getSender();
            if (sender != null) {
                pressAction(sender, message.type);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        Level level = player.level();
        if (!level.hasChunkAt(player.blockPosition())) {
            return;
        }
        switch (type) {
            case 0:
                handleFlightSpeed(player);
                break;
            case 1:
                toggleNightVision(player);
                break;
            case 2:
                handlePearlCount(player);
                break;
            case 3:
                upgradeToolSpeed(player);
                break;
        }
    }

    private static void handleFlightSpeed(Player player) {
        float speed;
        String armorSlots = player.getArmorSlots().toString();
        if (armorSlots.contains("magneto_hydro_dynamically_con_strained_star_matter_")) {
            speed = 0.4F;
        } else if (armorSlots.contains("fermium_")) {
            speed = 0.2F;
        } else if (armorSlots.contains("infinity_")) {
            speed = 0.3F;
        } else {
            return;
        }
        CompoundTag data = player.getPersistentData();
        int speedFactor = data.getInt("fly_speed");
        if (player.isShiftKeyDown()) {
            player.getAbilities().setFlyingSpeed(0.05F);
            player.displayClientMessage(Component.translatable("gtocore.fly_speed_reset"), true);
            data.putInt("fly_speed", 1);
        } else {
            float currentSpeed = player.getAbilities().getFlyingSpeed();
            if (currentSpeed < speed) {
                player.getAbilities().setFlyingSpeed(0.05F * speedFactor);
                data.putInt("fly_speed", speedFactor + 1);
                player.displayClientMessage(Component.translatable("gtocore.fly_speed", (speedFactor + 1)), true);
            } else {
                player.displayClientMessage(Component.translatable("gtocore.reach_limit"), true);
            }
        }
    }

    private static void toggleNightVision(Player player) {
        String armorSlots = player.getArmorSlots().toString();
        if (Objects.equals(armorSlots, "[1 fermium_boots, 1 fermium_leggings, 1 fermium_chestplate, 1 fermium_helmet]") ||
                Objects.equals(armorSlots, "[1 magneto_hydro_dynamically_con_strained_star_matter_boots, 1 magneto_hydro_dynamically_con_strained_star_matter_leggings, 1 magneto_hydro_dynamically_con_strained_star_matter_chestplate, 1 magneto_hydro_dynamically_con_strained_star_matter_helmet]") ||
                Objects.equals(armorSlots, "[1 space_fermium_boots, 1 space_fermium_leggings, 1 space_fermium_chestplate, 1 space_fermium_helmet]")) {
            CompoundTag data = player.getPersistentData();
            boolean nightVisionEnabled = data.getBoolean("night_vision");
            data.putBoolean("night_vision", !nightVisionEnabled);

            if (nightVisionEnabled) {
                player.removeEffect(MobEffects.NIGHT_VISION);
                player.displayClientMessage(Component.translatable("metaarmor.message.nightvision.disabled"), true);
            } else {
                player.displayClientMessage(Component.translatable("metaarmor.message.nightvision.enabled"), true);
            }
        }
    }

    private static void handlePearlCount(Player player) {
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == GTOItems.WARPED_ENDER_PEAL.asItem()) {
            CompoundTag data = player.getPersistentData();
            int pearl = data.getInt("pearl_slot");
            if (player.isShiftKeyDown()) {
                pearl = (pearl == 0) ? 10 : pearl - 1;
            } else {
                pearl = (pearl == 10) ? 0 : pearl + 1;
            }
            data.putInt("pearl_slot", pearl);
            player.displayClientMessage(Component.translatable("monitor.gui.title.slot").append(String.valueOf(pearl)), true);
        }
    }

    private static void upgradeToolSpeed(Player player) {
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (Objects.equals(itemStack.getItem().kjs$getId(), "gtceu:echoite_vajra")) {
            int value = player.isShiftKeyDown() ? 10 : 1;
            float speed = itemStack.getTag().getCompound("GT.Tool").getFloat("ToolSpeed");
            float newSpeed = adjustToolSpeed(speed, value);
            itemStack.getTag().getCompound("GT.Tool").putFloat("ToolSpeed", newSpeed);
            player.displayClientMessage(Component.translatable("jade.horseStat.speed", newSpeed), true);
        }
    }

    private static float adjustToolSpeed(float speed, int value) {
        if (speed < 100) {
            return speed + value;
        } else if (speed < 1000) {
            return speed + value * 10;
        } else {
            return 10;
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        GTOCore.addNetworkMessage(KeyMessage.class, KeyMessage::buffer, KeyMessage::new, KeyMessage::handler);
    }
}
