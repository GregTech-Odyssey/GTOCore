package com.gtocore.api.ae2.gui

import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import java.text.ParsePosition
import java.util.*
import kotlin.math.pow

object AdvMathExpParser {
    private val UNIT_K = BigDecimal("1E3")
    private val UNIT_M = BigDecimal("1E6")
    private val UNIT_G = BigDecimal("1E9")
    private val UNIT_T = BigDecimal("1E12")

    private val UNITS = mapOf(
        'k' to UNIT_K,
        'm' to UNIT_M,
        'g' to UNIT_G,
        't' to UNIT_T,
    )

    private val PRECEDENCE = mapOf(
        "<<" to 1,
        ">>" to 1,
        "+" to 2,
        "-" to 2,
        "*" to 3,
        "/" to 3,
        "u" to 4, // unary minus
        "^" to 5,
    )

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
    @Throws(IllegalArgumentException::class)
    fun parse(expression: String, format: DecimalFormat? = null): BigDecimal {
        if (expression.isBlank()) return BigDecimal.ZERO

        val tokens = tokenize(expression, format)
        val rpn = shuntingYard(tokens)
        return evaluate(rpn)
    }

    private fun tokenize(expr: String, format: DecimalFormat?): List<Any> {
        val tokens = ArrayList<Any>()
        val len = expr.length
        var i = 0

        while (i < len) {
            val c = expr[i]

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
                        val nextC = expr[i]
                        val unitMultiplier = UNITS[nextC.lowercaseChar()]
                        if (unitMultiplier != null) {
                            valDecimal = valDecimal.multiply(unitMultiplier)
                            i++
                        }
                    }
                    tokens.add(valDecimal)
                }

                c == '(' || c == ')' -> {
                    tokens.add(c.toString())
                    i++
                }

                (c == '<' || c == '>') && (i + 1 < len && expr[i + 1] == c) -> {
                    tokens.add("$c$c")
                    i += 2
                }

                c == '-' -> {
                    // unary minus
                    if (tokens.isEmpty() || (tokens.last() is String && tokens.last() != ")")) {
                        tokens.add("u")
                    } else {
                        tokens.add(c.toString())
                    }
                    i++
                }

                c == '+' || c == '*' || c == '/' || c == '^' -> {
                    tokens.add(c.toString())
                    i++
                }

                else -> throw IllegalArgumentException("Unexpected character at index $i: '$c'")
            }
        }
        return tokens
    }

    private fun shuntingYard(tokens: List<Any>): List<Any> {
        val output = ArrayList<Any>()
        val stack = Stack<String>()

        for (token in tokens) {
            when (token) {
                is BigDecimal -> output.add(token)
                is String -> {
                    when (token) {
                        "(" -> stack.push(token)
                        ")" -> {
                            while (stack.isNotEmpty() && stack.peek() != "(") {
                                output.add(stack.peek())
                                stack.pop()
                            }
                            if (stack.isEmpty()) throw IllegalArgumentException("Mismatched parentheses")
                            stack.pop()
                        }
                        else -> {
                            while (stack.isNotEmpty() && stack.peek() != "(") {
                                val top = stack.peek()
                                if ((PRECEDENCE[top] ?: 0) >= (PRECEDENCE[token] ?: 0)) {
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
            if (top == "(") throw IllegalArgumentException("Mismatched parentheses")
            output.add(top)
        }
        return output
    }

    private fun evaluate(rpn: List<Any>): BigDecimal {
        val stack = Stack<BigDecimal>()

        val mc = MathContext.DECIMAL128

        for (token in rpn) {
            if (token is BigDecimal) {
                stack.push(token)
            } else {
                val op = token as String

                // Unary minus
                if (op == "u") {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid expression: missing operand for unary minus")
                    val b = stack.pop()
                    stack.push(b.negate(mc))
                    continue
                }

                if (stack.size < 2) throw IllegalArgumentException("Invalid expression")
                val b = stack.pop()
                val a = stack.pop()

                val res = when (op) {
                    "+" -> a.add(b, mc)
                    "-" -> a.subtract(b, mc)
                    "*" -> a.multiply(b, mc)
                    "/" -> a.divide(b, mc)
                    "^" -> {
                        try {
                            a.pow(b.intValueExact(), mc)
                        } catch (_: ArithmeticException) {
                            BigDecimal(a.toDouble().pow(b.toDouble()), mc)
                        }
                    }
                    "<<" -> {
                        val bi = a.toBigInteger()
                        val shift = b.toInt()
                        BigDecimal(bi.shiftLeft(shift))
                    }
                    ">>" -> {
                        val bi = a.toBigInteger()
                        val shift = b.toInt()
                        BigDecimal(bi.shiftRight(shift))
                    }
                    else -> throw IllegalArgumentException("Unknown operator: $op")
                }
                stack.push(res)
            }
        }

        if (stack.size != 1) throw IllegalArgumentException("Invalid expression result")
        return stack.pop()
    }

    @JvmOverloads
    fun test(performance: Boolean = false) {
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
            "4 ^ 0.5" to BigDecimal("2.0"),

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

        var allPass = true
        for ((expr, expected) in testCases) {
            val result = runCatching { parse(expr, DecimalFormat("#.##########")) }.getOrNull()

            val pass = result != null && result.compareTo(expected) == 0
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

        val expression = "1k * 2 + 500 - (300 / 2) + 4 ^ 3 - 1 << 5 + 1.5m / 3"
        val iterations = 1_000_000
        val startTime = System.currentTimeMillis()
        repeat(iterations) {
            parse(expression, DecimalFormat("#.##########"))
        }
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        println("Evaluated expression $iterations times in $duration ms")
        println("Average time per evaluation: ${duration * 1_000_000L / iterations} ns")
    }
}
