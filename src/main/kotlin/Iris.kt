import smile.manifold.TSNE
import org.jetbrains.kotlinx.dataframe.api.toTypedArray
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.ggplot
import org.jetbrains.letsPlot.label.labs
import java.io.File
import java.io.InputStreamReader
import util.ToDataFrame

fun main() {
    val iris = ToDataFrame("src/main/kotlin/file/iris/iris.csv")
    // println(iris.df)
    val label = iris.df["Species"].toTypedArray()
    iris.removeColumn("Species")
    // println(iris.toArrayOfArrays().contentDeepToString())
    val features = iris.toArrayOfArrays().map { row -> row.copyOfRange(0, 4).map { it as Double }.toDoubleArray() }

    for (idx in 0..9) {

        val perplexity = 5.0 * (idx + 1)
        val eta = 200.0
        val iterations = 1000

        smile.math.MathEx.setSeed(2023)
        val tsne = TSNE(features.toTypedArray(), 2, perplexity, eta, iterations)
        // println(tsne.coordinates.contentDeepToString())
        val data = mapOf(
            "x" to tsne.coordinates.map { it[0] },
            "y" to tsne.coordinates.map { it[1] },
            "Species" to label
        )
        val p = ggplot() +
                geomPoint(data = data , shape = 16, size = 1.75) { x = "x"; y = "y"; color = "Species" } +
                org.jetbrains.letsPlot.coord.coordFixed(
                    ratio = 1,
                    xlim = Pair(-35, 35),
                    ylim = Pair(-35, 35)
                ) +
                labs(
                    title = "perplexity: $perplexity",
                    subtitle = "eta: $eta, iterations: $iterations"
                )
        ggsave(p, "iris${idx}.png", 9.99, 600, "src/main/kotlin/file/iris")
    }

    val pythonFile = File("src/main/python/iris.py")
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