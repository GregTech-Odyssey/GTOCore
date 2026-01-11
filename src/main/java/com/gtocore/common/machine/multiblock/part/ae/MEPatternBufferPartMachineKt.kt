package com.gtocore.common.machine.multiblock.part.ae

import com.gtocore.api.gui.ktflexible.textBlock
import com.gtocore.common.data.GTORecipes

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity
import com.gregtechceu.gtceu.api.gui.GuiTextures
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget
import com.gregtechceu.gtceu.api.gui.widget.TankWidget
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.VBoxBuilder
import com.gtolib.api.gui.ktflexible.blank
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.iconButton
import com.gtolib.api.network.NetworkPack
import com.gtolib.api.recipe.RecipeBuilder
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import com.lowdragmc.lowdraglib.gui.widget.Widget
import com.lowdragmc.lowdraglib.jei.IngredientIO
import dev.emi.emi.api.EmiApi

import java.util.function.IntSupplier

@DataGeneratorScanned
open class MEPatternBufferPartMachineKt(holder: MetaMachineBlockEntity, maxPatternCount: Int) : MEPatternBufferPartMachine(holder, maxPatternCount) {
    @DataGeneratorScanned
    companion object {

        @RegisterLanguage(cn = "此样板物品输入槽", en = "The item input slots of this pattern")
        const val item_special: String = "gtceu.ae.pattern_part_machine.item_special"

        @RegisterLanguage(cn = "此样板流体输入槽", en = "The fluid input slots of this pattern")
        const val fluid_special: String = "gtceu.ae.pattern_part_machine.fluid_special"

        @RegisterLanguage(cn = "此样板电路输入槽", en = "The circuit input slot of this pattern")
        const val circuit_special: String = "gtceu.ae.pattern_part_machine.circuit_special"

        @RegisterLanguage(cn = "此样板记录的配方", en = "The recipe recorded by this pattern")
        const val recipe_special: String = "gtceu.ae.pattern_part_machine.recipe_special"

        @RegisterLanguage(cn = "点此查看配方详情", en = "Click to see recipe details")
        const val view_recipe: String = "gtceu.ae.pattern_part_machine.view_recipe"

        @RegisterLanguage(cn = "当前并没有记录任何配方", en = "No recipe is recorded currently")
        const val no_recipe: String = "gtceu.ae.pattern_part_machine.no_recipe"

        @RegisterLanguage(cn = "解除当前机器的配方锁定", en = "Clear the recipe lock of this machine")
        const val clear_recipe_slot: String = "gtceu.ae.pattern_part_machine.clear_recipe"

        @RegisterLanguage(cn = "当前机器的配方锁定已清除", en = "The recipe lock of this machine has been cleared")
        const val clear_recipe_slot_msg: String = "gtceu.ae.pattern_part_machine.clear_recipe_msg"

        @RegisterLanguage(cn = "打开emi页面后，选择一个配方，用“+”按钮将其添加到样板中。", en = "After opening the emi page, select a recipe and use the \"+\" button to add it to the pattern.")
        const val add_recipe_msg: String = "gtceu.ae.pattern_part_machine.clear_recipe_msg2"

        val SET_ID_CHANNEL: NetworkPack = NetworkPack.registerC2S(
            "me_pattern_buffer_set_id_channel",
        ) { player: ServerPlayer, buf: FriendlyByteBuf ->
            val blockPos = buf.readBlockPos()
            val slot = buf.readVarInt()
            val recipeId = buf.readResourceLocation()
            val blockEntity = player.level().getBlockEntity(blockPos)
            if (blockEntity is MetaMachineBlockEntity && blockEntity.metaMachine is MEPatternBufferPartMachineKt) {
                val machine = blockEntity.metaMachine as MEPatternBufferPartMachineKt
                machine.recipeIdSetter.invoke(slot, recipeId)
            }
        }
    }

    val recipeIdGetter = { id: Int ->
        if (id < 0) {
            null
        } else {
            val recipe = getInternalInventory()[id].recipe
            if (recipe != null) {
                recipe.id
            } else {
                patternInventory.getStackInSlot(id).let {
                    if (it.isEmpty) {
                        null
                    } else {
                        val rl = ResourceLocation.tryParse(it.getOrCreateTag().getString("recipe"))
                        rl
                    }
                }
            }
        }
    }
    val recipeIdSetter = { id: Int, rl: ResourceLocation? ->
        val recipe = rl?.let { RecipeBuilder.RECIPE_MAP[it] }
        patternInventory.getStackInSlot(id).let { stack ->
            if (stack.isEmpty) return@let
            if (recipe == null) {
                stack.getOrCreateTag().remove("recipe")
            } else {
                stack.getOrCreateTag().putString("recipe", recipe.id.toString())
            }
        }
        getInternalInventory()[id].setRecipe(recipe)
    }

