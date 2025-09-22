package com.gtocore.mixin.gtm.machine;

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
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
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
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
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
public abstract class MufflerPartMachineMixin extends TieredPartMachine implements IMufflerMachine, IDroneInteractionMachine, IAirScrubberInteractor {

    @Shadow(remap = false)
    @SuppressWarnings("all")
    protected abstract boolean calculateChance();

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
    private static ItemStack gtolib$ASH;
    @Unique
    private static ItemStack gtolib$NeutronPile;
    @Unique
    @Persisted
    private @Nullable ItemStack gtocore$lastAsh;

    @Unique
    private boolean gtolib$invalid() {
        return GTOConfig.INSTANCE.disableMufflerPart || getTier() > GTValues.UHV;
    }

    @Unique
    private void gtolib$tick() {
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
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        gto$chanceOfNotProduceAsh = Math.min(Math.max(gto$chanceOfNotProduceAsh, 0), getTier() * 10);
        if (gtolib$invalid()) return;
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
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        if (gtolib$invalid()) return recipe;
        if (!isFrontFaceFree()) {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_OBSTRUCTED.reason());
                }
            }
            return null;
        }
        return recipe;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        return true;
    }

    @Override
    public boolean onWorking(IWorkableMultiController controller) {
        if (gtolib$invalid()) return true;
        gtolib$count++;
        if (gtolib$isAshFull) return false;
        if (gtolib$count % 80 == 0) {
            gtolib$count = 1;
            List<LivingEntity> entities = self().getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(self().getPos().relative(this.self().getFrontFacing())));
            entities.forEach(e -> {
                e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 2));
                e.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 1));
            });
            if (!calculateChance()) {
                gtolib$insertAsh(controller);
            }
        }
        return true;
    }

    @Override
    public boolean beforeWorking(IWorkableMultiController controller) {
        if (gtolib$invalid()) return true;
        gtolib$isAshFull = false;
        if (gto$checkAshFull()) {
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine recipeLogicMachine && recipeLogicMachine.getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(IdleReason.MUFFLER_OBSTRUCTED.reason());
                }
            }
            return false;
        }
        return true;
    }

    @Unique
    private boolean gto$checkAshFull() {
        var stack = inventory.getStackInSlot(inventory.getSlots() - 1);
        if (stack.getCount() > 63 || (!stack.isEmpty() && gtocore$lastAsh != null && !stack.is(gtocore$lastAsh.getItem()))) {
            gtolib$isAshFull = true;
            return true;
        }
        return false;
    }

    @Unique
    private void gtolib$insertAsh(IWorkableMultiController controller) {
        if (GTOCore.isNormal() && GTValues.RNG.nextBoolean()) return;
        AirScrubberMachine machine = getAirScrubberMachine();
        if (machine != null && GTValues.RNG.nextInt(100) < gto$chanceOfNotProduceAsh) return;

        if (gtolib$ASH == null) {
            gtolib$ASH = ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Ash);
        }
        ItemStack ash = gtolib$ASH;
        GTRecipe lastRecipe = controller.getRecipeLogic().getLastRecipe();
        if (lastRecipe != null && lastRecipe.getInputEUt() >= GTValues.V[GTValues.UV] && GTValues.RNG.nextFloat() < 1e-3f) {
            if (gtolib$NeutronPile == null) {
                gtolib$NeutronPile = ModItems.neutron_pile.get().getDefaultInstance();
            }
            ash = gtolib$NeutronPile;
        } else {
            MultiblockMachineBuilder.MufflerProductionGenerator supplier = controller.self().getDefinition().getRecoveryItems();
            if (supplier != null) {
                ash = supplier.getMuffledProduction(controller.self(), lastRecipe);
            }
            gtocore$lastAsh = ash;
        }
        CustomItemStackHandler.insertItemStackedFast(inventory, ash);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(MetaMachineBlockEntity holder, int tier, CallbackInfo ci) {
        if (gtolib$invalid()) return;
        inventory.setOnContentsChanged(() -> {
            for (var controller : getControllers()) {
                if (controller instanceof IRecipeLogicMachine recipeLogicMachine) {
                    recipeLogicMachine.getRecipeLogic().updateTickSubscription();
                }
            }
        });
    }

    @Inject(method = "createUI", at = @At("HEAD"), remap = false, cancellable = true)
    private void gtolib$createUI(Player entityPlayer, CallbackInfoReturnable<ModularUI> cir) {
        int rowSize = Math.min((int) Math.sqrt(inventory.getSlots()), 9);
        int w = 184, h = 18 + 18 * rowSize + 94;
        var modular = new DraggableScrollableWidgetGroup(4, 4, w - 8, 104 - 4).setBackground(GuiTextures.BACKGROUND)
                .setXBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON)
                .setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON)
        // .setXScrollBarHeight(3)
        // .setYScrollBarWidth(3)
        // .addWidget(new LabelWidget(10, 5, getBlockState().getBlock().getDescriptionId()))
        // .addWidget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.SLOT, 7 + xOffset, 18 + 18
        // * rowSize + 12, true))
        ;
        for (int index = 0; index < inventory.getSlots(); index++) {
            int x = index % rowSize * 18;
            int y = index / rowSize * 18;
            modular.addWidget(new SlotWidget(inventory, index, (88 - rowSize * 9 + x), y + 20 - rowSize, true, false).setBackgroundTexture(GuiTextures.SLOT));
        }
        cir.setReturnValue(new ModularUI(w, h, this, entityPlayer)
                .background(GuiTextures.BACKGROUND)
                .widget(new FancyMachineUIWidget(this, w, h).addWidget(modular)));
    }

    @Override
    public Widget createMainPage(FancyMachineUIWidget widget) {
        return super.createMainPage(widget);
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfigurator() {

            @Override
            public Component getTitle() {
                return Component.translatable("gtocore.machine.muffler.config");
            }

            @Override
            public IGuiTexture getIcon() {
                return GTOGuiTextures.PARALLEL_CONFIG;
            }

            @Override
            public Widget createConfigurator() {
                return gtolib$configPanelWidget();
            }
        });
    }

    @Unique
    private Widget gtolib$configPanelWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 100, 20);
        var intInput = new IntInputWidget(() -> gto$chanceOfNotProduceAsh, p -> gto$chanceOfNotProduceAsh = p) {

            @Override
            public void writeInitialData(FriendlyByteBuf buffer) {
                super.writeInitialData(buffer);
                buffer.writeVarInt(getTier() * 10);
                setMax(getTier() * 10);
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public void readInitialData(FriendlyByteBuf buffer) {
                super.readInitialData(buffer);
                setMax(buffer.readVarInt());
            }

            @Override
            public List<Component> getTooltipTexts() {
                return super.getTooltipTexts();
            }
        };
        intInput.setMin(0);
        group.addWidget(intInput.setHoverTooltips(Component.translatable("gtocore.machine.muffler.config.desc", gto$chanceOfNotProduceAsh)));
        return group;
    }
}
