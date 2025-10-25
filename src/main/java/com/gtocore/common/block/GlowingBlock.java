package com.gtocore.common.block;

import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import com.tterrag.registrate.util.entry.BlockEntry;

import static com.gtolib.utils.register.BlockRegisterUtils.block;

public class GlowingBlock extends Block {

    public GlowingBlock(int lightLevel, MapColor color) {
        this(createDefaultProperties(), lightLevel, color);
    }

    public GlowingBlock(Properties properties, int lightLevel, MapColor color) {
        super(properties.lightLevel(state -> validateLightLevel(lightLevel))
                .mapColor(color));
    }

    private static Properties createDefaultProperties() {
        return Properties.of()
                .mapColor(state -> MapColor.TERRACOTTA_WHITE)
                .lightLevel(state -> 0)
                .strength(4.0f, 8.0f)
                .sound(SoundType.STONE)
                .instrument(NoteBlockInstrument.BASS)
                .requiresCorrectToolForDrops();
    }

    private static int validateLightLevel(int level) {
        if (level < 0 || level > 15) {
            throw new IllegalArgumentException("Brightness level must be in the range 0-15");
        }
        return level;
    }

    static String[] num = { "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ", "Ⅸ", "Ⅹ", "Ⅺ", "Ⅻ" };

    public static BlockEntry<GlowingBlock>[] createStarStone() {
        BlockEntry<GlowingBlock>[] StarStone = new BlockEntry[num.length];
        for (int i = 0; i < num.length; i++) {
            int finalI = i;
            StarStone[i] = block("star_stone_" + (i + 1), "星辰石 " + num[i], p -> new GlowingBlock(finalI + 4, MapColor.TERRACOTTA_WHITE))
                    .lang("Star Stone " + num[i])
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll("star_stone_" + (finalI + 1), GTOCore.id("block/star_stone"))))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .item(BlockItem::new)
                    .tag(Tags.STAR_STONE)
                    .build()
                    .register();
        }
        return StarStone;
    }
}
