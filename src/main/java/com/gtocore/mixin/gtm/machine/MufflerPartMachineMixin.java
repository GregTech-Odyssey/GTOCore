package com.gtocore.mixin.gtm.machine;

import com.gtocore.api.machine.IGTOMufflerMachine;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.machine.feature.IAirScrubberInteractor;
import com.gtolib.api.machine.feature.IDroneInteractionMachine;
import com.gtolib.api.machine.multiblock.DroneControlCenterMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.misc.Drone;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MufflerPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import committee.nova.mods.avaritia.init.registry.ModItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MufflerPartMachine.class)
public abstract class MufflerPartMachineMixin extends TieredPartMachine implements IGTOMufflerMachine, IDroneInteractionMachine, IAirScrubberInteractor {

    @Shadow(remap = false)
    @Final
    private CustomItemStackHandler inventory;

    @Unique
    private DroneControlCenterMachine gtolib$cache;

    @Unique
    private AirScrubberMachine gtolib$airScrubberCache;

    @Unique
    private int gtolib$count;
    @Unique
    @Persisted
    @DescSynced
    private int gto$chanceOfNotProduceAsh = 100;

    protected MufflerPartMachineMixin(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Unique
    @SuppressWarnings("all")
    public AirScrubberMachine getAirScrubberMachineCache() {
        return gtolib$airScrubberCache;
    }

    @Unique
    @SuppressWarnings("all")
    public void setAirScrubberMachineCache(AirScrubberMachine cache) {
        gtolib$airScrubberCache = cache;
    }

    @Unique
    @SuppressWarnings("all")
    public DroneControlCenterMachine getNetMachineCache() {
        return gtolib$cache;
    }

    @Unique
    @SuppressWarnings("all")
    public void setNetMachineCache(DroneControlCenterMachine cache) {
        gtolib$cache = cache;
    }

    @Unique
    private TickableSubscription gtolib$tickSubs;

    @Unique
    private boolean gtolib$isFrontFaceFree;
    @Unique
    private boolean gtolib$isAshFull;
    @Unique
    @Persisted
    private @Nullable ItemStack gtocore$lastAsh;


    @Unique
    private void gtolib$tick() {
        if (getOffsetTimer() % 40 == 0) {
            DroneControlCenterMachine centerMachine = getNetMachine();
            if (centerMachine != null && !inventory.stacks[inventory.size - 3].isEmpty()) {
                Drone drone = null;
                boolean available = false;
                for (int i = 0; i < inventory.size; i++) {
                    ItemStack stack = inventory.stacks[i];
                    if (stack.getCount() > 1) {
                        if (drone == null) {
                            var eu = inventory.size << 4;
                            drone = getFirstUsableDrone(d -> d.getCharge() >= eu);
                            if (drone != null) {
                                available = drone.start(4, eu, GTOValues.REMOVING_ASH);
                            }
                        }
                        if (available) {
                            inventory.setStackInSlot(i, ItemStack.EMPTY);
                            MachineUtils.outputItem(centerMachine, stack);
                        } else break;
                    }
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        gto$chanceOfNotProduceAsh = Math.min(Math.max(gto$chanceOfNotProduceAsh, 0), getTier() * 10);
        if (!isRemote()) {
            gtolib$tickSubs = subscribeServerTick(gtolib$tickSubs, this::gtolib$tick);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (gtolib$tickSubs != null) {
            gtolib$tickSubs.unsubscribe();
            gtolib$tickSubs = null;
        }
        gtolib$airScrubberCache = null;
        removeNetMachineCache();
    }

    @Override
    public boolean isFrontFaceFree() {
        if (!beforeWorking(null)) return false;
        if (!gtolib$isFrontFaceFree || self().getOffsetTimer() % 20 == 0) {
            gtolib$isFrontFaceFree = true;
            BlockPos pos = self().getPos();
            for (int i = 0; i < 3; i++) {
                pos = pos.relative(this.self().getFrontFacing());
                if (!self().getLevel().getBlockState(pos).isAir()) gtolib$isFrontFaceFree = false;
            }
        }
        return gtolib$isFrontFaceFree;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        return true;
    }

    @Unique
    public boolean gto$checkAshFull() {
        gtolib$isAshFull = false;
        var stack = inventory.getStackInSlot(inventory.getSlots() - 1);
        if (stack.getCount() > 63 || (!stack.isEmpty() && gtocore$lastAsh != null && !stack.is(gtocore$lastAsh.getItem()))) {
            gtolib$isAshFull = true;
            return true;
        }
        return false;
    }

    @Override
    public void recoverItemsTable(ItemStack recoveryItems) {
        AirScrubberMachine machine = getAirScrubberMachine();
        if (machine != null && GTValues.RNG.nextInt(machine.getTier() << 1 + 1) > 1) {
            MachineUtils.outputItem(machine, recoveryItems);
            return;
        }
        CustomItemStackHandler.insertItemStackedFast(inventory, recoveryItems);
        gtocore$lastAsh=recoveryItems;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(MetaMachineBlockEntity holder, int tier, CallbackInfo ci) {
        inventory.setOnContentsChanged(() -> {
            for (var controller : getControllers()) {
                if (controller instanceof IRecipeLogicMachine recipeLogicMachine) {
                    recipeLogicMachine.getRecipeLogic().updateTickSubscription();
                }
            }
        });
    }

    @Inject(method = "createUI", at = @At("RETURN"), remap = false, cancellable = true)
    private void gtolib$createUI(Player entityPlayer, CallbackInfoReturnable<ModularUI> cir) {
        ConfiguratorPanel configuratorPanel;
        var originUI = cir.getReturnValue();
        cir.setReturnValue(originUI.widget(configuratorPanel = new ConfiguratorPanel(-(24 + 2), originUI.getHeight())));
        attachConfigurators(configuratorPanel);
        configuratorPanel.setSelfPosition(new Position(-24 - 2, originUI.getHeight() - configuratorPanel.getSize().height - 4));
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget widget) {
        return super.createMainPage(widget);
    }

}
