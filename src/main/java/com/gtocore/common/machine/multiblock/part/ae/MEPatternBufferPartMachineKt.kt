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
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour
import com.gtolib.api.annotation.DataGeneratorScanned
import com.gtolib.api.annotation.language.RegisterLanguage
import com.gtolib.api.gui.ktflexible.VBoxBuilder
import com.gtolib.api.gui.ktflexible.field
import com.gtolib.api.gui.ktflexible.iconButton
import com.gtolib.api.network.NetworkPack
import com.gtolib.api.recipe.RecipeBuilder
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper
import dev.emi.emi.api.EmiApi

import java.util.function.IntSupplier

@DataGeneratorScanned
open class MEPatternBufferPartMachineKt(holder: MetaMachineBlockEntity, maxPatternCount: Int) : MEPatternBufferPartMachine(holder, maxPatternCount) {
    @DataGeneratorScanned
    companion object {

        @RegisterLanguage(cn = "此样板物品输入槽", en = "The item input slots of this pattern")
        const val ITEM_SPECIAL: String = "gtceu.ae.pattern_part_machine.ITEM_SPECIAL"

        @RegisterLanguage(cn = "此样板流体输入槽", en = "The fluid input slots of this pattern")
        const val FLUID_SPECIAL: String = "gtceu.ae.pattern_part_machine.FLUID_SPECIAL"

        @RegisterLanguage(cn = "此样板电路输入槽", en = "The circuit input slot of this pattern")
        const val CIRCUIT_SPECIAL: String = "gtceu.ae.pattern_part_machine.CIRCUIT_SPECIAL"

        @RegisterLanguage(cn = "此样板记录的配方", en = "The recipe recorded by this pattern")
        const val RECIPE_SPECIAL: String = "gtceu.ae.pattern_part_machine.RECIPE_SPECIAL"

        @RegisterLanguage(cn = "点此查看配方详情", en = "Click to see recipe details")
        const val VIEW_RECIPE: String = "gtceu.ae.pattern_part_machine.VIEW_RECIPE"

        @RegisterLanguage(cn = "当前并没有记录任何配方", en = "No recipe is recorded currently")
        const val NO_RECIPE: String = "gtceu.ae.pattern_part_machine.NO_RECIPE"

        @RegisterLanguage(cn = "解除当前机器的配方锁定", en = "Clear the recipe lock of this machine")
        const val CLEAR_RECIPE_SLOT: String = "gtceu.ae.pattern_part_machine.clear_recipe"

        @RegisterLanguage(cn = "当前机器的配方锁定已清除", en = "The recipe lock of this machine has been cleared")
        const val CLEAR_RECIPE_SLOT_MSG: String = "gtceu.ae.pattern_part_machine.clear_recipe_msg"

        @RegisterLanguage(cn = "打开emi页面后，选择一个配方，用“+”按钮将其添加到样板中。", en = "After opening the emi page, select a recipe and use the \"+\" button to add it to the pattern.")
        const val ADD_RECIPE_MSG: String = "gtceu.ae.pattern_part_machine.clear_recipe_msg2"

        @RegisterLanguage(cn = "此样板物品与流体配置", en = "The item and fluid configuration of this pattern")
        const val PATTERN_CONFIGURATION: String = "gtceu.ae.pattern_part_machine.PATTERN_CONFIGURATION"

        @RegisterLanguage(cn = "发信合成模式", en = "Emitting crafting mode")
        const val EMITTING_CRAFTING_MODE: String = "gtceu.ae.pattern_part_machine.EMITTING_CRAFTING_MODE"

        @RegisterLanguage(cn = "物品不够时请求合成", en = "Request crafting when items are insufficient")
        const val REQUEST_CRAFTING_WHEN_INSUFFICIENT: String = "gtceu.ae.pattern_part_machine.REQUEST_CRAFTING_WHEN_INSUFFICIENT"

        @RegisterLanguage(cn = "已锁定，由样板内的配方自动拉取虚拟物品进行合成", en = "Locked, automatically pull virtual items for crafting according to the recipe in the pattern")
        const val ITEM_LOCKED: String = "gtceu.ae.pattern_part_machine.locked_emitting_crafting_mode"

        @RegisterLanguage(
            cn = "该模式与标准发信器的合成卡功能相似，在下单请求该物品后，机器会使用样板中的配方被动持续向机器内输入",
            en = "This mode is similar to the crafting card function of a standard emitter. " +
                "After placing an order for the item, the machine will passively and continuously input items into the machine using the recipe in the pattern.",
        )
        const val EMITTING_CRAFTING_MODE_TOOLTIP: String = "gtceu.ae.pattern_part_machine.EMITTING_CRAFTING_MODE_TOOLTIP"

        @RegisterLanguage(cn = "低存量触发模式", en = "Low stock triggering mode")
        const val LOW_STOCK_TRIGGERING_MODE: String = "gtceu.ae.pattern_part_machine.LOW_STOCK_TRIGGERING_MODE"

        @RegisterLanguage(
            cn = "该模式会在网络库存量低于设定数量时触发持续被动配方输入，直到库存量满足要求。",
            en = "This mode will trigger continuous passive recipe input when the network inventory is below the set quantity, until the inventory meets the requirements.",
        )
        const val LOW_STOCK_TRIGGERING_MODE_TOOLTIP: String = "gtceu.ae.pattern_part_machine.LOW_STOCK_TRIGGERING_MODE_TOOLTIP"

        @RegisterLanguage(cn = "低存量库存触发阈值", en = "Low stock triggering threshold")
        const val LOW_STOCK_TRIGGERING_THRESHOLD: String = "gtceu.ae.pattern_part_machine.LOW_STOCK_TRIGGERING_THRESHOLD"

        @RegisterLanguage(cn = "被动输入乘数", en = "Passive input multiplier")
        const val PASSIVE_INPUT_MULTIPLIER: String = "gtceu.ae.pattern_part_machine.PASSIVE_INPUT_MULTIPLIER"

        @RegisterLanguage(
            cn = "按照设定的倍数调整被动输入的数量。例如，设定为10时，按样板配置的数量x10进行被动输入。",
            en = "Adjust the quantity of passive input according to the set multiplier. For example, when set to 10, passive input will be performed according to the quantity configured in the pattern x10.",
        )
        const val PASSIVE_INPUT_MULTIPLIER_TOOLTIP: String = "gtceu.ae.pattern_part_machine.PASSIVE_INPUT_MULTIPLIER_TOOLTIP"

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
        val recipe = rl?.let { RecipeBuilder.get(it) }
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
    override fun runOnUpdate() = run {
        if (isRemote) {
            configuratorField.set(-1)
            configuratorField.markAsChanged()
            syncToServer()
        }
    }

    override fun VBoxBuilder.buildToolBoxContent() {
        when {
            configuratorField.get() < 0 -> {
            }

            configuratorField.get() in 0..<maxPatternCount -> {
                vBox(width = availableWidth, alwaysHorizonCenter = true, style = { spacing = 2 }) {
                    val width = this@vBox.availableWidth
                    val itemHandler = getInternalInventory()[configuratorField.get()].lockableInventory
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(ITEM_SPECIAL) })
                    (0 until itemHandler.slots).chunked(9).forEach { indices ->
                        hBox(height = 18) {
                            indices.forEach { index ->
                                widget(
                                    object : SlotWidget(itemHandler, index, 0, 0, true, true) {
                                        override fun drawInBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
                                            super.drawInBackground(graphics, mouseX, mouseY, partialTicks)
                                            if (configuratorField.get() < 0) return
                                            if (getInternalInventory()[configuratorField.get()].isLock) {
                                                DrawerHelper.drawSolidRect(graphics, positionX, positionY, sizeWidth, sizeHeight, 0x80000000.toInt())
                                            }
                                        }

                                        override fun getFullTooltipTexts(): List<Component> {
                                            var superList = super.getFullTooltipTexts()
                                            superList = superList.toMutableList()
                                            run {
                                                if (configuratorField.get() < 0) return@run
                                                if (getInternalInventory()[configuratorField.get()].isLock) {
                                                    superList.add(Component.translatable(ITEM_LOCKED))
                                                }
                                            }
                                            return superList
                                        }
                                    }.apply {
                                        setBackgroundTexture(GuiTextures.SLOT)
                                    },
                                )
                            }
                        }
                    }
                    val fluidHandler: Array<CustomFluidTank> = getInternalInventory()[configuratorField.get()].shareTank.storages
                    buildFluidSection(this, width, fluidHandler)
                    val circuitHandler = getInternalInventory()[configuratorField.get()].circuitInventory.storage
                    buildCircuitSection(
                        container = this,
                        width = width,
                        circuitSlot = createReadOnlyCircuitSlot(circuitHandler),
                        getter = { IntCircuitBehaviour.getCircuitConfiguration(getInternalInventory()[configuratorField.get()].circuitInventory.storage.getStackInSlot(0)).toString() },
                        setter = {
                            val circuit = when {
                                it.toIntOrNull() == null -> 0
                                else -> it.toInt().coerceAtMost(32).coerceAtLeast(0)
                            }
                            getInternalInventory()[configuratorField.get()].circuitInventory.storage.setStackInSlot(0, if (circuit == 0) ItemStack.EMPTY else IntCircuitBehaviour.stack(circuit))
                        },
                    )
                    textBlock(maxWidth = width, textSupplier = { Component.translatable(RECIPE_SPECIAL) })
                    hBox(height = 18, style = { spacing = 4 }) {
                        iconButton(tooltips = {
                            val recipe = recipeIdGetter.invoke(configuratorField.get())
                            if (recipe != null) {
                                Component.translatable(VIEW_RECIPE)
                            } else {
                                Component.translatable(NO_RECIPE)
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
                        ).setHoverTooltips(Component.translatable(ADD_RECIPE_MSG))
                        iconButton(width = 16, tooltips = {
                            if (getInternalInventory()[configuratorField.get()].recipe != null) {
                                Component.translatable(CLEAR_RECIPE_SLOT)
                            } else {
                                Component.translatable(CLEAR_RECIPE_SLOT_MSG)
                            }
                            Component.translatable(CLEAR_RECIPE_SLOT)
                        }) {
                            getInternalInventory()[configuratorField.get()].setRecipe(null)
                        }
                    }
                    buildSectionDivider(this)
                }
            }
        }
    }
}
