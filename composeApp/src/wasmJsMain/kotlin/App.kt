import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.koalaplot.core.bar.BulletGraph
import io.github.koalaplot.core.style.KoalaPlotTheme
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

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class, ExperimentalKoalaPlotApi::class)
@Composable
fun App(
	onLoadFinished: () -> Unit
) {
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
		val scope = rememberCoroutineScope()
		val state = MutableStateFlow<GameState>(GameState())

		val scaffoldState = rememberBottomSheetScaffoldState()
		val drawerState = rememberDrawerState(DrawerValue.Closed)

		LaunchedEffect(Unit) {
			delay(500)
			onLoadFinished()
		}

		ModalDrawer(
			modifier = Modifier.fillMaxSize(),
			drawerContent = {
				Text("Drawer content")
			},
			gesturesEnabled = false,
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
				sheetGesturesEnabled = false,
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
					Column(
						Modifier
							.fillMaxSize()
//							.verticalScroll(
//								state=rememberScrollState(),
//								enabled = true,
//							),
								,
						horizontalAlignment = Alignment.Start
					) {
						//Image(painterResource(Res.drawable.compose_multiplatform), null, modifier = Modifier.size(128.dp) )
						//Text("Compose: $greeting")

						ShowState(state)

						Button(
							onClick = {
//								state2.value = state.value.copy(
//									age = state.value.age + 1
//								)

								state.update {
									it.copy(
										age = it.age + 1,
										dollars = it.dollars - 100,
										health = it.health - 10,
										securityHappiness = it.securityHappiness - 10,
										emotionalHappiness = it.emotionalHappiness + 1
									)
								}
							}
						) {
							Text("Do Nothing")
						}

						Button(
							onClick = {
								state.update {
									it.copy(
										age = it.age + 1,
										dollars = it.dollars + 100,
										health = it.health + 10,
										skill = it.skill + 5
									)
								}
							}
						) {
							Text("Work")
						}

						Button(
							onClick = {
								state.update {
									it.copy(
										age = it.age + 1,
										dollars = it.dollars - 100,
										health = it.health - 10,
										securityHappiness = it.securityHappiness + 5,
										emotionalHappiness = it.emotionalHappiness + 5
									)
								}
							}
						) {
							Text("Go to a party")
						}

						Button(
							onClick = {
								state.update {
									it.copy(
										age = it.age + 1,
										dollars = it.dollars - 200,
										health = it.health - 10
									)
								}
							}
						) {
							Text("Go on to School")
						}

//						BulletGraph {
//							label {
//								Column(
//									horizontalAlignment = Alignment.End,
//									modifier = Modifier.padding(end = KoalaPlotTheme.sizes.gap)
//								) {
//									Text(
//										"Revenue 2005 YTD",
//										textAlign = TextAlign.End
//									)
//									Text(
//										"(US $ in thousands)",
//										textAlign = TextAlign.End
//									)
//								}
//							}
//							axis { labels { "${it.toInt()}" } }
//							comparativeMeasure(260f)
//							featuredMeasureBar(275f)
//							ranges(
//								0f,
//								200f,
//								250f,
//								300f
//							)
//						}
//
//						LazyColumn {
//							items(100) { index ->
//								Text("Item $index")
//							}
//						}

						StackedAreaSample(false, "Your Stats Over Time")
					}
				}
			)
		}
	}
}