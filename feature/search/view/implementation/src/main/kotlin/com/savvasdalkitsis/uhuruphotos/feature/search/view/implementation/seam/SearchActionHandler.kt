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
package com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam

import com.savvasdalkitsis.uhuruphotos.feature.collage.view.api.ui.state.toCluster
import com.savvasdalkitsis.uhuruphotos.feature.db.domain.api.people.People
import com.savvasdalkitsis.uhuruphotos.feature.feed.domain.api.usecase.FeedUseCase
import com.savvasdalkitsis.uhuruphotos.feature.media.remote.domain.api.usecase.RemoteMediaUseCase
import com.savvasdalkitsis.uhuruphotos.feature.people.domain.api.usecase.PeopleUseCase
import com.savvasdalkitsis.uhuruphotos.feature.people.view.api.ui.state.toPerson
import com.savvasdalkitsis.uhuruphotos.feature.search.domain.api.usecase.SearchUseCase
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.ChangeDisplay
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.ChangeFocus
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.Initialise
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.LoadHeatMap
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.PersonSelected
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.QueryChanged
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.RemoveFromRecentSearches
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.SearchCleared
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.SearchFor
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.SelectedCel
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchAction.ViewAllPeopleSelected
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.ErrorRefreshingPeople
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.ErrorSearching
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.HideKeyboard
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.NavigateToAllPeople
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.NavigateToHeatMap
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.NavigateToPerson
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.OpenLightbox
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.ChangeFeedDisplay
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.ChangeSearchDisplay
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.FocusChanged
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.HideSuggestions
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.ShowLibrary
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.ShowPeople
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.ShowSearchSuggestion
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.ShowSearchSuggestions
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.SwitchStateToFound
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.SwitchStateToIdle
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.SwitchStateToSearching
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchMutation.UpdateLatestQuery
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.ui.state.SearchResults.Found
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.ui.state.SearchState
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.ui.state.SearchSuggestion
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.ui.state.SearchSuggestion.PersonSearchSuggestion
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.ui.state.SearchSuggestion.RecentSearchSuggestion
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.ui.state.SearchSuggestion.ServerSearchSuggestion
import com.savvasdalkitsis.uhuruphotos.feature.settings.domain.api.usecase.SettingsUseCase
import com.savvasdalkitsis.uhuruphotos.foundation.coroutines.api.onErrors
import com.savvasdalkitsis.uhuruphotos.foundation.coroutines.api.onErrorsIgnore
import com.savvasdalkitsis.uhuruphotos.foundation.log.api.log
import com.savvasdalkitsis.uhuruphotos.foundation.seam.api.ActionHandler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class SearchActionHandler @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val feedUseCase: FeedUseCase,
    private val settingsUseCase: SettingsUseCase,
    private val peopleUseCase: PeopleUseCase,
    private val remoteMediaUseCase: RemoteMediaUseCase,
) : ActionHandler<SearchState, SearchEffect, SearchAction, SearchMutation> {

    private var lastSearch: Job? = null
    private var lastSuggestions: Job? = null
    private val queryFilter = MutableSharedFlow<String>()

    override fun handleAction(
        state: SearchState,
        action: SearchAction,
        effect: suspend (SearchEffect) -> Unit,
    ): Flow<SearchMutation> = when (action) {
        Initialise -> with(remoteMediaUseCase) {
            merge(
                showLibrary(),
                showFeedDisplay(),
                showServerSearchSuggestion(),
                showPeopleSuggestion(effect),
                showSearchSuggestions()
            )
        }
        is QueryChanged -> flow {
            queryFilter.emit(action.query)
            emit(UpdateLatestQuery(action.query))
        }
        is SearchFor -> performSearch(effect, action)
        is ChangeFocus -> flowOf(FocusChanged(action.focused))
        SearchCleared -> flow {
            lastSearch?.cancel()
            lastSuggestions?.cancel()
            emit(SwitchStateToIdle)
        }
        is SelectedCel -> flow {
            with(action) {
                effect(OpenLightbox(celState.mediaItem.id, center, scale, celState.mediaItem.isVideo, state.latestQuery))
            }
        }
        is ChangeDisplay -> flowOf(ChangeSearchDisplay(action.display))
        ViewAllPeopleSelected -> flow {
            effect(NavigateToAllPeople)
        }
        is PersonSelected -> flow {
            effect(NavigateToPerson(action.person.id))
        }
        LoadHeatMap -> flow {
            effect(NavigateToHeatMap)
        }
        is RemoveFromRecentSearches -> flow {
            searchUseCase.removeFromRecentSearches(action.query)
        }
    }

    private fun performSearch(
        effect: suspend (SearchEffect) -> Unit,
        action: SearchFor
    ) = channelFlow {
        lastSearch?.cancel()
        send(UpdateLatestQuery(action.query))
        send(SwitchStateToSearching)
        effect(HideKeyboard)
        lastSearch = launch {
            searchUseCase.addSearchToRecentSearches(action.query)
            searchUseCase.searchFor(action.query)
                .debounce(200)
                .mapNotNull { result ->
                    val clusters = result.getOrNull()?.map { it.toCluster() }
                    if (clusters != null)
                        when {
                            clusters.isEmpty() -> SwitchStateToSearching
                            else -> SwitchStateToFound(Found(clusters))
                        }
                    else {
                        effect(ErrorSearching)
                        null
                    }
                }
                .cancellable()
                .catch {
                    if (it !is CancellationException) {
                        log(it)
                        effect(ErrorSearching)
                    }
                    send(SwitchStateToIdle)
                }
                .collect { send(it) }
        }
    }

    context(RemoteMediaUseCase)
    private fun showSearchSuggestions() = combine(
        searchUseCase.getRecentTextSearches()
            .map {
                it.map(::RecentSearchSuggestion)
            },
        peopleUseCase.observePeopleByPhotoCount()
            .onErrorsIgnore()
            .toPeople()
            .map {
                it.map(::PersonSearchSuggestion)
            },
        searchUseCase.getSearchSuggestions()
            .map {
                it.map(::ServerSearchSuggestion)
            },
        queryFilter,
    ) { recentSearches, people, searchSuggestions, query ->
        when {
            query.isEmpty() -> emptyList()
            else -> recentSearches + people + searchSuggestions
        }.filterQuery(query)
    }.map(::ShowSearchSuggestions)

    context(RemoteMediaUseCase)
    private fun showPeopleSuggestion(effect: suspend (SearchEffect) -> Unit) =
        peopleUseCase.observePeopleByPhotoCount()
            .onErrors {
                effect(ErrorRefreshingPeople)
            }
            .toPeople()
            .map { it.subList(0, max(0, min(10, it.size - 1))) }
            .map(::ShowPeople)

    private fun showServerSearchSuggestion() =
        settingsUseCase.observeSearchSuggestionsEnabledMode().flatMapLatest { enabled ->
            if (enabled)
                searchUseCase.getRandomSearchSuggestion()
                    .map(::ShowSearchSuggestion)
            else
                flowOf(HideSuggestions)
        }

    private fun showLibrary() = settingsUseCase.observeShowLibrary()
        .map(::ShowLibrary)

    private fun showFeedDisplay() = feedUseCase
        .getFeedDisplay()
        .distinctUntilChanged()
        .map(::ChangeFeedDisplay)

    context(RemoteMediaUseCase)
    private fun Flow<List<People>>.toPeople() = map { people ->
        people.map {
            it.toPerson { url -> url.toRemoteUrl() }
        }
    }

    private fun List<SearchSuggestion>.filterQuery(query: String): List<SearchSuggestion> =
        filter { it.filterable.contains(query, ignoreCase = true) }
}
