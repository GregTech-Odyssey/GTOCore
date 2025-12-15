package com.gtocore.mixin.ae2.wtlib;

import com.gtocore.client.Message;
import com.gtocore.config.GTOConfig;

import com.gtolib.api.network.NetworkPack;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import net.minecraft.server.level.ServerPlayer;

import appeng.api.config.Actionable;
import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.crafting.CraftingLink;
import appeng.me.helpers.PlayerSource;
import com.llamalad7.mixinextras.sugar.Local;
import de.mari_023.ae2wtlib.AE2wtlibEvents;
import de.mari_023.ae2wtlib.wct.CraftingTerminalHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Mixin(AE2wtlibEvents.class)
public class AE2wtlibEventsMixin {

    @Unique
    private static final NetworkPack PICK_CRAFT_TOAST = NetworkPack.registerS2C(GTCEu.isClientSide() ? (p, b) -> Message.pickCraftToast(b) : GTOUtils.NOOP_BI_CONSUMER);

    @Redirect(method = "pickBlock(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;)V",
              at = @At(value = "INVOKE", target = "Lappeng/api/storage/MEStorage;extract(Lappeng/api/stacks/AEKey;JLappeng/api/config/Actionable;Lappeng/api/networking/security/IActionSource;)J", remap = false),
              remap = false)
    private static long redirectExtractMEStorage(MEStorage instance, AEKey what, long amount, Actionable mode, IActionSource source,
                                                 @Local(argsOnly = true) ServerPlayer player,
                                                 @Local CraftingTerminalHandler cTHandler, @Local PlayerSource playerSource) {
        if (!GTOConfig.INSTANCE.pickCraft) return instance.extract(what, amount, mode, source);
        switch (mode) {
            case SIMULATE -> {
                var amountExtractable = instance.extract(what, amount, Actionable.SIMULATE, source);
                if (amountExtractable == 0) {
                    final ICraftingService craftingService = cTHandler.getTargetGrid().getCraftingService();
                    final long amountNeeded = what instanceof AEItemKey itemKey &&
                            itemKey.toStack().getItem() instanceof MetaMachineItem mmItem &&
                            mmItem.getDefinition() instanceof MultiblockMachineDefinition ?
                                    1 : amount;
                    boolean isCraftable = craftingService.isCraftable(what);
                    if (isCraftable) {
                        Set<CraftingLink> playerLinks = IEnhancedPlayer.of(player).getPlayerData().craftingLinks;
                        if (playerLinks.size() >= GTOConfig.INSTANCE.pickCraftMaxTasks) {
                            gto$packet(player, what, 3);
                            return 0;
                        }
                        CompletableFuture.supplyAsync(() -> {
                            Future<ICraftingPlan> task = craftingService.beginCraftingCalculation(
                                    player.level(),
                                    () -> playerSource,
                                    what,
                                    amountNeeded,
                                    CalculationStrategy.REPORT_MISSING_ITEMS);
                            try {
                                return task.get();
                            } catch (Exception e) {
                                return null;
                            }
                        }).thenAcceptAsync(plan -> {
                            if (plan != null && !plan.simulation()) {
                                var job = craftingService.submitJob(plan, IEnhancedPlayer.of(player), null, true, playerSource);
                                if (job == null || !job.successful()) {
                                    gto$packet(player, what, 2);
                                    return;
                                }
                                playerLinks.add((CraftingLink) job.link());
                                gto$packet(player, what, 0);
                            } else {
                                gto$packet(player, what, plan == null ? 1 : 2);
                            }
                        });
                    }
                }
                return amountExtractable;
            }
            case MODULATE -> {
                return instance.extract(what, amount, Actionable.MODULATE, source);
            }
            default -> {
                return 0;
            }
        }
    }

    @Unique
    private static void gto$packet(ServerPlayer player, AEKey what, int code) {
        PICK_CRAFT_TOAST.send(
                buf -> {
                    AEKey.writeKey(buf, what);
                    buf.writeInt(code);
                }, player);
    }
}
