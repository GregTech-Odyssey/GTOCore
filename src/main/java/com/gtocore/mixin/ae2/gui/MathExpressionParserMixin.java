package com.gtocore.mixin.ae2.gui;

import com.gtocore.api.ae2.gui.AdvMathExpParser;

import appeng.client.gui.MathExpressionParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

@Mixin(MathExpressionParser.class)
public class MathExpressionParserMixin {

    /**
     * @author FYWinds
     * @reason Use advanced math expression parser
     */
    @Overwrite(remap = false)
    public static Optional<BigDecimal> parse(String expression, DecimalFormat decimalFormat) {
        try {
            return Optional.of(AdvMathExpParser.parse(expression, decimalFormat));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
