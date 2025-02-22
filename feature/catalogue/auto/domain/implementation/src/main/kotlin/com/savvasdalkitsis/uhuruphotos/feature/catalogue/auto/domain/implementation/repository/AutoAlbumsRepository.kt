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
package com.savvasdalkitsis.uhuruphotos.feature.catalogue.auto.domain.implementation.repository

import com.savvasdalkitsis.uhuruphotos.feature.catalogue.auto.domain.implementation.service.AutoAlbumsService
import com.savvasdalkitsis.uhuruphotos.feature.catalogue.auto.domain.implementation.service.model.toAutoAlbums
import com.savvasdalkitsis.uhuruphotos.feature.db.domain.api.album.auto.AutoAlbums
import com.savvasdalkitsis.uhuruphotos.feature.db.domain.api.album.auto.AutoAlbumsQueries
import com.savvasdalkitsis.uhuruphotos.feature.db.domain.api.extensions.await
import com.savvasdalkitsis.uhuruphotos.foundation.log.api.runCatchingWithLog
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class AutoAlbumsRepository @Inject constructor(
    private val autoAlbumsQueries: AutoAlbumsQueries,
    private val autoAlbumsService: AutoAlbumsService,
) {
    fun observeAutoAlbums(): Flow<List<AutoAlbums>> =
        autoAlbumsQueries.getAutoAlbums().asFlow().mapToList()
            .distinctUntilChanged()

    suspend fun getAutoAlbums(): List<AutoAlbums> =
        autoAlbumsQueries.getAutoAlbums().await()

    suspend fun refreshAutoAlbums(): Result<Unit> = runCatchingWithLog {
        val albums = autoAlbumsService.getAutoAlbums()
        autoAlbumsQueries.transaction {
            autoAlbumsQueries.clearAll()
            for (album in albums.results) {
                autoAlbumsQueries.insert(album.toAutoAlbums())
            }
        }
    }

}