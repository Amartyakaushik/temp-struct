package com.example.worka1.utils

fun formatNumber(value: Int): String {
    return when {
        value >= 1_000_000_000 -> formatWithSuffix(value, 1_000_000_000, "B")
        value >= 1_000_000 -> formatWithSuffix(value, 1_000_000, "M")
        value >= 1_000 -> formatWithSuffix(value, 1_000, "K")
        else -> value.toString()
    }
}

private fun formatWithSuffix(value: Int, divisor: Int, suffix: String): String {
    val result = value.toDouble() / divisor
    return if (result % 1.0 == 0.0) {
        String.format("%.0f%s", result, suffix)
    } else {
        String.format("%.1f%s", result, suffix)
    }
}
