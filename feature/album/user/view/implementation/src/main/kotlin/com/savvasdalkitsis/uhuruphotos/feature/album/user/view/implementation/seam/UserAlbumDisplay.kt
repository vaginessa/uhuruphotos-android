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
package com.savvasdalkitsis.uhuruphotos.feature.album.user.view.implementation.seam

import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.savvasdalkitsis.uhuruphotos.feature.collage.view.api.ui.state.CollageDisplay
import com.savvasdalkitsis.uhuruphotos.feature.collage.view.api.ui.state.PredefinedCollageDisplay
import javax.inject.Inject

internal class UserAlbumDisplay @Inject constructor(
    private val flowSharedPreferences: FlowSharedPreferences,
) {

    fun getUserAlbumGalleryDisplay(albumId: Int) : CollageDisplay =
        userAlbumGalleryDisplay(albumId).get()

    suspend fun setUserAlbumGalleryDisplay(albumId: Int, galleryDisplay: PredefinedCollageDisplay) {
        userAlbumGalleryDisplay(albumId).setAndCommit(galleryDisplay)
    }

    private fun userAlbumGalleryDisplay(albumId: Int) =
        flowSharedPreferences.getEnum("userAlbumGalleryDisplay/$albumId", PredefinedCollageDisplay.default)

}
