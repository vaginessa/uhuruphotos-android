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

import androidx.compose.ui.geometry.Offset
import com.savvasdalkitsis.uhuruphotos.feature.collage.view.api.ui.state.CollageDisplay
import com.savvasdalkitsis.uhuruphotos.feature.media.common.view.api.ui.state.CelState
import com.savvasdalkitsis.uhuruphotos.feature.people.view.api.ui.state.Person

sealed class SearchAction {

    object Initialise : SearchAction()
    object SearchCleared : SearchAction()
    object ViewAllPeopleSelected : SearchAction()
    object LoadHeatMap : SearchAction()

    data class QueryChanged(val query: String) : SearchAction()
    data class SearchFor(val query: String) : SearchAction()
    data class ChangeFocus(val focused: Boolean) : SearchAction()
    data class SelectedCel(val celState: CelState, val center: Offset, val scale: Float) : SearchAction()
    data class ChangeDisplay(val display: CollageDisplay) : SearchAction()
    data class PersonSelected(val person: Person) : SearchAction()
    data class RemoveFromRecentSearches(val query: String) : SearchAction()
}