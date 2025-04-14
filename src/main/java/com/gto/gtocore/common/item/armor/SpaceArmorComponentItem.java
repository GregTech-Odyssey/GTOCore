package com.gto.gtocore.common.item.armor;

import com.gregtechceu.gtceu.api.item.armor.ArmorComponentItem;
import com.gregtechceu.gtceu.api.item.armor.IArmorLogic;
import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import earth.terrarium.adastra.api.systems.OxygenApi;
import earth.terrarium.adastra.common.registry.ModFluids;
import earth.terrarium.adastra.common.tags.ModFluidTags;
import earth.terrarium.adastra.common.utils.FluidUtils;
import earth.terrarium.adastra.common.utils.TooltipUtils;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.ClientFluidHooks;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class SpaceArmorComponentItem extends ArmorComponentItem implements BotariumFluidItem<WrappedItemFluidContainer> {

    private final long tankSize;

    public SpaceArmorComponentItem(ArmorMaterial material, Type type, long size, Properties properties) {
        super(material, type, properties);
        this.tankSize = size;
    }

    @Override
    public void attachComponents(IItemComponent... components) {
        super.attachComponents(components);

        IDurabilityBar durabilityBar = new IDurabilityBar() {

            @Override
            public int getBarColor(ItemStack stack) {
                return ClientFluidHooks.getFluidColor(FluidUtils.getTank(stack));
            }

            @Override
            public int getBarWidth(ItemStack stack) {
                var fluidContainer = getFluidContainer(stack);
                return (int) (((double) fluidContainer.getFirstFluid().getFluidAmount() /
                        fluidContainer.getTankCapacity(0)) * 13);
            }

            @Override
            public boolean isBarVisible(ItemStack stack) {
                return FluidUtils.hasFluid(stack);
            }

            @Override
            public boolean showEmptyBar(ItemStack itemStack) {
                return false;
            }
        };

        this.components.add(durabilityBar);
        durabilityBar.onAttached(this);
    }

    @Override
    public SpaceArmorComponentItem setArmorLogic(IArmorLogic armorLogic) {
        return (SpaceArmorComponentItem) super.setArmorLogic(armorLogic);
    }

    @Override
    public WrappedItemFluidContainer getFluidContainer(ItemStack holder) {
        return new WrappedItemFluidContainer(holder, new SimpleFluidContainer(FluidConstants.fromMillibuckets(tankSize), 1, (t, f) -> f.is(ModFluidTags.OXYGEN)));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        if (level.isClientSide) return;
        if (player.isCreative() || player.isSpectator()) return;
        player.setTicksFrozen(0);
        if (player.tickCount % 20 == 0 && SpaceArmorComponentItem.hasOxygen(player)) {
            if (!OxygenApi.API.hasOxygen(player)) SpaceArmorComponentItem.consumeOxygen(stack);
            if (player.isEyeInFluidType(ForgeMod.WATER_TYPE.get())) {
                SpaceArmorComponentItem.consumeOxygen(stack);
                player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + 4 * 10));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(TooltipUtils.getFluidComponent(FluidUtils.getTank(stack), FluidConstants.fromMillibuckets(getFluidContainer(stack).getTankCapacity(0)), ModFluids.OXYGEN.get()));
    }

    public static long getOxygenAmount(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return 0;
        var stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (!(stack.getItem() instanceof SpaceArmorComponentItem suit)) return 0;
        return suit.getFluidContainer(stack).getFirstFluid().getFluidAmount();
    }

    public static boolean hasOxygen(Entity entity) {
        return getOxygenAmount(entity) > FluidConstants.fromMillibuckets(1);
    }

    private static void consumeOxygen(ItemStack stack) {
        ItemStackHolder holder = new ItemStackHolder(stack);
        var container = FluidContainer.of(holder);
        if (container == null) return;
        FluidHolder extracted = container.extractFluid(container.getFirstFluid().copyWithAmount(FluidConstants.fromMillibuckets(2)), false);
        if (holder.isDirty() || extracted.getFluidAmount() > 0) stack.setTag(holder.getStack().getTag());
    }
}
