package com.gtocore.mixin.gtm.machine;

import com.gtocore.api.machine.IGTOMufflerMachine;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.feature.IAirScrubberInteractor;
import com.gtolib.api.machine.feature.IDroneInteractionMachine;
import com.gtolib.api.machine.multiblock.IDroneControlCenterMachine;
import com.gtolib.api.misc.Drone;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.MufflerPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
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

    @Unique
    @Persisted
    boolean gtocore$isWorkingEnabled;
    @Shadow(remap = false)
    @Final
    private CustomItemStackHandler inventory;
    @Unique
    private IDroneControlCenterMachine gtolib$cache;
    @Unique
    private AirScrubberMachine gtolib$airScrubberCache;
    @Unique
    @Persisted
    @DescSynced
    private int gto$chanceOfNotProduceAsh = 100;
    @Unique
    private TickableSubscription gtolib$tickSubs;
    @Unique
    private boolean gtolib$isFrontFaceFree;
    @Unique
    @Persisted
    private @Nullable ItemStack gtocore$lastAsh;

    protected MufflerPartMachineMixin(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Unique
    public AirScrubberMachine getAirScrubberMachineCache() {
        return gtolib$airScrubberCache;
    }

    @Unique
    public void setAirScrubberMachineCache(AirScrubberMachine cache) {
        gtolib$airScrubberCache = cache;
    }

    @Unique
    public IDroneControlCenterMachine getNetMachineCache() {
        return gtolib$cache;
    }

    @Unique
    public void setNetMachineCache(IDroneControlCenterMachine cache) {
        gtolib$cache = cache;
    }

    @Unique
    private void gtolib$tick() {
        if (getOffsetTimer() % 40 == 0) {
            IDroneControlCenterMachine centerMachine = getNetMachine();
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
    public boolean isWorkingEnabled() {
        return gtocore$isWorkingEnabled;
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        gtocore$isWorkingEnabled = isWorkingAllowed;
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
    public boolean gtolib$checkAshFull() {
        var stack = inventory.getStackInSlot(inventory.getSlots() - 1);
        return stack.getCount() > 63 || (!stack.isEmpty() && gtocore$lastAsh != null && !stack.is(gtocore$lastAsh.getItem()));
    }

    @Override
    public int gtolib$getRecoveryChance() {
        return gto$chanceOfNotProduceAsh;
    }

    @Override
    public void recoverItemsTable(ItemStack recoveryItems) {
        AirScrubberMachine machine = getAirScrubberMachine();
        if (machine != null && GTValues.RNG.nextInt(machine.getTier() << 1 + 1) > 1) {
            MachineUtils.outputItem(machine, recoveryItems);
            return;
        }
        CustomItemStackHandler.insertItemStackedFast(inventory, recoveryItems);
        gtocore$lastAsh = recoveryItems;
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
        gtocore$isWorkingEnabled = true;
    }

    @Inject(method = "createUI", at = @At("RETURN"), remap = false, cancellable = true)
    private void gtolib$createUI(Player entityPlayer, CallbackInfoReturnable<ModularUI> cir) {
        ConfiguratorPanel configuratorPanel;
        var originUI = cir.getReturnValue();
        if (GTCEu.isDev()) {
            int rowSize = (int) Math.sqrt(inventory.getSlots());
            int xOffset = rowSize == 10 ? 9 : 0;
            var modular = new ModularUI(176 + xOffset * 2, 18 + 18 * rowSize + 94, this, entityPlayer).background(GuiTextures.BACKGROUND).widget(new LabelWidget(10, 5, getBlockState().getBlock().getDescriptionId())).widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7 + xOffset, 18 + 18 * rowSize + 12, true));
            for (int y = 0; y < rowSize; y++) {
                for (int x = 0; x < rowSize; x++) {
                    int index = y * rowSize + x;
                    modular.widget(new SlotWidget(inventory, index, (88 - rowSize * 9 + x * 18) + xOffset, 18 + y * 18, true, true).setBackgroundTexture(GuiTextures.SLOT));
                }
            }
            originUI = modular;
        }
        cir.setReturnValue(originUI.widget(configuratorPanel = new ConfiguratorPanel(-(24 + 2), originUI.getHeight())));
        attachConfigurators(configuratorPanel);
        configuratorPanel.setSelfPosition(new Position(-24 - 2, originUI.getHeight() - configuratorPanel.getSize().height - 4));
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget widget) {
        return super.createMainPage(widget);
    }

    @Override
    public void gtolib$addMufflerEffect() {
        List<LivingEntity> entities = self().getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(self().getPos().relative(this.self().getFrontFacing())));
        entities.forEach(e -> {
            e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 2));
            e.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 1));
        });
    }
}
