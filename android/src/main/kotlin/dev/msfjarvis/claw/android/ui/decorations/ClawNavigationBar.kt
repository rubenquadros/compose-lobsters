package dev.msfjarvis.claw.android.ui.decorations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavController
import dev.msfjarvis.claw.android.ui.AnimationDuration

@Composable
fun ClawNavigationBar(
  navController: NavController,
  items: List<NavigationItem>,
  isVisible: Boolean,
  modifier: Modifier = Modifier,
) {
  var selectedIndex by remember { mutableStateOf(0) }

  AnimatedVisibility(
    visible = isVisible,
    enter =
      slideInVertically(
        // Enters by sliding up from offset 0 to fullHeight.
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = AnimationDuration, easing = LinearOutSlowInEasing),
      ),
    exit =
      slideOutVertically(
        // Exits by sliding up from offset 0 to -fullHeight.
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = AnimationDuration, easing = FastOutLinearInEasing),
      ),
    modifier = Modifier,
  ) {
    NavigationBar(modifier = modifier) {
      items.forEachIndexed { index, navItem ->
        NavigationBarItem(
          icon = { Icon(painter = navItem.icon, contentDescription = navItem.label) },
          label = { Text(text = navItem.label) },
          selected = selectedIndex == index,
          onClick = {
            selectedIndex = index
            navController.navigate(navItem.route)
          }
        )
      }
    }
  }
}

class NavigationItem(
  val label: String,
  val route: String,
  val icon: Painter,
)
