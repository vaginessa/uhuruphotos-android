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
package com.savvasdalkitsis.uhuruphotos.feature.media.common.domain.api.model

data class MediaItemInstance(
    override val id: MediaId<*>,
    override val mediaHash: String,
    override val thumbnailUri: String? = null,
    override val fullResUri: String? = null,
    override val fallbackColor: String? = null,
    override val displayDayDate: String? = null,
    override val sortableDate: String? = null,
    override val isFavourite: Boolean = false,
    override val ratio: Float = 1f,
    override val isVideo: Boolean = false,
    override val latLng: (Pair<Double, Double>)? = null,
    override val syncState: MediaItemSyncState
) : MediaItem