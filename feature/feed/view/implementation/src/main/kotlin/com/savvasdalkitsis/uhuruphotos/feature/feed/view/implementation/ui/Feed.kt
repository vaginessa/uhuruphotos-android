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
package com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.savvasdalkitsis.uhuruphotos.feature.collage.view.api.ui.Collage
import com.savvasdalkitsis.uhuruphotos.feature.collage.view.api.ui.state.PredefinedCollageDisplay
import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.seam.FeedAction
import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.seam.FeedAction.*
import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.ui.state.FeedState
import com.savvasdalkitsis.uhuruphotos.feature.home.view.api.ui.HomeScaffold
import com.savvasdalkitsis.uhuruphotos.feature.media.common.view.api.ui.TrashPermissionDialog
import com.savvasdalkitsis.uhuruphotos.foundation.compose.api.blurIf
import com.savvasdalkitsis.uhuruphotos.foundation.ui.api.ui.SwipeRefresh
import kotlinx.coroutines.launch

@Composable
internal fun Feed(
    navHostController: NavHostController,
    state: FeedState,
    isShowingPopUp: Boolean,
    action: (FeedAction) -> Unit,
    actionBarContent: @Composable () -> Unit,
    additionalContent: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()

    fun scrollToTop() {
        coroutineScope.launch {
            gridState.animateScrollToItem(0, 0)
        }
    }

    HomeScaffold(
        modifier = Modifier.blurIf(isShowingPopUp),
        title = {
            FeedTitle(state, action, ::scrollToTop)
        },
        navController = navHostController,
        showLibrary = state.showLibrary,
        homeFeedDisplay = state.collageState.collageDisplay,
        selectionMode = state.hasSelection,
        actionBarContent = {
            FeedActionBar(state, action)
            actionBarContent()
        },
        onReselected = { scrollToTop() },
    ) { contentPadding ->
        SwipeRefresh(
            indicatorPadding = contentPadding,
            isRefreshing = state.isRefreshing,
            onRefresh = { action(RefreshFeed) }
        ) {
            Collage(
                contentPadding = contentPadding,
                state = state.collageState,
                showSelectionHeader = state.hasSelection,
                showGroupRefreshButton = state.shouldShowClusterRefreshButtons,
                gridState = gridState,
                onCelSelected = { cel, center, scale ->
                    action(SelectedCel(cel, center, scale,))
                },
                onChangeDisplay = { action(ChangeDisplay(it as PredefinedCollageDisplay)) },
                onCelLongPressed = {
                    action(CelLongPressed(it))
                },
                onClusterSelectionClicked = {
                    action(ClusterSelectionClicked(it))
                },
                onClusterRefreshClicked = {
                    action(ClusterRefreshClicked(it))
                },
                collageHeader = {
                    AnimatedVisibility(visible = state.memories.isNotEmpty()) {
                        FeedMemories(state.memories) { cel, center, scale ->
                            action(MemorySelected(cel, center, scale))
                        }
                    }
                }
            )
        }
        if (state.showTrashingConfirmationDialog) {
            TrashPermissionDialog(
                mediaItemCount = state.selectedCelCount,
                onDismiss = { action(DismissSelectedPhotosTrashing) },
                onDelete = { action(TrashSelectedCels) }
            )
        }
        additionalContent()
    }
}