package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTMaterials;
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

import java.util.List;

@Mixin(MufflerPartMachine.class)
public abstract class MufflerPartMachineMixin implements IMufflerMachine {

    @Shadow(remap = false)
    protected abstract boolean calculateChance();

    @Shadow(remap = false)
    @Final
    private CustomItemStackHandler inventory;

    @Unique
    private boolean gtocore$isFrontFaceFree;
    @Unique
    private boolean gtocore$isAshFull;

    @Unique
    private static ItemStack gtocore$ASH;

    @Override
    public boolean isFrontFaceFree() {
        if (gtocore$isAshFull) return false;
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
        if (gtocore$ASH == null) {
            gtocore$ASH = ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Ash);
        }
        ItemHandlerHelper.insertItemStacked(inventory, gtocore$ASH.copy(), false);
    }
}
