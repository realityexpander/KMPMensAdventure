import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Chris Athanas on 4/4/24.
 */
@Composable
fun ShowState(
	 state: StateFlow<GameState>
//	 state: State<GameState>
) {
	Text("Game State:")
	Spacer(modifier = Modifier.height(8.dp))

	with(state.collectAsState().value) {
		Text("Name: $name")
		Text("Age: $age")
		Text("Dollars: $dollars")
		Text("Health: $health")
		Text("Happiness: $securityHappiness")
		Text("Sexual Happiness: $sexualHappiness")
		Text("Emotional Happiness: $emotionalHappiness")
		Text("Skill: $skill")
		Text("Muscularity: $muscularity")
	}
}

