package com.gtocore.integration.mythic_botany;

import com.gtolib.api.player.IEnhancedPlayer;

import com.gregtechceu.gtceu.utils.memoization.GTMemoizer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import mythicbotany.MythicPlayerData;
import mythicbotany.register.ModBlocks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import vazkii.botania.common.item.BotaniaItems;

import java.util.Set;
import java.util.function.Supplier;

public class AlfheimHelper {

    public static final Supplier<Set<Item>> REQUIRED_ITEMS = GTMemoizer.memoize(() -> Set.of(
            BotaniaItems.kingKey,
            BotaniaItems.flugelEye,
            BotaniaItems.infiniteFruit,
            BotaniaItems.thorRing,
            BotaniaItems.odinRing,
            BotaniaItems.lokiRing,
            ModBlocks.mjoellnir.asItem(),
            ExtraBotanyItems.excalibur,
            ExtraBotanyItems.failnaught,
            ExtraBotanyItems.rheinHammer,
            ExtraBotanyItems.achillesShield,
            ExtraBotanyItems.voidArchives));

    public static boolean gtocore$canPlayerUsePortal(ServerPlayer player) {
        boolean hasKnowledge = MythicPlayerData.getData(player).getBoolean("KvasirKnowledge");
        boolean passesAdditionalChecks = AlfheimHelper.gtocore$additionalChecks(player);
        return hasKnowledge && passesAdditionalChecks;
    }

    public static boolean gtocore$additionalChecks(ServerPlayer player) {
        if (player.isCreative() || player.isSpectator()) return true;
        if (MythicPlayerData.getData(player).getBoolean("enterAlfheim")) return true;
        if (itemObtained(player).size() < REQUIRED_ITEMS.get().size()) return false;
        if (!MythicPlayerData.getData(player).getBoolean("enterAlfheim")) {
            MythicPlayerData.getData(player).putBoolean("enterAlfheim", true);
        }
        return true;
    }

    public static boolean clientCanPlayerUsePortal(Player player) {
        var data = IEnhancedPlayer.of(player).getPlayerData();
        return data.mythicBotKnowledgeState && data.mythicBotRelicState;
    }

    public static boolean clientPlayerHasKnowledge(Player player) {
        return IEnhancedPlayer.of(player).getPlayerData().mythicBotKnowledgeState;
    }

    public static Set<Item> itemObtained(Player player) {
        ReferenceSet<Item> items = getItems(player);
        items.retainAll(REQUIRED_ITEMS.get());
        return items;
    }

    private static ReferenceSet<Item> getItems(Player player) {
        ReferenceSet<Item> curiosItems = new ReferenceOpenHashSet<>(45);
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) {
                curiosItems.add(stack.getItem());
            }
        }
        curiosItems.add(player.getOffhandItem().getItem());
        LazyOptional<ICuriosItemHandler> curiosHandlerOpt = CuriosApi.getCuriosInventory(player);
        if (curiosHandlerOpt.isPresent()) {
            for (ICurioStacksHandler slotHandler : curiosHandlerOpt.resolve().get().getCurios().values()) {
                for (int slotIndex = 0; slotIndex < slotHandler.getSlots(); slotIndex++) {
                    ItemStack stack = slotHandler.getStacks().getStackInSlot(slotIndex);
                    if (!stack.isEmpty()) {
                        curiosItems.add(stack.getItem());
                    }
                }
            }
        }
        return curiosItems;
    }
}
