package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import appeng.crafting.pattern.EncodedPatternItem;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Setter;

import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

@Setter
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MECraftPatternPartMachine extends MEPatternPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MECraftPatternPartMachine.class, MEPatternPartMachine.MANAGED_FIELD_HOLDER);

    public Consumer<Integer> updateCallback;

    public MECraftPatternPartMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    boolean filter(ItemStack stack) {
        return !(stack.getItem() instanceof EncodedPatternItem);
    }

    @Override
    protected void onPatternChange(int index) {
        super.onPatternChange(index);
        if (updateCallback != null) {
            updateCallback.accept(index);
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
