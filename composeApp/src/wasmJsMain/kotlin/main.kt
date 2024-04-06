import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App(
           onLoadFinished = ::onLoadFinished,
        )
    }
}

external fun onLoadFinished()

//external fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int)  // calls JS function using ::setVideoCoordinates
actual fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int) {
   js("setVideoCoordinates(x, y, width, height)")
}

actual fun setVideoVisible(visible: Boolean) {
   js("setVideoVisible(visible)")
}

//external class GeolocationPosition
//{
//   val latitude: Double
//   val longitude: Double
//   val error: String
//}

//external fun getGpsLocation(callback: (GPSCoords) -> Unit)

//actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
//   js("getGpsLocation(callback)")
//}
//actual fun getGpsLocation(callback: (String) -> Unit) {
actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
   js("getGpsLocation(callback)")
}