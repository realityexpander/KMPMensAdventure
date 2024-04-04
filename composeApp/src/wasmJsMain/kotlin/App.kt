import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLStyleElement

data class GameState(
	val age: Int= 0,
	val name: String = "Player",
	val dollars: Int = 0,
	val health: Int = 100,
	val happiness: Int = 50,
	val sexualHappiness: Int = 0,
	val emotionalHappiness: Int = 50,
	val skill: Int = 0,
	val muscularity: Int = 0,
)

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class, ExperimentalKoalaPlotApi::class)
@Composable
fun App(
	onLoadFinished: () -> Unit
) {

//	browserViewportWindow("test") {
		MaterialTheme(
			typography = Typography(
				FontFamily.Default,
				body1 = TextStyle(
					fontFamily = FontFamily.Default,
					fontSize = 18.sp,
					color = Color.Black
				),
			),
		) {
			var showContent by remember { mutableStateOf(false) }
			val scope = rememberCoroutineScope()
			var state = MutableStateFlow<GameState>(GameState())

//			Text("Click me!")

//		Column(
//			Modifier.fillMaxWidth(),
//			horizontalAlignment = Alignment.CenterHorizontally
//		) {
//			Button(onClick = { showContent = !showContent }) {
//				Text("Click me!")
//			}
//			AnimatedVisibility(showContent) {
//				val greeting = remember { Greeting().greet() }
//				TopAppBar(title = { Text("ComposeApp") })
//				Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//					Image(painterResource(Res.drawable.compose_multiplatform), null)
//					Text("Compose: $greeting")
//
//					FloatingActionButton(onClick = { showContent = !showContent }) {
//						Icon(painterResource(Res.drawable.compose_multiplatform), null)
//					}
//
//				}
//			}
//		}

			val scaffoldState = rememberBottomSheetScaffoldState()
			val drawerState = rememberDrawerState(DrawerValue.Closed)

			LaunchedEffect(Unit) {
				delay(1000)
				onLoadFinished()
			}

			ModalDrawer(
				modifier = Modifier.fillMaxSize(),
				drawerContent = {
					Text("Drawer content")
				},
				gesturesEnabled = true,
				drawerState = drawerState,
			) {
				BottomSheetScaffold(
					modifier = Modifier.fillMaxSize(),
					sheetContent = {
						Column {
							Text("Sheet content")
							Button(onClick = { scope.launch { scaffoldState.bottomSheetState.collapse() } }) {
								Text("Close")
							}
						}
					},
					sheetPeekHeight = 0.dp,
					sheetElevation = 8.dp,
					scaffoldState = scaffoldState,
					sheetShape = MaterialTheme.shapes.large,
					sheetBackgroundColor = MaterialTheme.colors.surface,
					sheetContentColor = MaterialTheme.colors.onSurface,
					sheetGesturesEnabled = true,
					floatingActionButton = {
						FloatingActionButton(
							onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } },
							modifier = Modifier.size(48.dp),
						) {
							Icon(
								painterResource(Res.drawable.compose_multiplatform),
								null
							)
						}
					},
					floatingActionButtonPosition = FabPosition.End,
					topBar = {
						TopAppBar(
							title = { Text("ComposeApp") },
							navigationIcon = {
								IconButton(onClick = { scope.launch { drawerState.open() } }) {
									Icon(Icons.Default.Menu, null)
								}
							},
							actions = {
								IconButton(onClick = { scope.launch { drawerState.open() } }) {
									Icon(painterResource(Res.drawable.compose_multiplatform), null)
								}
							}
						)
					},
					content = {
						val greeting = remember { Greeting().greet() }
						Column(
							Modifier.fillMaxWidth(),
							horizontalAlignment = Alignment.Start
						) {
							//Image(painterResource(Res.drawable.compose_multiplatform), null, modifier = Modifier.size(128.dp) )
							//Text("Compose: $greeting")

							//Text("currentUrl: ${window.location.href}")
							val screenRatio = window.screen.height / window.screen.width.toFloat()
							val scale = screenRatio / window.devicePixelRatio
							val innerRatio = window.innerHeight / window.innerWidth.toFloat()
							val outerRatio = window.outerHeight / window.outerWidth.toFloat()

							//Spacer(Modifier.height(260.dp))

							Text("InnerWidth x Height: ${window.innerWidth} x ${window.innerHeight}")
							Text("ScreenWidth x Height: ${window.screen.width} x ${window.screen.height}")
							Text("Pixel ratio: ${window.devicePixelRatio}")
							Text("Screen h/w ratio: $screenRatio")
							Text("Scale: $scale")
							Text("InnerHeight/InnerWidth: $innerRatio")
							Text("OuterHeight/OuterWidth: $outerRatio")
							Text("OuterRatio pixelRatio: ${outerRatio / window.devicePixelRatio}")
							Text("parent. innerWidth x innerHeight: ${window.parent.innerWidth} x ${window.parent.innerHeight}")
							Text("parent. outerWidth x outerHeight: ${window.parent.outerWidth} x ${window.parent.outerHeight}")
							Text("window.pageOffset: ${window.pageXOffset} x ${window.pageYOffset}")
							Text("window.screen.availWidth x availHeight: ${window.screen.availWidth} x ${window.screen.availHeight}")
							Text("window.screen.pixelDepth: ${window.screen.pixelDepth}")
							Text("window.outerWidth x outerHeight: ${window.outerWidth} x ${window.outerHeight}")

//							BulletGraph {
//								label {
//									Column(
//										horizontalAlignment = Alignment.End,
//										modifier = Modifier.padding(end = KoalaPlotTheme.sizes.gap)
//									) {
//										Text(
//											"Revenue 2005 YTD",
//											textAlign = TextAlign.End
//										)
//										Text(
//											"(US $ in thousands)",
//											textAlign = TextAlign.End
//										)
//									}
//								}
//								axis { labels { "${it.toInt()}" } }
//								comparativeMeasure(260f)
//								featuredMeasureBar(275f)
//								ranges(
//									0f,
//									200f,
//									250f,
//									300f
//								)
//							}

//							LazyColumn {
//								items(100) { index ->
//									Text("Item $index")
//								}
//							}
						}
					},
				)
			}

		}
	}






