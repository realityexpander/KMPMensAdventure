import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.hamama.kwhi.HtmlView
import com.hamama.kwhi.LocalLayerContainer
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
   CanvasBasedWindow(
      canvasElementId = "ComposeTarget",
      title = "Kotlin Wasm Html Interop",
   ) {
      CompositionLocalProvider(LocalLayerContainer provides document.body!!) { // For HtmlView
         App(
            onLoadFinished = ::onLoadFinished,
         )
      }
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

@Composable
actual fun HtmlVideoView(
   modifier: Modifier,
) {
   HtmlView(
      modifier = modifier.fillMaxWidth().height(300.dp),
      factory = {
         val video = document.createElement(
            "video"
         )
         video.setAttribute(
            "src",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
         )
         video.setAttribute("controls", "true")
         video.setAttribute("controlsList", "nofullscreen")

         video
      }
   )
}

@Composable
actual fun HtmlImageView(
   modifier: Modifier,
   imageUrl: String
) {
   HtmlView(
      modifier = Modifier.fillMaxWidth().height(300.dp),
      factory = {
         val img = document.createElement("img")
         img.setAttribute(
            "src",
            imageUrl
         )
         img
      }
   )
}

// USING EXTERNAL
//actual fun getGpsLocation(callback: (GeolocationPosition) -> Unit) {
//   js("getGpsLocation(callback)")
//}
