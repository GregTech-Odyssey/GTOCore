package com.gtocore.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.BLOCK;

public class StructureVBO {

    private String[][] structure;

    public final Char2ObjectOpenHashMap<Block> mapper = new Char2ObjectOpenHashMap<>();

    public StructureVBO assignStructure(String[][] structure) {
        this.structure = structure;
        return this;
    }

    public StructureVBO addMapping(char letter, Block block) {
        mapper.put(letter, block);
        return this;
    }

    public TextureUpdateRequester getTextureUpdateRequestor() {
        TextureUpdateRequester textureUpdateRequester = new TextureUpdateRequester();
        for (Block block : mapper.values()) {
            textureUpdateRequester.add(block);
        }
        return textureUpdateRequester;
    }

    private boolean isOpaqueAt(int x, int y, int z) {
        char letter = structure[x][y].charAt(z);
        if (letter == ' ') return false;
        Block block = mapper.get(letter);
        if (block == null) return false;
        if (block == Blocks.AIR) return false;
        return block.defaultBlockState().isSolidRender(Minecraft.getInstance().level, BlockPos.ZERO);
    }

    private List<Direction> getVisibleFaces(int x, int y, int z) {
        List<Direction> visibility = new ArrayList<>();
        int maxX = structure.length - 1;
        int maxY = structure[0].length - 1;
        int maxZ = structure[0][0].length() - 1;
        if (!((x > 0) && ((isOpaqueAt(x - 1, y, z)) || structure[x][y].charAt(z) == structure[x - 1][y].charAt(z)))) visibility.add(Direction.EAST);
        if (!((x < maxX) && ((isOpaqueAt(x + 1, y, z)) || structure[x][y].charAt(z) == structure[x + 1][y].charAt(z)))) visibility.add(Direction.WEST);
        if (!((y > 0) && ((isOpaqueAt(x, y - 1, z)) || structure[x][y].charAt(z) == structure[x][y - 1].charAt(z)))) visibility.add(Direction.DOWN);
        if (!((y < maxY) && ((isOpaqueAt(x, y + 1, z)) || structure[x][y].charAt(z) == structure[x][y + 1].charAt(z)))) visibility.add(Direction.UP);
        if (!((z > 0) && ((isOpaqueAt(x, y, z - 1)) || structure[x][y].charAt(z) == structure[x][y].charAt(z - 1)))) visibility.add(Direction.NORTH);
        if (!((z < maxZ) && ((isOpaqueAt(x, y, z + 1)) || structure[x][y].charAt(z) == structure[x][y].charAt(z + 1)))) visibility.add(Direction.SOUTH);
        return visibility;
    }

    public VertexBuffer build() {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, BLOCK);
        Minecraft mc = Minecraft.getInstance();
        FaceCulledRenderBlocks renderer = new FaceCulledRenderBlocks(mc, mc.getTextureManager(), mc.getModelManager(), mc.getItemColors(), mc.getItemRenderer().getBlockEntityRenderer());
        PoseStack poseStack = new PoseStack();

        for (int x = 0; x < structure.length; x++) {
            String[] plane = structure[x];
            for (int y = 0; y < plane.length; y++) {
                String row = plane[y];
                for (int z = 0; z < row.length(); z++) {
                    char letter = row.charAt(z);
                    if (letter == ' ') continue;
                    Block block = mapper.get(letter);
                    if (block == null) {
                        continue;
                    }
                    if (block == Blocks.AIR) continue;

                    List<Direction> faceInfo = getVisibleFaces(x, y, z);

                    if (faceInfo.isEmpty()) continue;

                    BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(block.defaultBlockState());
                    renderer.setDirections(faceInfo);

                    poseStack.pushPose();

                    poseStack.translate(structure.length / 2f - x,
                            -plane.length / 2f + y,
                            -row.length() / 2f + z);

                    int light = block.getLightEmission(block.defaultBlockState(), EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                    renderer.renderModelLists(model, block.asItem().getDefaultInstance(), LightTexture.pack(light, 13), OverlayTexture.NO_OVERLAY, poseStack, bufferBuilder);
                    poseStack.popPose();
                }
            }
        }
        buffer.bind();
        buffer.upload(bufferBuilder.end());
        VertexBuffer.unbind();
        return buffer;
    }
}
