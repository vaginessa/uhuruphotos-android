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

import com.savvasdalkitsis.uhuruphotos.api.heatmap.navigation.HeatMapNavigationTarget
import com.savvasdalkitsis.uhuruphotos.feature.lightbox.view.api.model.LightboxSequenceDataSource.SearchResults
import com.savvasdalkitsis.uhuruphotos.feature.lightbox.view.api.navigation.LightboxNavigationTarget
import com.savvasdalkitsis.uhuruphotos.feature.people.view.api.navigation.PeopleNavigationTarget
import com.savvasdalkitsis.uhuruphotos.feature.person.view.api.navigation.PersonNavigationTarget
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.ErrorRefreshingPeople
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.ErrorSearching
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.HideKeyboard
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.NavigateToAllPeople
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.NavigateToHeatMap
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.NavigateToPerson
import com.savvasdalkitsis.uhuruphotos.feature.search.view.implementation.seam.SearchEffect.OpenLightbox
import com.savvasdalkitsis.uhuruphotos.foundation.navigation.api.Navigator
import com.savvasdalkitsis.uhuruphotos.foundation.seam.api.EffectHandler
import com.savvasdalkitsis.uhuruphotos.foundation.strings.api.R.string
import com.savvasdalkitsis.uhuruphotos.foundation.toaster.api.Toaster
import com.savvasdalkitsis.uhuruphotos.foundation.ui.api.usecase.UiUseCase
import javax.inject.Inject

class SearchEffectsHandler @Inject constructor(
    private val navigator: Navigator,
    private val uiUseCase: UiUseCase,
    private val toaster: Toaster,
) : EffectHandler<SearchEffect> {

    override suspend fun handleEffect(
        effect: SearchEffect,
    ) = when (effect) {
        HideKeyboard -> uiUseCase.hideKeyboard()
        is OpenLightbox -> navigateTo(
            with(effect) {
                LightboxNavigationTarget.name(
                    id,
                    center,
                    scale,
                    isVideo,
                    SearchResults(currentQuery)
                )
            }
        )
        ErrorSearching -> toaster.show(string.error_searching)
        NavigateToAllPeople -> navigateTo(PeopleNavigationTarget.name)
        ErrorRefreshingPeople -> toaster.show(string.error_refreshing_people)
        is NavigateToPerson -> navigateTo(PersonNavigationTarget.name(effect.personId))
        NavigateToHeatMap -> navigateTo(HeatMapNavigationTarget.name)
    }

    private fun navigateTo(target: String) {
        navigator.navigateTo(target)
    }
}
