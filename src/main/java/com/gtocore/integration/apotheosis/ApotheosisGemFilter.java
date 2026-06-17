package com.gtocore.integration.apotheosis;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.cover.filter.ItemFilter;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.PhantomSlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemItem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@DataGeneratorScanned
public class ApotheosisGemFilter implements ItemFilter {

    @Getter
    protected boolean isBlackList;
    @Getter
    protected LootRarity rarity;
    @Getter
    protected DynamicHolder<Gem> gemType;
    protected Consumer<ItemFilter> itemWriter = filter -> {};
    protected Consumer<ItemFilter> onUpdated = filter -> itemWriter.accept(filter);
    private CustomItemStackHandler guiSLot;

    @Override
    public int testItemCount(ItemStack itemStack) {
        return test(itemStack) ? Integer.MAX_VALUE : 0;
    }

    @Override
    public WidgetGroup openConfigurator(int x, int y) {
        var raritySelector = new DraggableScrollableWidgetGroup(0, 16, 90, 20);
        raritySelector.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.HORIZONTAL);
        raritySelector.setXScrollBarHeight(2);
        raritySelector.setBackground(GuiTextures.DISPLAY);
        for (var rH : RarityRegistry.INSTANCE.getOrderedRarities()) {
            var r = rH.get();
            var button = new ButtonWidget(r.ordinal() * 18, 2, 16, 16, cd -> {
                if (this.rarity == r) {
                    this.rarity = null;
                } else {
                    this.rarity = r;
                }
                onUpdated.accept(this);
            }) {

                @Override
                @OnlyIn(Dist.CLIENT)
                protected void drawBackgroundTexture(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
                    var btnBg = rarity == r ? GuiTextures.BACKGROUND_INVERSE : GuiTextures.BACKGROUND;
                    setButtonTexture(new GuiTextureGroup(btnBg, new ItemStackTexture(r.getMaterial()).scale(0.8f)));
                    super.drawBackgroundTexture(graphics, mouseX, mouseY);
                }
            };
            button.setHoverTooltips(r.toComponent());
            raritySelector.addWidget(button);
        }

        guiSLot = new CustomItemStackHandler(1);
        guiSLot.setStackInSlot(0, gemType != null ? GemRegistry.createGemStack(gemType.get(), gemType.get().getMinRarity()) : ItemStack.EMPTY);
        guiSLot.setOnContentsChanged(() -> {
            ItemStack stack = guiSLot.getStackInSlot(0);
            if (stack.isEmpty() || !GemItem.getGem(stack).isBound()) {
                gemType = null;
            } else {
                gemType = GemItem.getGem(stack);
            }
            onUpdated.accept(this);
        });
        var typeSlot = new PhantomSlotWidget(guiSLot, 0,
                90, 40, stack -> GemItem.getGem(stack).isBound());
        typeSlot.setBackground(GuiTextures.SLOT);
        typeSlot.setClearSlotOnRightClick(true);
        typeSlot.setHoverTooltips(Component.translatable(TYPE_DESC));

        WidgetGroup group = new WidgetGroup(x, y, 18 * 3 + 25, 18 * 3);
        group.addWidget(new LabelWidget(0, 2, Component.translatable(RARITY_DESC)));
        group.addWidget(raritySelector);
        group.addWidget(new LabelWidget(0, 40, Component.translatable(TYPE_FILTER_DESC)));
        group.addWidget(typeSlot);
        group.addWidget(new ToggleButtonWidget(90, 0, 20, 20, GuiTextures.BUTTON_BLACKLIST, this::isBlackList, this::setBlackList));
        return group;
    }

    @Override
    public CompoundTag saveFilter() {
        if (!isBlackList && rarity == null && gemType == null) {
            return null;
        }
        var tag = new CompoundTag();
        tag.putBoolean("isBlackList", isBlackList);
        if (rarity != null) {
            tag.putInt("rarity", rarity.ordinal());
        }
        if (gemType != null) {
            tag.putString("gemType", gemType.getId().toString());
        }
        return tag;
    }

    public void setBlackList(boolean blackList) {
        isBlackList = blackList;
        onUpdated.accept(this);
    }

    public static ApotheosisGemFilter loadFilter(ItemStack itemStack) {
        return loadFilter(itemStack.getOrCreateTag(), filter -> itemStack.setTag(filter.saveFilter()));
    }

    private static ApotheosisGemFilter loadFilter(CompoundTag tag, Consumer<ItemFilter> itemWriter) {
        var handler = new ApotheosisGemFilter();
        handler.itemWriter = itemWriter;
        handler.isBlackList = tag.getBoolean("isBlackList");
        if (tag.contains("rarity")) {
            handler.rarity = RarityRegistry.byOrdinal(tag.getInt("rarity")).get();
        }
        if (tag.contains("gemType")) {
            handler.gemType = GemRegistry.INSTANCE.holder(RLUtils.parse(tag.getString("gemType")));
            if (!handler.gemType.isBound()) {
                handler.gemType = null;
            }
        }
        return handler;
    }

    @Override
    public void setOnUpdated(Consumer<ItemFilter> onUpdated) {
        this.onUpdated = filter -> {
            this.itemWriter.accept(filter);
            onUpdated.accept(filter);
        };
    }

    @Override
    public boolean test(ItemStack gemStack) {
        DynamicHolder<Gem> gem = GemItem.getGem(gemStack);
        if (!gem.isBound()) {
            return isBlackList;
        }
        if (gemType != null && !gemType.equals(gem)) {
            return isBlackList;
        }
        DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(gemStack);
        if (this.rarity != null && this.rarity != rarity.get()) {
            return isBlackList;
        }
        return !isBlackList;
    }

    @RegisterLanguage(cn = "过滤稀有度", en = "Filter by rarity")
    public static final String RARITY_DESC = "gtocore.apotheosis_gem_filter.rarity_desc";
    @RegisterLanguage(cn = "过滤宝石品种", en = "Filter by gem type")
    public static final String TYPE_FILTER_DESC = "gtocore.apotheosis_gem_filter.type_filter_desc";
    @RegisterLanguage(cn = "放入宝石来过滤品种，清空则不过滤", en = "Put in a gem to filter by type, or leave empty to not filter by type")
    public static final String TYPE_DESC = "gtocore.apotheosis_gem_filter.type_desc";
}
