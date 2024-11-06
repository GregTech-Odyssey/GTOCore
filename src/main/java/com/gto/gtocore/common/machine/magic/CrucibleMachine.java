package com.gto.gtocore.common.machine.magic;

import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.utils.Registries;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMachines;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrucibleMachine extends SimpleNoEnergyMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CrucibleMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private ItemStack output;

    public CrucibleMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, GTMachines.defaultTankSizeFunction, args);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        Level level = getLevel();
        if (recipe == null || level == null) return false;
        boolean successful = false;
        int input_count = recipe.data.getInt("input_count");
        ItemStack input = Registries.getItemStack(recipe.data.getString("input"), input_count);
        output = Registries.getItemStack(recipe.data.getString("output"), recipe.data.getInt("output_count"));
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(
                getPos().getX(),
                getPos().getY() + 1,
                getPos().getZ(),
                getPos().getX(),
                getPos().getY(),
                getPos().getZ()));
        for (ItemEntity entity : entities) {
            if (entity.getItem().getItem() == input.getItem()) {
                int entity_count = entity.getItem().getCount();
                if (entity_count > input_count) {
                    entity.setItem(input.copyWithCount(entity_count - input_count));
                    successful = true;
                } else if (entity_count == input_count) {
                    entity.kill();
                    successful = true;
                }
            }
        }
        return successful && super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        Level level = getLevel();
        if (level == null) return;
        level.addFreshEntity(new ItemEntity(level, getPos().getX(), getPos().getY() + 1, getPos().getZ(), output));
    }
}
