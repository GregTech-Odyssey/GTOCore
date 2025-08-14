package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.StructurePattern;
import com.gtocore.client.renderer.StructureVBO;
import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MultiBlockFileReader;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtocore.common.block.BlockMap.GRAVITONFLOWMAP;
import static com.gtolib.api.GTOValues.GRAVITON_FLOW_TIER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GodForgeMachine extends NoEnergyMultiblockMachine implements ITierCasingMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            GodForgeMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    @Persisted
    public float color;
    private boolean isRemoved = false;
    public long rotation;
    public int timer;
    @DescSynced
    @Persisted
    public int tier;

    private final TierCasingTrait tierCasingTrait;

    public GodForgeMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GRAVITON_FLOW_TIER);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (this.isActive() || this.timer > this.rotation) {
            this.rotation++;
            this.timer = 20;
            if (!isRemoved) {
                if (removeBlockFromWorld()) {
                    isRemoved = true;
                }
            }
        } else {
            this.timer = 0;
            if (this.rotation > 0) {
                this.rotation = (this.rotation - 1) % 180;
            } else if (isRemoved) {
                if (addBlockToWorld()) {
                    isRemoved = false;
                }
            }
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        color = 1 - 0.1F * getCasingTier(GRAVITON_FLOW_TIER);
        tier = getCasingTier(GRAVITON_FLOW_TIER);
    }

    private BlockPos getRealPos(int x, int y, int z) {
        String[][] structure = StructurePattern.ringOne;
        BlockPos.MutableBlockPos pos = BlockPos.ZERO.offset(122 + structure.length / 2 - x, -structure[0].length / 2 + y, -structure[0][0].length() / 2 + z).mutable();
        switch (getFrontFacing()) {
            case EAST -> pos.set(-pos.getX(), pos.getY(), -pos.getZ());
            case NORTH -> pos.set(-pos.getZ(), pos.getY(), pos.getX());
            case SOUTH -> pos.set(pos.getZ(), pos.getY(), -pos.getX());
        }
        return pos.offset(this.getPos());
    }

    private boolean removeBlockFromWorld() {
        String[][] structure = StructurePattern.ringOne;
        for (int x = 0; x < structure.length; x++) {
            String[] plane = structure[x];
            for (int y = 0; y < plane.length; y++) {
                String row = plane[y];
                for (int z = 0; z < row.length(); z++) {
                    char letter = row.charAt(z);
                    if (letter == ' ') continue;
                    BlockPos realPos = getRealPos(x, y, z);
                    if (!getLevel().isLoaded(realPos)) return false;
                    int flags = Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE;
                    getLevel().setBlock(realPos, Blocks.AIR.defaultBlockState(), flags);
                }
            }
        }
        return true;
    }

    private boolean addBlockToWorld() {
        StructureVBO ringStructure = (new StructureVBO()).addMapping('B', GTOBlocks.SINGULARITY_REINFORCED_STELLAR_SHIELDING_CASING.get())
                .addMapping('C', GTOBlocks.CELESTIAL_MATTER_GUIDANCE_CASING.get())
                .addMapping('D', GTOBlocks.BOUNDLESS_GRAVITATIONALLY_SEVERED_STRUCTURE_CASING.get())
                .addMapping('E', GTOBlocks.TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING.get())
                .addMapping('F', GTOBlocks.STELLAR_ENERGY_SIPHON_CASING.get())
                .addMapping('1', GTOBlocks.REMOTE_GRAVITON_FLOW_MODULATOR.get())
                .addMapping('2', GTOBlocks.MEDIAL_GRAVITON_FLOW_MODULATOR.get())
                .addMapping('3', GTOBlocks.CENTRAL_GRAVITON_FLOW_MODULATOR.get())
                .addMapping('H', GTOBlocks.SPATIALLY_TRANSCENDENT_GRAVITATIONAL_LENS_BLOCK.get());

        String[][] structure = StructurePattern.ringOne;
        if (tier == 2) {
            structure = StructurePattern.ringTwo;
        } else if (tier == 3) {
            structure = StructurePattern.ringThree;
        }
        ringStructure.assignStructure(structure);

        for (int x = 0; x < structure.length; x++) {
            String[] plane = structure[x];
            for (int y = 0; y < plane.length; y++) {
                String row = plane[y];
                for (int z = 0; z < row.length(); z++) {
                    char letter = row.charAt(z);
                    if (letter == ' ') continue;
                    BlockPos realPos = getRealPos(x, y, z);
                    if (!getLevel().isLoaded(realPos)) return false;
                    BlockState blockState = ringStructure.mapper.get(letter).defaultBlockState();
                    int flags = Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE;
                    getLevel().setBlock(realPos, blockState, flags);
                }
            }
        }
        return true;
    }

    @Override
    public BlockPattern getPattern() {
        return getBlockPattern(getDefinition());
    }

    public static BlockPattern getBlockPattern(MultiblockMachineDefinition definition) {
        return MultiBlockFileReader.start(definition)
                .where('~', Predicates.controller(Predicates.blocks(definition.get())))
                .where(' ', Predicates.any())
                .where('A', Predicates.blocks(GTOBlocks.TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING.get()).or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(1)))
                .where('B', Predicates.blocks(GTOBlocks.SINGULARITY_REINFORCED_STELLAR_SHIELDING_CASING.get()))
                .where('C', Predicates.blocks(GTOBlocks.CELESTIAL_MATTER_GUIDANCE_CASING.get()))
                .where('D', Predicates.blocks(GTOBlocks.BOUNDLESS_GRAVITATIONALLY_SEVERED_STRUCTURE_CASING.get()))
                .where('E', Predicates.blocks(GTOBlocks.TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING.get()))
                .where('F', Predicates.blocks(GTOBlocks.STELLAR_ENERGY_SIPHON_CASING.get()))
                .where('G', GTOPredicates.tierBlock(GRAVITONFLOWMAP, GRAVITON_FLOW_TIER))
                .where('H', Predicates.blocks(GTOBlocks.SPATIALLY_TRANSCENDENT_GRAVITATIONAL_LENS_BLOCK.get()))
                .build();
    }

    private Recipe getRecipe() {
        return getRecipeBuilder().inputFluids(Fluids.WATER, 100).duration(20).buildRawRecipe();
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}
