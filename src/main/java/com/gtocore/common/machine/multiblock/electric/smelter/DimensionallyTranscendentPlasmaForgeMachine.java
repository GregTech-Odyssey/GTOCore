package com.gtocore.common.machine.multiblock.electric.smelter;

import com.gtocore.common.block.CoilType;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.sound.AutoReleasedSound;
import com.gregtechceu.gtceu.common.data.GTRecipeDataKeys;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.core.proxy.ClientProxy;

import java.util.List;

public final class DimensionallyTranscendentPlasmaForgeMachine extends CoilCrossRecipeMultiblockMachine {

    public DimensionallyTranscendentPlasmaForgeMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, false, false, MachineUtils::getHatchParallel);
    }

    @Override
    public int getTemperature() {
        return getCoilType() == CoilType.URUIUM ? 32000 : super.getTemperature();
    }

    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        if (recipe.definition.recipeType == GTORecipeTypes.STELLAR_FORGE_RECIPES) {
            if (getCoilType() != CoilType.URUIUM) {
                setIdleReason(() -> Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil"));
                return null;
            }
        } else if (recipe.data.getInt(GTRecipeDataKeys.EBF_TEMP) > getTemperature()) {
            setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
            return null;
        }
        return super.getRealRecipe(unit, recipe);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature", Component.literal(FormattingUtil.formatNumbers(getTemperature()) + "K").withStyle(ChatFormatting.BLUE)));
        if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES && getCoilType() != CoilType.URUIUM) {
            textList.add(Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new Logic(this) {

            @OnlyIn(Dist.CLIENT)
            public void updateSound() {
                if (isWorking() && shouldWorkingPlaySound()) {
                    var sound = getSound();
                    if (sound == null) sound = getRecipeType().getSound();
                    if (workingSound instanceof AutoReleasedSound soundEntry) {
                        if (soundEntry.soundEntry == sound && !soundEntry.isStopped()) {
                            return;
                        }
                        soundEntry.release();
                        workingSound = null;
                    }
                    if (sound != null) {
                        var sound0 = new AutoReleasedSound(sound, () -> shouldWorkingPlaySound() && isWorking() && !isInValid() && getLevel().isLoaded(getPos()), getPos().relative(getFrontFacing(), -7), true, 0, 2f, 1) {

                            @Override
                            public void tick() {
                                super.tick();
                                var clientPlayer = ClientProxy.INSTANCE.getClientPlayer();
                                var yp = clientPlayer != null ? (int) clientPlayer.getY() : getPos().getY();
                                y = Mth.clamp(yp, getPos().getY(), getPos().getY() + 71);
                            }
                        };
                        Minecraft.getInstance().getSoundManager().play(sound0);
                        workingSound = sound0;
                    }
                } else if (workingSound instanceof AutoReleasedSound soundEntry) {
                    soundEntry.release();
                    workingSound = null;
                }
            }
        };
    }
}
