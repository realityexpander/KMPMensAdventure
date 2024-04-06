import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

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

//external fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int)
actual fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int) {
    println("setVideoCoordinates($x, $y, $width, $height)")
}

actual fun setVideoVisible(visible: Boolean) {
  println("setVideoVisible($visible)")
}

actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
//actual fun getGpsLocation(callback: (String) -> Unit) {
//    callback(GeolocationPosition(37.7749, -122.4194, ""))
//    callback("37.7749, -122.4194")
}