private const val CANVAS_ELEMENT_ID = "ComposeTarget" // Hardwired into ComposeWindow

/**
 * A Skiko/Canvas-based top-level window using the browser's entire viewport. Supports resizing.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun browserViewportWindow(
	title: String = "Untitled",
	content: @Composable () -> Unit
) {
	val htmlHeadElement = document.head!!
	htmlHeadElement.appendChild(
		(document.createElement("style") as HTMLStyleElement).apply {
			type = "text/css"
			appendChild(
				document.createTextNode(
					"""
                    html, body {
                        overflow: hidden;
                        margin: 0 !important;
                        padding: 0 !important;
								font-size: 250px;
                    }

                    #$CANVAS_ELEMENT_ID {
                        outline: none;
                    }
                    """.trimIndent()
				)
			)
		}
	)

	fun HTMLCanvasElement.fillViewportSize() {
		setAttribute("width", "${window.innerWidth}")
		setAttribute("height", "${window.innerHeight}")
	}

	val canvas = (document.getElementById(CANVAS_ELEMENT_ID) as HTMLCanvasElement).apply {
		fillViewportSize()
	}

	CanvasBasedWindow(
		title = title,
		canvasElementId = CANVAS_ELEMENT_ID,
		applyDefaultStyles = true,

	) {
		content()
	}
//	ComposeWindow(
//		canvasId = CANVAS_ELEMENT_ID,
//		content = content
//	)
//	.apply {
//		window.addEventListener("resize") {
//			canvas.fillViewportSize()
//			layer.layer.attachTo(canvas)
//			val scale = layer.layer.contentScale
//			layer.setSize((canvas.width / scale).toInt(), (canvas.height / scale).toInt())
//			layer.layer.needRedraw()
//		}
//
//		// WORKAROUND: ComposeWindow does not implement `setTitle(title)`
//		val htmlTitleElement = (
//				htmlHeadElement.getElementsByTagName("title").item(0)
//					?: document.createElement("title").also { htmlHeadElement.appendChild(it) }
//				) as HTMLTitleElement
//		htmlTitleElement.textContent = title
//	}

}
