package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.feature.IAirScrubberInteractor;
import com.gto.gtocore.api.machine.feature.IDroneInteractionMachine;
import com.gto.gtocore.api.misc.Drone;
import com.gto.gtocore.common.machine.multiblock.noenergy.DroneControlCenterMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MufflerPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemHandlerHelper;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MufflerPartMachine.class)
public abstract class MufflerPartMachineMixin extends TieredPartMachine implements IMufflerMachine, IDroneInteractionMachine, IAirScrubberInteractor {

    @Shadow(remap = false)
    @SuppressWarnings("all")
    protected abstract boolean calculateChance();

    @Shadow(remap = false)
    @Final
    private CustomItemStackHandler inventory;

    @Unique
    private DroneControlCenterMachine gtocore$cache;

    @Unique
    private AirScrubberMachine gtocore$airScrubberCache;

    protected MufflerPartMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Unique
    @SuppressWarnings("all")
    public AirScrubberMachine getAirScrubberMachineCache() {
        return gtocore$airScrubberCache;
    }

    @Unique
    @SuppressWarnings("all")
    public void setAirScrubberMachineCache(AirScrubberMachine cache) {
        gtocore$airScrubberCache = cache;
    }

    @Unique
    @SuppressWarnings("all")
    public DroneControlCenterMachine getNetMachineCache() {
        return gtocore$cache;
    }

    @Unique
    @SuppressWarnings("all")
    public void setNetMachineCache(DroneControlCenterMachine cache) {
        gtocore$cache = cache;
    }

    @Unique
    private TickableSubscription gtocore$tickSubs;

    @Unique
    private boolean gtocore$isFrontFaceFree;
    @Unique
    private boolean gtocore$isAshFull;

    @Unique
    private static ItemStack gtocore$ASH;

    @Unique
    private void gtocore$tick() {
        if (getOffsetTimer() % 40 == 0) {
            DroneControlCenterMachine centerMachine = getNetMachine();
            if (centerMachine != null) {
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (stack.getCount() > 32) {
                        Drone drone = getFirstUsableDrone();
                        if (drone != null && drone.start(4, stack.getCount() << 2, GTOValues.REMOVING_ASH)) {
                            inventory.setStackInSlot(i, ItemStack.EMPTY);
                            MachineUtils.outputItem(centerMachine, stack);
                        }
                    }
                }
            }
            if (!isFrontFaceFree()) {
                for (var c : getControllers()) {
                    if (c instanceof IRecipeLogicMachine machine) {
                        machine.getRecipeLogic().markLastRecipeDirty();
                    }
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            gtocore$tickSubs = subscribeServerTick(gtocore$tickSubs, this::gtocore$tick);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (gtocore$tickSubs != null) {
            gtocore$tickSubs.unsubscribe();
            gtocore$tickSubs = null;
        }
        gtocore$airScrubberCache = null;
        removeNetMachineCache();
    }

    @Override
    public boolean isFrontFaceFree() {
        if (!beforeWorking(null)) return false;
        if (!gtocore$isFrontFaceFree || self().getOffsetTimer() % 20 == 0) {
            gtocore$isFrontFaceFree = true;
            BlockPos pos = self().getPos();
            for (int i = 0; i < 3; i++) {
                pos = pos.relative(this.self().getFrontFacing());
                if (!self().getLevel().getBlockState(pos).isAir()) gtocore$isFrontFaceFree = false;
            }
        }
        return gtocore$isFrontFaceFree;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        if (!gtocore$isAshFull && !calculateChance()) gtocore$insertAsh();
        return true;
    }

    @Override
    public boolean onWorking(IWorkableMultiController controller) {
        if (gtocore$isAshFull) return false;
        if (self().getOffsetTimer() % 80 == 0) {
            List<LivingEntity> entities = self().getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(self().getPos().relative(this.self().getFrontFacing())));
            entities.forEach(e -> {
                e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 2));
                e.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 1));
            });
            if (!calculateChance()) gtocore$insertAsh();

        }
        return true;
    }

    @Override
    public boolean beforeWorking(IWorkableMultiController controller) {
        gtocore$isAshFull = false;
        if (inventory.getStackInSlot(inventory.getSlots() - 1).getCount() > 63) {
            gtocore$isAshFull = true;
            return false;
        }
        return true;
    }

    @Unique
    private void gtocore$insertAsh() {
        AirScrubberMachine machine = getAirScrubberMachine();
        if (machine != null && GTValues.RNG.nextInt(machine.getTier() << 1) > 1) return;
        if (gtocore$ASH == null) {
            gtocore$ASH = ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Ash);
        }
        ItemHandlerHelper.insertItemStacked(inventory, gtocore$ASH.copy(), false);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(IMachineBlockEntity holder, int tier, CallbackInfo ci) {
        inventory.setOnContentsChanged(() -> {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine) {
                    recipeLogicMachine.getRecipeLogic().updateTickSubscription();
                }
            }
        });
    }
}
