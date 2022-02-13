package dev.msfjarvis.claw.android.ui

import android.content.Context
import android.os.Build
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.msfjarvis.claw.android.ui.navigation.Destinations
import dev.msfjarvis.claw.android.viewmodel.ClawViewModel
import dev.msfjarvis.claw.common.posts.PostActions
import dev.msfjarvis.claw.common.theme.DarkThemeColors
import dev.msfjarvis.claw.common.theme.LightThemeColors
import dev.msfjarvis.claw.common.urllauncher.UrlLauncher
import dev.msfjarvis.claw.database.local.SavedPost
import kotlin.math.ln

private const val AnimationDuration = 100

fun slideInAnimation(): EnterTransition {
  return slideInVertically(
    // Enters by sliding up from offset 0 to fullHeight.
    initialOffsetY = { fullHeight -> fullHeight },
    animationSpec = tween(durationMillis = AnimationDuration, easing = LinearOutSlowInEasing),
  )
}

fun slideOutAnimation(): ExitTransition {
  return slideOutVertically(
    // Exits by sliding up from offset 0 to -fullHeight.
    targetOffsetY = { fullHeight -> fullHeight },
    animationSpec = tween(durationMillis = AnimationDuration, easing = FastOutLinearInEasing),
  )
}

// The destination needs to be tracked like this rather than used directly since
// `NavController#currentDestination` is not a Composable state.
@Composable
fun currentNavigationDestination(navController: NavController): State<String?> {
  val currentDestination = remember { mutableStateOf<String?>(null) }
  navController.addOnDestinationChangedListener { _, destination, _ ->
    currentDestination.value = destination.route
  }
  return currentDestination
}

@Composable
fun decideColorScheme(context: Context): ColorScheme {
  val isDarkTheme = isSystemInDarkTheme()
  return if (Build.VERSION.SDK_INT >= 31) {
    if (isDarkTheme) {
      dynamicDarkColorScheme(context)
    } else {
      dynamicLightColorScheme(context)
    }
  } else {
    if (isDarkTheme) {
      DarkThemeColors
    } else {
      LightThemeColors
    }
  }
}

@Composable
fun rememberPostActions(
  urlLauncher: UrlLauncher,
  navController: NavController,
  viewModel: ClawViewModel,
): PostActions {
  return remember {
    object : PostActions {
      override fun viewPost(postUrl: String, commentsUrl: String) {
        urlLauncher.openUri(postUrl.ifEmpty { commentsUrl })
      }

      override fun viewComments(postId: String) {
        navController.navigate(Destinations.Comments.getRoute(postId))
      }

      override fun viewCommentsPage(commentsUrl: String) {
        urlLauncher.openUri(commentsUrl)
      }

      override fun toggleSave(post: SavedPost) {
        viewModel.toggleSave(post)
      }
    }
  }
}

/**
 * Returns the [ColorScheme.surface] color with an alpha of the [ColorScheme.primary] color overlaid
 * on top of it. Computes the surface tonal color at different elevation levels e.g. surface1
 * through surface5.
 *
 * Stolen from AndroidX, keep in sync when upgrading Compose. This version is hard-coded to
 * replicate the logic used by the Material3 NavigationBar to determine its surface color.
 * https://github.com/androidx/androidx/blob/74d3510b608c3cc26b9cf9be8d15a6a6c26192c2/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/ColorScheme.kt#L453-L466
 */
@Composable
fun ColorScheme.surfaceColorAtNavigationBarElevation(): Color {
  // Absolute tonal elevation + NavigationBarTokens.ContainerElevation
  val elevation = LocalAbsoluteTonalElevation.current + 3.0.dp
  if (elevation == 0.dp) return surface
  val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
  return primary.copy(alpha = alpha).compositeOver(surface)
}
