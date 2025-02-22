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

import com.savvasdalkitsis.uhuruphotos.feature.people.view.api.ui.state.Person
import com.savvasdalkitsis.uhuruphotos.foundation.map.api.model.LatLon

data class MediaItemDetails(
    val formattedDateAndTime: String,
    val isFavourite: Boolean,
    val isVideo: Boolean,
    val location: String,
    val latLon: LatLon?,
    val remotePath: String? = null,
    val localPath: String? = null,
    val peopleInMediaItem: List<Person>
) {
    fun mergeWith(mediaItemDetails: MediaItemDetails?): MediaItemDetails = copy(
        location = location.ifBlank { mediaItemDetails?.location.orEmpty() },
        latLon = latLon ?: mediaItemDetails?.latLon,
        remotePath = remotePath ?: mediaItemDetails?.remotePath,
        localPath = localPath ?: mediaItemDetails?.localPath,
    )
}
