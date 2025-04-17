package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gto.gtocore.api.gui.ParallelConfigurator;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.integration.ae2.pattern.ParallelAECraftingPattern;
import com.gto.gtocore.integration.ae2.pattern.ParallelAESmithingTablePattern;
import com.gto.gtocore.integration.ae2.pattern.ParallelAEStonecuttingPattern;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.blockentity.crafting.IMolecularAssemblerSupportedPattern;
import appeng.crafting.pattern.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

@Setter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MECraftPatternPartMachine extends MEPatternPartMachine<MECraftPatternPartMachine.InternalSlot> implements IParallelMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MECraftPatternPartMachine.class, MEPatternPartMachine.MANAGED_FIELD_HOLDER);

    private Runnable onContentsChanged = () -> {};

    @Persisted
    private int parallelNumber = 1;

    public MECraftPatternPartMachine(IMachineBlockEntity holder) {
        super(holder, 72);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    InternalSlot[] createInternalSlotArray() {
        return new InternalSlot[72];
    }

    @Override
    boolean patternFilter(ItemStack stack) {
        return stack.getItem() instanceof EncodedPatternItem && !(stack.getItem() instanceof ProcessingPatternItem);
    }

    @Override
    InternalSlot createInternalSlot() {
        return new InternalSlot(this);
    }

    @Nullable
    IPatternDetails decodePattern(ItemStack stack) {
        return toParallelPattern(super.decodePattern(stack), 1);
    }

    void updatePatterns() {
        patterns = detailsSlotMap.keySet().stream().map(p -> toParallelPattern(p, getParallel())).filter(Objects::nonNull).toList();
        needPatternSync = true;
    }

    private @Nullable IPatternDetails toParallelPattern(@Nullable IPatternDetails pattern, int parallel) {
        if (pattern instanceof AECraftingPattern craftingPattern) {
            return new ParallelAECraftingPattern(craftingPattern.getDefinition(), getLevel(), parallel);
        } else if (pattern instanceof AEStonecuttingPattern stonecuttingPattern) {
            return new ParallelAEStonecuttingPattern(stonecuttingPattern.getDefinition(), getLevel(), parallel);
        } else if (pattern instanceof AESmithingTablePattern smithingTablePattern) {
            return new ParallelAESmithingTablePattern(smithingTablePattern.getDefinition(), getLevel(), parallel);
        }
        return null;
    }

    @Override
    public int getMaxParallel() {
        return 1000000;
    }

    @Override
    public int getParallel() {
        if (parallelNumber == 0) parallelNumber = getMaxParallel();
        return Math.max(1, parallelNumber);
    }

    @Override
    public void setParallel(int number) {
        int parallel = Mth.clamp(number, 1, getMaxParallel());
        if (parallel != parallelNumber) {
            parallelNumber = parallel;
            updatePatterns();
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new ParallelConfigurator(this));
    }

    public static class InternalSlot extends AbstractInternalSlot {

        @Getter
        private ItemStack output;
        @Getter
        @Setter
        private long amount;

        private final MECraftPatternPartMachine machine;

        private InternalSlot(MECraftPatternPartMachine machine) {
            this.machine = machine;
        }

        @Override
        public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder, Predicate<ItemStack> itemCallback) {
            if (patternDetails instanceof IMolecularAssemblerSupportedPattern pattern && pattern.getOutputs().length == 1 && pattern.getOutputs()[0].what() instanceof AEItemKey itemKey) {
                if (output == null) output = itemKey.toStack();
                amount += pattern.getOutputs()[0].amount();
                machine.onContentsChanged.run();
                return true;
            }
            return false;
        }

        @Override
        void refund() {
            output = null;
            amount = 0;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
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
    }
}
