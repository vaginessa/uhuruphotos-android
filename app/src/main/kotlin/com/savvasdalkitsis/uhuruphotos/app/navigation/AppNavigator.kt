/*
Copyright 2022 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.exoplayer2.ExoPlayer
import com.savvasdalkitsis.uhuruphotos.api.homenavigation.HomeNavigationRoutes
import com.savvasdalkitsis.uhuruphotos.api.map.model.LocalMapProvider
import com.savvasdalkitsis.uhuruphotos.api.navigation.NavigationTarget
import com.savvasdalkitsis.uhuruphotos.api.navigation.Navigator
import com.savvasdalkitsis.uhuruphotos.api.settings.usecase.SettingsUseCase
import com.savvasdalkitsis.uhuruphotos.implementation.ui.usecase.UiUseCase
import com.savvasdalkitsis.uhuruphotos.api.ui.window.LocalSystemUiController
import com.savvasdalkitsis.uhuruphotos.api.video.LocalExoPlayer
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppNavigator @Inject constructor(
    private val navigationTargets: Set<@JvmSuppressWildcards NavigationTarget>,
    private val navigator: Navigator,
    private val uiUseCase: UiUseCase,
    private val exoPlayer: ExoPlayer,
    private val settingsUseCase: SettingsUseCase,
) {

    @Composable
    fun NavigationTargets() {
        val navHostController = rememberAnimatedNavController()
        navigator.navController = navHostController
        with(uiUseCase) {
            keyboardController = LocalSoftwareKeyboardController.current!!
            systemUiController = LocalSystemUiController.current
            haptics = LocalHapticFeedback.current
        }
        CompositionLocalProvider(
            LocalExoPlayer provides exoPlayer,
            LocalMapProvider provides settingsUseCase.getMapProvider(),
        ) {
            AnimatedNavHost(
                navController = navHostController,
                startDestination = HomeNavigationRoutes.home
            ) {
                runBlocking {
                    navigationTargets.forEach { navigationTarget ->
                        with(navigationTarget) { create(navHostController) }
                    }
                }
            }
        }
    }
}