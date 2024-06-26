import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

data class GameState(
	val age: Int = 0,
	val name: String = "Player",
	val dollars: Int = 0,
	val health: Int = 100,
	val securityHappiness: Int = 50,
	val sexualHappiness: Int = 0,
	val emotionalHappiness: Int = 50,
	val skill: Int = 0,
	val muscularity: Int = 0,
) {
	companion object {
		val keys = listOf(
			"age",
			"name",
			"dollars",
			"health",
			"securityHappiness",
			"sexualHappiness",
			"emotionalHappiness",
			"skill",
			"muscularity",
		)
	}
}

@OptIn(ExperimentalResourceApi::class, ExperimentalKoalaPlotApi::class, ExperimentalMaterial3Api::class,
	ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalCoilApi::class
)
@Composable
fun App(
	onLoadFinished: () -> Unit = {},
) {

	val windowSizeClass = calculateWindowSizeClass()

	MaterialTheme(
		typography = Typography(
			bodySmall = TextStyle(
				fontFamily = FontFamily.Default,
				fontSize = 18.sp,
				color = Color.White
			),
		),
		colorScheme = MaterialTheme.colorScheme.copy(
			onBackground = Color.White,
			background = Color.DarkGray,
		)
	) {
		val scope = rememberCoroutineScope()
		val state = MutableStateFlow<GameState>(GameState())

		val scaffoldState = rememberBottomSheetScaffoldState()
		val drawerState = rememberDrawerState(DrawerValue.Closed)
		val modalBottomSheetState = rememberModalBottomSheetState()
		var showBottomSheet by remember { mutableStateOf(false) }

		val url by remember { mutableStateOf("https://www.youtube.com/embed/Y7rSvV6caVQ") }
//		var gpsLocation by remember { mutableStateOf(GeolocationPosition(37.7749, -122.4194, "")) }
		var gpsLocation by remember { mutableStateOf("") }

		LaunchedEffect(Unit) {
			delay(100)
			onLoadFinished()
		}

		// Hide the video when the drawer is open
		LaunchedEffect(drawerState.isOpen) {
			//setVideoVisible(drawerState.currentValue == DrawerValue.Closed)
		}

		val focusRequester = remember { FocusRequester() }
		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}

		val loadingState = MutableStateFlow("Idle.")

		ModalNavigationDrawer(
			modifier = Modifier.background(MaterialTheme.colorScheme.background)
				.focusRequester(focusRequester)
				.focusTarget().onKeyEvent {
					if (it.type == KeyEventType.KeyDown) {
						when(it.key) {
							Key.Tab -> {
								scope.launch {
									if (drawerState.isOpen) {
										drawerState.close()
									} else {
										drawerState.open()
									}
								}
								true
							}
							Key.Escape -> {
								scope.launch {
									if (modalBottomSheetState.isVisible) {
										modalBottomSheetState.hide()
									}
									if (drawerState.isOpen) {
										drawerState.close()
									}
								}
								true
							}
							else -> false
						}
					} else {
						false
					}
				}
			,
			drawerContent = {
				ModalDrawerSheet {
					Text("Drawer title", modifier = Modifier.padding(16.dp))
					HorizontalDivider()
					NavigationDrawerItem(
						label = { Text(text = "Drawer Item") },
						selected = false,
						onClick = { /*TODO*/ }
					)
				}
			},
			gesturesEnabled = true,
			drawerState = drawerState,
		) {
			val stateVertical = rememberScrollState(0)

			Scaffold {

				Column {
					TopAppBar(
						title = { Text("ComposeApp") },
						navigationIcon = {
							IconButton(onClick = {
								scope.launch {
									//setVideoVisible(false)
									drawerState.open()
								}
							}) {
								Icon(Icons.Default.Menu, null)
							}
						},
						actions = {
							IconButton(onClick = { scope.launch {
								showBottomSheet = true
								println("hello")
							} }) {
								Icon(painterResource(Res.drawable.compose_multiplatform), null)
							}
						}
					)
          		val file = "https://raw.githubusercontent.com/realityexpander/FredsRoadtripStoryteller/main/raw_assets/screenshots/final_app/02L-MainScreen-50.png"
//					val file = "https://wsrv.nl/?url=https://img.youtube.com/vi/aov6J8Bd3hs/hqdefault.jpg"
					when(windowSizeClass.widthSizeClass) {
						WindowWidthSizeClass.Compact -> {
							Box {
								Column(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp)
										.verticalScroll(state = stateVertical)
								) {
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")
									Text("Compact")

									//HtmlImageView(modifier = Modifier.fillMaxWidth(), imageUrl = file)

									val painter = rememberAsyncImagePainter(file)
									Image(
										painter = painter,
										contentDescription = "image",
										modifier = Modifier.fillMaxWidth()
											.aspectRatio(4f / 3f)
											.background(Color.Gray)
									)

									AsyncImage(
										file,
										contentDescription = "image",
										modifier = Modifier.fillMaxWidth()
											.aspectRatio(4f / 3f)
											.background(Color.Gray),
										onState = { state ->
											when (state) {
												is AsyncImagePainter.State.Loading -> {
													loadingState.update {
														"$it\nLoading..."
													}
												}

												is AsyncImagePainter.State.Success -> {
													loadingState.update { "$it\nSuccess!" }
												}

												is AsyncImagePainter.State.Error -> {
													loadingState.update { "$it\nError: ${state.result.throwable}" }
												}

												else -> {
													loadingState.update { "$it\nIdle." }
												}
											}
										}
									)
									Text(loadingState.collectAsState().value)

									//VideoView(modifier = Modifier.fillMaxWidth())
								}

								VerticalScrollbar(
									modifier = Modifier
										.align(Alignment.CenterEnd)
										.fillMaxHeight(),
									adapter = rememberScrollbarAdapter(stateVertical)
								)
							}
						}
							else -> {
								Column(
									modifier = Modifier.verticalScroll(state = rememberScrollState())
								) {
									Row(
										modifier = Modifier
											.fillMaxWidth()
											.padding(16.dp)
									) {
										Text("Medium")
										Text("Medium")
									}
									Text("Medium")
									Text("Medium")
									Text("Medium")
									Text("Medium")
									Text("Medium")
									Text("Medium")
									Text("Medium")
									Text("Medium")
									Text("Medium")

									//VideoView(modifier = Modifier.fillMaxWidth())

									AsyncImage(
//										"https://github.com/realityexpander/FredsRoadtripStoryteller/blob/main/screenshots/run-configurations.png",
										ImageRequest.Builder(LocalPlatformContext.current)
											.data(file)
											.build()
										,
										contentDescription = "image",
										modifier = Modifier.fillMaxWidth()
											.aspectRatio(4f / 3f)
											.background(Color.Gray)
									)


								}
							}
						}

					if (showBottomSheet) {
						ModalBottomSheet(
							onDismissRequest = {
								scope.launch {
									modalBottomSheetState.hide()
								}.invokeOnCompletion {
									if (!modalBottomSheetState.isVisible) {
										showBottomSheet = false
									}
								}
							},
						) {
							Column {
								Text("Sheet content")
								Button(
								onClick = { scope.launch { showBottomSheet = false } } )
								{
									Text("Close")
								}
							}
						}
					}
				}
			}
		}


