package com.gtocore.api.ae2.gui

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParsePosition
import java.util.*

object AdvMathExpParser {
    private const val MAX_SHIFT = 64
    private val MAX_EXPONENT = 30.toBigDecimal()
    private val MAX_BASE = 1E9.toBigDecimal()
    private const val PRECISION = 30
    private val MATH_CONTEXT = MathContext(PRECISION, RoundingMode.FLOOR)

    private val UNIT_MAP = mapOf(
        'k' to BigDecimal("1E3"),
        'm' to BigDecimal("1E6"),
        'g' to BigDecimal("1E9"),
        't' to BigDecimal("1E12"),
        'p' to BigDecimal("1E15"),
    ).let { map ->
        val upperMap = map.mapKeys { it.key.uppercaseChar() }
        map + upperMap
    } // Generate uppercase mappings to avoid checking case later

    private enum class Op(val precedence: Int, val symbol: String?) {
        PLUS(2, "+"),
        MINUS(2, "-"),
        MULTIPLY(3, "*"),
        DIVIDE(3, "/"),
        POWER(5, "^"),
        L_SHIFT(1, "<<"),
        R_SHIFT(1, ">>"),
        UNARY_MINUS(4, null),
        L_PAREN(0, "("), // precedence not used for parentheses
        R_PAREN(0, ")"), // precedence not used for parentheses
        ;

        companion object {
            private val map = entries.associateBy(Op::symbol)
            private val charMap = entries
                .mapNotNull { if (it.symbol != null && it.symbol.length == 1) it.symbol[0] to it else null }
                .toMap()
            fun fromSymbol(symbol: String): Op? = map[symbol]
            fun fromSymbol(c: Char): Op? = charMap[c]
        }
    }

