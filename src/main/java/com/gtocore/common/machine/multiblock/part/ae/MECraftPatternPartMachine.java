package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.blockentity.crafting.IMolecularAssemblerSupportedPattern;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MECraftPatternPartMachine extends MEPatternPartMachineKt<MECraftPatternPartMachine.InternalSlot> {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MECraftPatternPartMachine.class, MEPatternPartMachineKt.Companion.getMANAGED_FIELD_HOLDER());
    private Runnable onContentsChanged = () -> {};

    public MECraftPatternPartMachine(IMachineBlockEntity holder) {
        super(holder, 72);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public InternalSlot[] createInternalSlotArray() {
        return new InternalSlot[72];
    }

    @Override
    public boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof EncodedPatternItem && !(stack.getItem() instanceof ProcessingPatternItem);
    }

    @Override
    public InternalSlot createInternalSlot(int i) {
        return new InternalSlot(this);
    }

    @Override
    public boolean canShared() {
        return false;
    }

    public static final class InternalSlot extends AbstractInternalSlot {

        private ItemStack output;
        private long amount;
        private final MECraftPatternPartMachine machine;

        private InternalSlot(MECraftPatternPartMachine machine) {
            this.machine = machine;
        }

        @Override
        public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
            if (patternDetails instanceof IMolecularAssemblerSupportedPattern pattern && pattern.getOutputs().length == 1 && pattern.getOutputs()[0].what() instanceof AEItemKey itemKey) {
                if (output == null) output = itemKey.toStack();
                amount += pattern.getOutputs()[0].amount();
                machine.onContentsChanged.run();
                return true;
            }
            return false;
        }

        @Override
        public void onPatternChange() {
            output = null;
            amount = 0;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = super.serializeNBT();
            if (output != null) {
                tag.put("output", output.serializeNBT());
                tag.putLong("amount", amount);
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            amount = nbt.getLong("amount");
            if (nbt.contains("output")) {
                output = ItemStack.of(nbt.getCompound("output"));
            }
        }

        @Override
        public void setOnContentsChanged(Runnable onContentChanged) {}

        @Override
        public Runnable getOnContentsChanged() {
            return machine.onContentsChanged;
        }

        public ItemStack getOutput() {
            return this.output;
        }

        public void setAmount(final long amount) {
            this.amount = amount;
        }

        public long getAmount() {
            return this.amount;
        }
    }

    public void setOnContentsChanged(final Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }
}
