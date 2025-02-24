package com.gto.gtocore.utils.register;

import com.gto.gtocore.data.lang.LangHandler;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class RecipeTypeRegisterUtils {

    public static final String MAGIC = "magic";

    public static final Map<String, LangHandler.ENCN> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    public static final Function<CompoundTag, String> TEMPERATURE = data -> LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(data.getInt("ebf_temp")));

    public static final Function<CompoundTag, String> COIL = data -> {
        ICoilType requiredCoil = ICoilType.getMinRequiredType(data.getInt("ebf_temp"));
        if (requiredCoil != null && requiredCoil.getMaterial() != null) {
            return LocalizationUtils.format("gtceu.recipe.coil.tier",
                    I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
        }
        return "";
    };

    public static final BiConsumer<GTRecipe, WidgetGroup> COIL_UI = (recipe, widgetGroup) -> {
        int temp = recipe.data.getInt("ebf_temp");
        List<List<ItemStack>> items = new ArrayList<>();
        items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                .map(coil -> new ItemStack(coil.getValue().get())).toList());
        widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false));
    };

    public static GTRecipeType register(String name, String enLang, String cnLang, String type) {
        if (LANG != null) LANG.put(name, new LangHandler.ENCN(enLang, cnLang));
        return GTRecipeTypes.register(name, type);
    }

    public static GTRecipeType register(String name, String cnLang, String type) {
        if (LANG != null) LANG.put(name, new LangHandler.ENCN(FormattingUtil.toEnglishName(name), cnLang));
        return GTRecipeTypes.register(name, type);
    }
}