    /**
     * Parses and evaluates a mathematical expression.
     *
     * Supported operators: +, -, *, /, ^, <<, >>
     *     * Supports parentheses for grouping.
     *     * Supports unit suffixes: k, m, g, t (case-insensitive).
     *     * Supports scientific notation (e.g., 1.23e4).
     *
     * @param expression The mathematical expression as a string.
     * @param format Optional DecimalFormat for parsing numbers.
     * @return Evaluated result as a BigDecimal.
     * @throws IllegalArgumentException If the expression is invalid.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IllegalArgumentException::class, ArithmeticException::class)
    fun parse(expression: String, format: DecimalFormat? = null): BigDecimal {
        if (expression.isBlank()) return BigDecimal.ZERO

        val tokens = tokenize(expression, format)
        val rpn = shuntingYard(tokens)
        return evaluate(rpn)
    }

    // List<Any> to prevent boxing
    private fun tokenize(expr: String, format: DecimalFormat?): List<Any> {
        val len = expr.length
        val tokens = ArrayList<Any>(len / 2) // rough estimate
        var i = 0

        while (i < len) {
            val c = expr[i]
            val symbol = Op.fromSymbol(c)

            when {
                c.isWhitespace() -> i++

                c.isDigit() || c == '.' -> {
                    val start = i
                    while (i < len && (expr[i].isDigit() || expr[i] == '.')) {
                        i++
                    }

                    // Scientific notation
                    if (i < len && (expr[i] == 'e' || expr[i] == 'E')) {
                        val mark = i
                        i++
                        if (i < len && (expr[i] == '+' || expr[i] == '-')) i++
                        if (i < len && expr[i].isDigit()) {
                            while (i < len && expr[i].isDigit()) i++
                        } else {
                            i = mark
                        }
                    }

                    val numStr = expr.substring(start, i)
                    var valDecimal = if (format != null) {
                        val pp = ParsePosition(0)
                        val num = format.parse(numStr, pp)
                        if (pp.index != numStr.length) {
                            BigDecimal(numStr)
                        } else {
                            BigDecimal(num.toString())
                        }
                    } else {
                        BigDecimal(numStr)
                    }

                    // Unit suffix
                    if (i < len) {
                        val unitMultiplier = UNIT_MAP[expr[i]]
                        if (unitMultiplier != null) {
                            valDecimal = valDecimal.multiply(unitMultiplier)
                            i++
                        }
                    }
                    tokens.add(valDecimal)
                }

                symbol == Op.L_PAREN || symbol == Op.R_PAREN -> {
                    tokens.add(symbol)
                    i++
                }

                (c == '<') && (i + 1 < len && expr[i + 1] == c) -> {
                    tokens.add(Op.L_SHIFT)
                    i += 2
                }

                (c == '>') && (i + 1 < len && expr[i + 1] == c) -> {
                    tokens.add(Op.R_SHIFT)
                    i += 2
                }

                symbol == Op.MINUS -> {
                    // unary minus
                    if (tokens.isEmpty() || (tokens.last() is Op && tokens.last() != Op.R_PAREN)) {
                        tokens.add(Op.UNARY_MINUS)
                    } else {
                        tokens.add(symbol)
                    }
                    i++
                }

                symbol == Op.PLUS || symbol == Op.MULTIPLY || symbol == Op.DIVIDE || symbol == Op.POWER -> {
                    tokens.add(symbol)
                    i++
                }

                else -> throw IllegalArgumentException("Unexpected character at index $i: '$c'")
            }
        }
        return tokens
    }

    private fun shuntingYard(tokens: List<Any>): List<Any> {
        val output = ArrayList<Any>()
        val stack = ArrayDeque<Op>()

        for (token in tokens) {
            when (token) {
                is BigDecimal -> output.add(token)
                is Op -> {
                    when (token) {
                        Op.L_PAREN -> stack.push(token)
                        Op.R_PAREN -> {
                            while (stack.isNotEmpty() && stack.peek() != Op.L_PAREN) {
                                output.add(stack.peek())
                                stack.pop()
                            }
                            if (stack.isEmpty()) throw IllegalArgumentException("Mismatched parentheses")
                            stack.pop()
                        }
                        else -> {
                            while (stack.isNotEmpty() && stack.peek() != Op.L_PAREN) {
                                val top = stack.peek()
                                val shouldPop = if (token == Op.UNARY_MINUS) {
                                    top.precedence > token.precedence
                                } else {
                                    top.precedence >= token.precedence
                                }
                                if (shouldPop) {
                                    output.add(stack.pop())
                                } else {
                                    break
                                }
                            }
                            stack.push(token)
                        }
                    }
                }
            }
        }
        while (stack.isNotEmpty()) {
            val top = stack.pop()
            if (top == Op.L_PAREN) throw IllegalArgumentException("Mismatched parentheses")
            output.add(top)
        }
        return output
    }

    private fun evaluate(rpn: List<Any>): BigDecimal {
        val stack = ArrayDeque<BigDecimal>()

        for (token in rpn) {
            when (token) {
                is BigDecimal -> stack.push(token)
                is Op -> {
                    // Unary minus
                    if (token == Op.UNARY_MINUS) {
                        if (stack.isEmpty()) throw IllegalArgumentException("Invalid expression: missing operand for unary minus")
                        val b = stack.pop()
                        stack.push(b.negate(MATH_CONTEXT))
                        continue
                    }

                    if (stack.size < 2) throw IllegalArgumentException("Invalid expression")
                    var b = stack.pop()
                    val a = stack.pop()

                    val res = when (token) {
                        Op.PLUS -> a.add(b, MATH_CONTEXT)
                        Op.MINUS -> a.subtract(b, MATH_CONTEXT)
                        Op.MULTIPLY -> a.multiply(b, MATH_CONTEXT)
                        Op.DIVIDE -> a.divide(b, MATH_CONTEXT)
                        Op.POWER -> {
                            b = b.stripTrailingZeros()

                            // limit exponent to 30
                            if (b > MAX_EXPONENT) {
                                throw ArithmeticException("Exponent too large")
                            }

                            // limit base number to 1e9
                            if (a > MAX_BASE) {
                                throw ArithmeticException("Base too large")
                            }

                            if (b.scale() > 0) {
                                throw ArithmeticException("Non-integer exponent not supported")
                            }

                            a.pow(b.intValueExact(), MATH_CONTEXT)
                        }
                        Op.L_SHIFT -> {
                            val bi = a.toBigInteger()
                            val shift = b.toInt()
                            if (shift > MAX_SHIFT) {
                                throw ArithmeticException("Shift amount too large")
                            }
                            BigDecimal(bi.shiftLeft(shift))
                        }
                        Op.R_SHIFT -> {
                            val bi = a.toBigInteger()
                            val shift = b.toInt()
                            if (shift > MAX_SHIFT) {
                                throw ArithmeticException("Shift amount too large")
                            }
                            BigDecimal(bi.shiftRight(shift))
                        }
                        else -> throw IllegalArgumentException("Unknown operator: $token")
                    }
                    stack.push(res)
                }
            }
        }

        if (stack.size != 1) throw IllegalArgumentException("Invalid expression result")
        return stack.pop()
    }

    @JvmOverloads
    fun test(performance: Boolean = false) {
        // cases from AI
        val testCases = mapOf(
            // Basic Arithmetic
            "0" to BigDecimal("0"),
            "1 + 1" to BigDecimal("2"),
            "10 - 4" to BigDecimal("6"),
            "2 * 3" to BigDecimal("6"),
            "10 / 2" to BigDecimal("5"),
            "10 / 4" to BigDecimal("2.5"),
            "0.1 + 0.2" to BigDecimal("0.3"),
            "-1 + 5" to BigDecimal("4"),
            "-3 * 2" to BigDecimal("-6"),
            "5 + -2" to BigDecimal("3"),
            "5 - -2" to BigDecimal("7"),

            // Precedence and Parentheses
            "1 + 2 * 3" to BigDecimal("7"),
            "(1 + 2) * 3" to BigDecimal("9"),
            "10 - 2 + 3" to BigDecimal("11"),
            "100 / 10 * 2" to BigDecimal("20"),
            "2 + 3 << 2" to BigDecimal("20"),
            "1 << 2 + 3" to BigDecimal("32"),
            "4 * 2 ^ 3" to BigDecimal("32"),

            // Power
            "2 ^ 3" to BigDecimal("8"),
            "2 ^ 3 ^ 2" to BigDecimal("64"),
            "2 ^ (3 ^ 2)" to BigDecimal("512"),
//            "4 ^ 0.5" to BigDecimal("2"), // Disabled: non-integer exponent not supported

            // Unit Suffixes
            "1k" to BigDecimal("1000"),
            "1.5k" to BigDecimal("1500.0"),
            "1m" to BigDecimal("1000000"),
            "0.5g" to BigDecimal("500000000"),
            "2t" to BigDecimal("2000000000000"),
            "100k" to BigDecimal("100000"),
            "1m + 1k" to BigDecimal("1001000"),

            // Scientific Notation + Unit Suffixes
            "1e3" to BigDecimal("1000"),
            "1.5E2" to BigDecimal("150.0"),
            "1e-2" to BigDecimal("0.01"),
            "1.2e2k" to BigDecimal("120000.0"),
            "5e-1k" to BigDecimal("500.0"),

            // Bitwise Shift Operations
            "1 << 10" to BigDecimal("1024"),
            "8 >> 2" to BigDecimal("2"),
            "1k << 1" to BigDecimal("2000"),
            "1.5 << 1" to BigDecimal("2"),
            "3.9 >> 1" to BigDecimal("1"),
            "255 >> 0" to BigDecimal("255"),

            // Complex Expressions
            "1.5k * 2 + 500" to BigDecimal("3500.0"),
            "(1k + 2k) / 3" to BigDecimal("1000"),
            "1m / 1k" to BigDecimal("1000"),
            "1g / 1m" to BigDecimal("1000"),
            "1 << 4 + 1" to BigDecimal("32"),
            "3 * 5m" to BigDecimal("15000000"),
            "100 * (2 + 1.2e2k / 60k)" to BigDecimal("400"),
            "100 * (-2) + 50k / (2 + 3)" to BigDecimal("9800"),
        )

        // cases from AE2
        val ae2cases = listOf(
            "1 + 2|3",
            "3 *4 |12",
            "1 + 2 * 3 |7",
            "1 - 6|-5",
//            "1/3|0.333333", // Disabled: higher precision supported
            "23.4 + 0.6|24",
            "1 - -4|5",
            "1 + 4*3*2|25",
            "1/0|failed",
            "1/(1 - 1)|failed",
            "3 + 2 * 4 - 1 /2|10.5",
            "1 + (2 * (2 * (1 + 1)))|9",
            "arkazkdhz|failed",
            "1 + 2 3 7 - 1|failed",
            "2 + + 2|failed",
//            "10e6|failed", // Disabled: scientific notation supported
            "-1 -1|-2",
            "- (1 + 1)|-2",
            "2 * -1|-2",
            "2 -2|0",
            "-  1|-1",
            "-1|-1",
            "- - - - - 5|-5",
            "-(-(-(-2)))|2",
            "1 - -1|2",
            "1 + -(2|failed",
            "NaN|failed",
            "1 / 0|failed",
            "64/4|16",
            "-2^2|-4",
            "2^2*3|12",
            "2^3.1|failed",
            "2^31|failed",
            "2^3^4|4096", // a bit unusual but acceptable
            "2^30^30^30|failed",
            "2^-1|failed",
        )

        var allPass = true
        for ((expr, expected) in testCases) {
            val result = runCatching { parse(expr, DecimalFormat("#.##########")) }.getOrElse {
                println("\u001B[31mERROR\u001B[0m: $expr -> Exception: ${it.message}")
                null
            }

            val pass = result != null && result.compareTo(expected) == 0
            allPass = allPass && pass

            if (pass) {
                println("\u001B[32mPASS\u001B[0m: $expr = $result")
            } else {
                println("\u001B[31mFAIL\u001B[0m: $expr -> Expected: $expected, Got: $result")
            }
        }

        for (case in ae2cases) {
            val parts = case.split("|")
            val expr = parts[0]
            val expectedStr = parts[1]
            val expected = if (expectedStr == "failed") null else BigDecimal(expectedStr)

            val result = runCatching { parse(expr, DecimalFormat("#.##########")) }.getOrElse {
                null
            }

            val pass = if (expected == null) {
                result == null
            } else {
                result != null && result.compareTo(expected) == 0
            }
            allPass = allPass && pass

            if (pass) {
                println("\u001B[32mPASS\u001B[0m: $expr = $result")
            } else {
                println("\u001B[31mFAIL\u001B[0m: $expr -> Expected: $expected, Got: $result")
            }
        }

        if (allPass) {
            println("\u001B[32mAll test cases passed!\u001B[0m")
        } else {
            println("\u001B[31mSome test cases failed.\u001B[0m")
        }

        if (!performance) return

        val expression = "1k * 2 + 500 - (300 / 2) + 4 ^ 3 - (1 << 5) + 1.5m / 3"
        val iterations = 1_000_000
        val startTime = System.currentTimeMillis()
        val format = DecimalFormat("#.##########")
        repeat(iterations) {
            parse(expression, format)
        }
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        println("Evaluated expression $iterations times in $duration ms")
        println("Average time per evaluation: ${duration * 1_000_000L / iterations} ns")
    }
}
