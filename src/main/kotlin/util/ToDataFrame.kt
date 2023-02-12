package util

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.read

class ToDataFrame(pathToData: String) {
    var df = DataFrame.read(pathToData)

    fun removeColumn(columnName: String) {
        df = df.remove(columnName)
    }
    fun toArrayOfArrays(): Array<Array<Any?>> {
        return df.rows().map { row -> row.values().toList().toTypedArray() }.toTypedArray()
    }
}