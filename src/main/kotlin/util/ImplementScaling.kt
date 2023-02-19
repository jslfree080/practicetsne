package util

import kotlin.math.*

fun scaleData(data: Array<DoubleArray>, method: String): Array<DoubleArray> {
    val scaledData = Array(data.size) { DoubleArray(data[0].size) }

    for (i in data[0].indices) {
        val column = data.map { it[i] }.toDoubleArray()
        when (method) {
            "standardization" -> {
                val mean = column.average()
                val std = column.stdDev()
                for (j in scaledData.indices) {
                    scaledData[j][i] = (data[j][i] - mean) / std
                }
            }
            "min-max" -> {
                val min = column.minOrNull() ?: 0.0
                val max = column.maxOrNull() ?: 1.0
                val range = max - min
                for (j in scaledData.indices) {
                    scaledData[j][i] = (data[j][i] - min) / range
                }
            }
            "log" -> {
                for (j in scaledData.indices) {
                    scaledData[j][i] = ln(data[j][i])
                }
            }
            else -> throw IllegalArgumentException("Invalid scaling method: $method")
        }
    }

    return scaledData
}

fun DoubleArray.stdDev(): Double {
    val mean = average()
    var sum = 0.0
    for (i in indices) {
        sum += (this[i] - mean) * (this[i] - mean)
    }
    return sqrt(sum / size)
}