    override fun getApplyIndex() = IntSupplier { configuratorField.get() }
    override fun runOnUpdate() = run { if (isRemote)configuratorField.setAndSyncToServer(-1) }

    override fun VBoxBuilder.buildToolBoxContent() {
        when {
            configuratorField.get() < 0 -> {
            }
            configuratorField.get() in 0..<maxPatternCount -> {
                vBox(width = availableWidth, alwaysHorizonCenter = true, style = { spacing = 2 }) {
                    val width = this@vBox.availableWidth
                    val itemHandler = getInternalInventory()[configuratorField.get()].lockableInventory
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(item_special) })
                    (0 until itemHandler.slots).chunked(9).forEach { indices ->
                        hBox(height = 18) {
                            indices.forEach { index ->
                                widget(
                                    SlotWidget(itemHandler, index, 0, 0, true, true).apply {
                                        setBackgroundTexture(GuiTextures.SLOT)
                                    },
                                )
                            }
                        }
                    }
                    val fluidHandler: Array<CustomFluidTank> = getInternalInventory()[configuratorField.get()].shareTank.storages
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(fluid_special) })
                    (0 until fluidHandler.size).chunked(9).forEach { indices ->
                        hBox(height = 18) {
                            indices.forEach { index ->
                                widget(
                                    TankWidget(
                                        fluidHandler[index],
                                        0,
                                        0,
                                        18,
                                        18,
                                        true,
                                        true,
                                    ).setBackground(GuiTextures.FLUID_SLOT),
                                )
                            }
                        }
                    }
                    val circuitHandler = getInternalInventory()[configuratorField.get()].circuitInventory.storage
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(circuit_special) })
                    hBox(height = 18, style = { spacing = 4 }) {
                        widget(
                            SlotWidget(circuitHandler, 0, 0, 0, false, false).apply {
                                setBackgroundTexture(GuiTextures.SLOT)
                                setIngredientIO(IngredientIO.RENDER_ONLY)
                            },
                        )
                        field(
                            height = 18,
                            getter = { IntCircuitBehaviour.getCircuitConfiguration(getInternalInventory()[configuratorField.get()].circuitInventory.storage.getStackInSlot(0)).toString() },
                            setter = {
                                val circuit = when {
                                    it.toIntOrNull() == null -> 0
                                    else -> it.toInt().coerceAtMost(32).coerceAtLeast(0)
                                }
                                getInternalInventory()[configuratorField.get()].circuitInventory.storage.setStackInSlot(0, if (circuit == 0) ItemStack.EMPTY else IntCircuitBehaviour.stack(circuit))
                            },
                        )
                    }
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(recipe_special) })
                    hBox(height = 18, style = { spacing = 4 }) {
                        iconButton(tooltips = {
                            val recipe = recipeIdGetter.invoke(configuratorField.get())
                            if (recipe != null) {
                                Component.translatable(view_recipe)
                            } else {
                                Component.translatable(no_recipe)
                            }
                        }) {
                            val recipeId = recipeIdGetter.invoke(configuratorField.get())
                            if (recipeId != null && isRemote) {
                                GTORecipes.EMI_RECIPES.firstOrNull { it.id == recipeId }?.let(EmiApi::displayRecipe)
                            }
                        }
                        field(
                            width = 100,
                            height = 18,
                            getter = { recipeIdGetter.invoke(configuratorField.get())?.toString() ?: "" },
                            setter = {
                                val rl = ResourceLocation.tryParse(it)
                                recipeIdSetter.invoke(configuratorField.get(), rl)
                            },
                            rightClickClear = true,
                        ).setHoverTooltips(Component.translatable(add_recipe_msg))
                        iconButton(width = 16, tooltips = {
                            if (getInternalInventory()[configuratorField.get()].recipe != null) {
                                Component.translatable(clear_recipe_slot)
                            } else {
                                Component.translatable(clear_recipe_slot_msg)
                            }
                            Component.translatable(clear_recipe_slot)
                        }) {
                            getInternalInventory()[configuratorField.get()].setRecipe(null)
                        }
                    }
                    blank(height = 4)
                    widget(object : Widget(0, 0, availableWidth, 3) {
                        override fun drawInBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
                            super.drawInBackground(graphics, mouseX, mouseY, partialTicks)
                            DrawerHelper.drawSolidRect(graphics, positionX, positionY, sizeWidth, sizeHeight, 0xFFFFFFFF.toInt())
                        }
                    })
                    blank(height = 4)
                }
            }
        }
    }
}
