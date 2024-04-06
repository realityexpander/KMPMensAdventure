import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App(
           onLoadFinished = ::onLoadFinished,
//           setVideoCoordinates = ::setVideoCoordinates
        )
    }
}

external fun onLoadFinished()

//external fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int)
actual fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int) {
    // Call the JS function
      js("setVideoCoordinates(x, y, width, height)")
}

actual fun setVideoVisible(visible: Boolean) {
    // Call the JS function
    js("setVideoVisible(visible)")
}