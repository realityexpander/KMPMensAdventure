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

external fun onLoadFinished()  // ok to use `external` bc it's only called from this file

actual fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int) {
   js("setVideoCoordinates(x, y, width, height)")
}

actual fun setVideoVisible(visible: Boolean) {
   js("setVideoVisible(visible)")
}


// USING EXPECT/ACTUAL
actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
   js("getGpsLocation(callback)")
}
actual external class GeolocationPosition {
   val latitude: Double?
   val longitude: Double?
   val error: String?
}

// USING EXTERNAL
//actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
//   js("getGpsLocation(callback)")
//}
