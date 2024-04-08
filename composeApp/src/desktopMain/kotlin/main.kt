import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.function.Consumer


fun main() = application {
	Window(onCloseRequest = ::exitApplication, title = "KotlinProject") {
		App()
	}
}

@Preview
@Composable
fun AppPreview() {
	App()
}

actual fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int) {
	println("setVideoCoordinates($x, $y, $width, $height)")
}

actual fun setVideoVisible(visible: Boolean) {
	println("setVideoVisible($visible)")
}

actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
	callback(GeolocationPosition(40.7128, 74.0060, null))
}


private class StreamGobbler(
	private val inputStream: InputStream,
	private val consumer: Consumer<String>
) : Runnable {
	override fun run() {
		BufferedReader(InputStreamReader(inputStream)).lines()
			.forEach(consumer)
	}
}


// USING EXPECT/ACTUAL
actual class GeolocationPosition(
	val latitude: Double?,
	val longitude: Double?,
	val error: String?
) {
	constructor(error: String) : this(null, null, error)
}