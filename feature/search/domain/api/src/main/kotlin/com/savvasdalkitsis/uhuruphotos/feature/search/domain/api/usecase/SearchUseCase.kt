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

package com.savvasdalkitsis.uhuruphotos.feature.search.domain.api.usecase

import com.savvasdalkitsis.uhuruphotos.feature.media.common.domain.api.model.MediaCollection
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    fun searchFor(query: String): Flow<Result<List<MediaCollection>>>
    suspend fun searchResultsFor(query: String): List<MediaCollection>
    fun getRandomSearchSuggestion(): Flow<String>
    fun getSearchSuggestions(): Flow<List<String>>
    fun getRecentTextSearches(): Flow<List<String>>
    suspend fun addSearchToRecentSearches(query: String)
    suspend fun removeFromRecentSearches(query: String)
    suspend fun clearRecentSearchSuggestions()
}