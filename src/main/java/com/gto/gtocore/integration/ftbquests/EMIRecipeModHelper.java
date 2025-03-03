package com.gto.gtocore.integration.ftbquests;

import net.minecraft.world.item.ItemStack;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiStack;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public final class EMIRecipeModHelper implements RecipeModHelper {

    public static void setRecipeModHelper() {
        FTBQuests.setRecipeModHelper(new EMIRecipeModHelper());
    }

    @Override
    public void refreshAll(Components components) {}

    @Override
    public void refreshRecipes(QuestObjectBase questObjectBase) {}

    @Override
    public void showRecipes(ItemStack itemStack) {
        EmiApi.displayRecipes(EmiStack.of(itemStack));
    }

    @Override
    public String getHelperName() {
        return "EMI";
    }

    @Override
    public boolean isRecipeModAvailable() {
        return true;
    }
}
