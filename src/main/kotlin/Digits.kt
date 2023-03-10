import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.size
import kotlin.random.Random
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.ggplot
import org.jetbrains.letsPlot.label.labs
import org.jetbrains.letsPlot.scale.scaleFillManual
import smile.manifold.TSNE
import util.scaleData
import util.ToDataFrame
import java.io.File
import java.io.InputStreamReader

fun main() {
    val digits = ToDataFrame("src/main/kotlin/file/digits/digits.csv")

    val numRows = digits.df.count()
    val randomIndices = List(3000) { Random.nextInt(numRows) }.distinct().toSet()
    digits.df = digits.df.filter { row -> row.index() in randomIndices }

    // println(digits.df)
    val label = digits.df["Digit"].map { it.toString() }.toTypedArray()
    digits.removeColumn("Digit")
    for (columnName in digits.df.columnNames()) {
        digits.df = digits.df.convert(columnName).toDouble()
    }
    // println(digits.toArrayOfArrays().contentDeepToString())
    val features = digits.toArrayOfArrays().map { row -> row.copyOfRange(0, 16).map { it as Double }.toDoubleArray() }

    for (idx in 0..9) {

        val perplexity = 5.0 * (idx + 1)
        val eta = (digits.df.size().nrow / 12).toDouble() // recommend setting the learning rate to n/12
        val iterations = 1000

        val digitsFill = listOf("#fff100", "#ff8c00", "#e81123", "#ec008c", "#68217a", "#00188f", "#00bcf2", "#00b294", "#009e49", "#bad80a")
            .zip(listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")).toMap()

        smile.math.MathEx.setSeed(2023)
        val featuresScaled = scaleData(features.toTypedArray(), "min-max")
        val tsne = TSNE(featuresScaled, 2, perplexity, eta, iterations)
        // println(tsne.coordinates.contentDeepToString())
        val data = mapOf(
            "x" to tsne.coordinates.map { it[0] },
            "y" to tsne.coordinates.map { it[1] },
            "Digit" to label
        )
        val p = ggplot() +
                geomPoint(data = data , shape = 21, size = 1.75, color = "black") { x = "x"; y = "y"; fill = "Digit" } +
                org.jetbrains.letsPlot.coord.coordFixed(
                    ratio = 1,
                    xlim = Pair(-85, 85),
                    ylim = Pair(-85, 85)
                ) +
                labs(
                    title = "perplexity: $perplexity",
                    subtitle = "eta: $eta, iterations: $iterations"
                ) +
                scaleFillManual(
                    name = "Digit",
                    values = digitsFill.keys.toList(),
                    breaks = digitsFill.values.toList()
                )
        ggsave(p, "digits${idx}.png", 9.99, 600, "src/main/kotlin/file/digits")
    }

    val pythonFile = File("src/main/python/digits.py")
    val processBuilder = ProcessBuilder("python", pythonFile.absolutePath)
    val process = processBuilder.start()
    // Get the output of the Python process
    val reader = InputStreamReader(process.inputStream)
    val output = reader.readText()
    // Wait for the process to finish
    val exitCode = process.waitFor()
    // Print the output and exit code
    println("Output: $output")
    println("Exit code: $exitCode")
}