//		ModalDrawer(
//			modifier = Modifier
//				.fillMaxSize(),
//			drawerContent = {
//				Text("Drawer content")
//			},
//			gesturesEnabled = true,
//			drawerState = drawerState,
//		) {
//			BottomSheetScaffold(
//				modifier = Modifier.fillMaxSize(),
//				sheetContent = {
//					Column {
//						Text("Sheet content")
//						Button(onClick = { scope.launch { scaffoldState.bottomSheetState.collapse() } }) {
//							Text("Close")
//						}
//					}
//				},
//				sheetPeekHeight = 0.dp,
//				sheetElevation = 8.dp,
//				scaffoldState = scaffoldState,
//				sheetShape = MaterialTheme.shapes.large,
//				sheetBackgroundColor = MaterialTheme.colors.surface,
//				sheetContentColor = MaterialTheme.colors.onSurface,
//				sheetGesturesEnabled = false,
//				floatingActionButton = {
//					FloatingActionButton(
//						onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } },
//						modifier = Modifier.size(48.dp),
//					) {
//						Icon(
//							painterResource(Res.drawable.compose_multiplatform),
//							null
//						)
//					}
//				},
//				floatingActionButtonPosition = FabPosition.End,
//				topBar = {
//					TopAppBar(
//						title = { Text("ComposeApp") },
//						navigationIcon = {
//							IconButton(onClick = { scope.launch {
//								setVideoVisible(false)
//								drawerState.open()
//							} }) {
//								Icon(Icons.Default.Menu, null)
//							}
//						},
//						actions = {
//							IconButton(onClick = { scope.launch { drawerState.open() } }) {
//								Icon(painterResource(Res.drawable.compose_multiplatform), null)
//							}
//						}
//					)
//				},
//				content = {
//					Column(
//						Modifier
//							.fillMaxSize()
//							.background(Color.DarkGray)
//							.padding(16.dp)
//							.verticalScroll(state= rememberScrollState())
//						,
//
//						horizontalAlignment = Alignment.Start
//					) {
//						//Image(painterResource(Res.drawable.compose_multiplatform), null, modifier = Modifier.size(128.dp) )
//						//Text("Compose: $greeting")
//
//						ShowState(state)
//
//						Button(
//							onClick = {
//								state.update {
//									it.copy(
//										age = it.age + 1,
//										dollars = it.dollars - 100,
//										health = it.health - 10,
//										securityHappiness = it.securityHappiness - 10,
//										emotionalHappiness = it.emotionalHappiness + 1
//									)
//								}
//							},
//							// Find the coordinates of this button
//							modifier = Modifier.onGloballyPositioned { coordinates ->
//								setVideoCoordinates(
//									coordinates.localToWindow(Offset.Zero).x.toInt(),
//									coordinates.localToWindow(Offset.Zero).y.toInt(),
//									coordinates.size.width,
//									coordinates.size.height
//								)
//							}
//						) {
//							Text("Do Nothing")
//						}
//
//						Button(
//							onClick = {
//								state.update {
//									it.copy(
//										age = it.age + 1,
//										dollars = it.dollars + 100,
//										health = it.health + 10,
//										skill = it.skill + 5
//									)
//								}
//							}
//						) {
//							Text("Work")
//						}
//
//						Button(
//							onClick = {
//								state.update {
//									it.copy(
//										age = it.age + 1,
//										dollars = it.dollars - 100,
//										health = it.health - 10,
//										securityHappiness = it.securityHappiness + 5,
//										emotionalHappiness = it.emotionalHappiness + 5
//									)
//								}
//							}
//						) {
//							Text("Go to a party")
//						}
//
//						Button(
//							onClick = {
//								state.update {
//									it.copy(
//										age = it.age + 1,
//										dollars = it.dollars - 200,
//										health = it.health - 10
//									)
//								}
//
//							}
//						) {
//							Text("Go on to School")
//						}
//
//						Button(
//							onClick = {
//								scope.launch {
//									getGpsLocation {
//										gpsLocation = "Lat: ${it.latitude}, Long: ${it.longitude}, Error: ${it.error}"
//									}
//								}
//							}
//						) {
//							Text("Get GPS Location")
//						}
//
//						Text("GPS Result:$gpsLocation")
//
//						StackedAreaSample(false, "Your Stats Over Time")
//					}
//				}
//			)
//		}
	}
}

expect fun setVideoCoordinates(x: Int, y: Int, width: Int, height: Int)
expect fun setVideoVisible(visible: Boolean)

expect class GeolocationPosition
expect fun getGpsLocation(callback: (GeolocationPosition) -> Unit)

@Composable
expect fun HtmlVideoView(modifier: Modifier)

@Composable
expect fun HtmlImageView(modifier: Modifier, imageUrl: String)

//// USING EXTERNAL
//external fun getGpsLocation(callback: (GeolocationPosition) -> Unit)
//external interface GeolocationPosition {
//	val latitude: Double?
//	val longitude: Double?
//	val error: String?
//}
