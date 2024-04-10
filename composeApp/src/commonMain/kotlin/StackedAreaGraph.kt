import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.line.*
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.KoalaPlotTheme
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

/**
 * Created by Chris Athanas on 4/4/24.
 */

interface SampleView {
	val name: String
	val thumbnail: @Composable () -> Unit
	val content: @Composable () -> Unit
}

internal val padding = 8.dp
internal val paddingMod = Modifier.padding(padding)

/**
 * Sets the KoalaPlotTheme with values used for Thumbnail chart views.
 */
@Composable
fun ThumbnailTheme(content: @Composable () -> Unit) {
	KoalaPlotTheme(
		axis = KoalaPlotTheme.axis.copy(
			majorTickSize = 0.dp,
			minorTickSize = 0.dp,
			majorGridlineStyle = null,
			minorGridlineStyle = null
		),
		content = content
	)
}

val stackedAreaSampleView = object : SampleView {
	override val name: String = "Stacked Area Chart"

	override val thumbnail = @Composable {
		ThumbnailTheme {
			StackedAreaSample(true, name)
		}
	}

	override val content: @Composable () -> Unit = @Composable {
		StackedAreaSample(false, "Your Stats Over Time")
	}
}

@Suppress("MagicNumber")
private val colorPalette = listOf(
	Color(0xFF00498F),
	Color(0xFF37A78F),
	Color(0xFFC05050),
	Color(0xFFED7D31),
	Color(0xFF8068A0),
	Color(0xFF9BBB59),
	Color(0xFFA5A5A5),
	Color(0xFFFFC000),
	Color(0xFF4472C4),
)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
@Suppress("MagicNumber")
fun StackedAreaSample(thumbnail: Boolean, title: String) {
	ChartLayout(
		modifier = paddingMod
			.height(800.dp)
		,
		title = { ChartTitle(title) },
		legendLocation = LegendLocation.BOTTOM
	) {
		XYGraph(
			xAxisModel = LinearAxisModel(
				GameData.years.first().toFloat()..GameData.years.last().toFloat(),
				minimumMajorTickIncrement = 1F
			),
			yAxisModel =  LinearAxisModel(0f..GameData.maxOfSumOfAllYears.toFloat(), minimumMajorTickIncrement = 10F),
			horizontalMajorGridLineStyle = null,
			horizontalMinorGridLineStyle = null,
			verticalMajorGridLineStyle = null,
			verticalMinorGridLineStyle = null,
			xAxisLabels = {
				if (!thumbnail) {
					AxisLabel(it.toString(0), Modifier.padding(top = 2.dp))
				}
			},
			xAxisTitle = {
				if (!thumbnail) {
					Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
						AxisTitle("Year")
					}
				}
			},
			yAxisLabels = {
				if (!thumbnail) {
					AxisLabel(it.toString(0))
				}
			},
			yAxisTitle = {
				if (!thumbnail) {
					Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
						AxisTitle(
							"Points",
							modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
						)
					}
				}
			},
			panZoomEnabled = false
		) {
			StackedAreaPlot(
				stackedAreaData,
				colorPalette.map {
					StackedAreaStyle(
						LineStyle(brush = SolidColor(Color.White), strokeWidth = 4.dp),
						AreaStyle(brush = SolidColor(it))
					)
				},
				AreaBaseline.ConstantLine<Float, Float>(0F)
			)

			annotations(thumbnail)
		}
	}
}

@Suppress("MagicNumber")
@Composable
private fun XYGraphScope<Float, Float>.annotations(thumbnail: Boolean) {
	if (!thumbnail) {
		val entries = GameData.data.entries.toList()
		val max = entries.map { it.value.max() }
		val maxIndices = entries.mapIndexed { index, entry ->
			entry.value.indexOfFirst { it == max[index] }
		}

		entries.forEachIndexed { index, (category, data) ->
			val yearIndex = maxIndices[index] // index into the year the max occurred

			var sum = 0
			for (i in 0..<index) {
				sum += entries[i].value[yearIndex]
			}

			val anchorPoint = when (yearIndex) {
				0 -> AnchorPoint.LeftMiddle
				GameData.years.lastIndex -> AnchorPoint.RightMiddle
				else -> AnchorPoint.Center
			}

			XYAnnotation(
				Point(GameData.years[yearIndex].toFloat(), (sum + data[yearIndex] / 2f)),
				anchorPoint
			) {
				Text(
					category,
					style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
					fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
					color = Color.DarkGray,
					modifier = Modifier.padding(horizontal = KoalaPlotTheme.sizes.gap)
						.background(Color.LightGray, RoundedCornerShape(4.dp))
						.padding(horizontal = KoalaPlotTheme.sizes.gap)
				)
			}
		}
	}
}

private val stackedAreaData: List<StackedAreaPlotEntry<Float, Float>> by lazy {
	StackedAreaPlotDataAdapter(
		GameData.years.map { it.toFloat() },
		GameData.data.values.map {
			it.map { it: Int ->
				it.toFloat()
			}
		}
	)
}

internal object GameData {
	val years = mutableListOf(0, 1, 2, 3)
	val data = buildMap {
		GameState.keys.forEachIndexed { index, _ ->
			put(
				GameState.keys[index], // label for each item: age, dollars, etc.
				listOf( // data for each item for each year
					Random.nextInt(0, 100),
					Random.nextInt(0, 100),
					Random.nextInt(0, 100),
					Random.nextInt(0, 100),
				)
			)
		}
	}

	val maxOfSumOfAllYears = years.maxOf { year ->
		data.values.sumOf { it[year] }
	}
}

@Composable
fun ChartTitle(title: String) {
	Column {
		Text(
			title,
			color = MaterialTheme.colorScheme.onBackground,
			style = MaterialTheme.typography.bodySmall,
			modifier = Modifier.align(Alignment.CenterHorizontally)
		)
	}
}

@Composable
fun AxisTitle(title: String, modifier: Modifier = Modifier) {
	Text(
		title,
		color = MaterialTheme.colorScheme.onBackground,
		style = MaterialTheme.typography.bodySmall,
		modifier = modifier
	)
}

@Composable
fun AxisLabel(label: String, modifier: Modifier = Modifier) {
	Text(
		label,
//		color = MaterialTheme.colors.onBackground,
		color = MaterialTheme.colorScheme.error,
		style = MaterialTheme.typography.bodySmall,
		modifier = modifier,
		overflow = TextOverflow.Ellipsis,
		maxLines = 1
	)
}

@Preview
@Composable
fun StackedAreaSamplePreview() {
	//StackedAreaSample(true, "Your Stats Over Time")
	Text("StackedAreaSamplePreview")
}