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
package com.savvasdalkitsis.uhuruphotos.feature.settings.domain.api.usecase

import androidx.work.NetworkType
import com.savvasdalkitsis.uhuruphotos.foundation.map.api.model.MapProvider
import com.savvasdalkitsis.uhuruphotos.foundation.ui.api.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsUseCase {

    fun getImageDiskCacheMaxLimit(): Int
    fun getImageMemCacheMaxLimit(): Int
    fun getVideoDiskCacheMaxLimit(): Int
    fun getFeedSyncFrequency(): Int
    fun getFeedDaysToRefresh(): Int
    fun getFullSyncNetworkRequirements(): NetworkType
    fun getFullSyncRequiresCharging(): Boolean
    fun getShouldPerformPeriodicFullSync(): Boolean
    fun getShareRemoveGpsData(): Boolean
    fun getShowLibrary(): Boolean
    fun getMapProvider(): MapProvider
    fun getAvailableMapProviders(): Set<MapProvider>
    fun getLoggingEnabled(): Boolean
    fun getBiometricsRequiredForAppAccess(): Boolean
    fun getBiometricsRequiredForHiddenPhotosAccess(): Boolean
    fun getBiometricsRequiredForTrashAccess(): Boolean
    fun getMemoriesEnabled(): Boolean
    fun getAnimateVideoThumbnails(): Boolean
    fun getMaxAnimatedVideoThumbnails(): Int
    fun getShowBannerAskingForLocalMediaPermissionsOnFeed(): Boolean
    fun getShowBannerAskingForLocalMediaPermissionsOnHeatmap(): Boolean

    fun observeImageDiskCacheMaxLimit(): Flow<Int>
    fun observeImageMemCacheMaxLimit(): Flow<Int>
    fun observeVideoDiskCacheMaxLimit(): Flow<Int>
    fun observeFeedSyncFrequency(): Flow<Int>
    fun observeFeedDaysToRefresh(): Flow<Int>
    fun observeFullSyncNetworkRequirements(): Flow<NetworkType>
    fun observeFullSyncRequiresCharging(): Flow<Boolean>
    fun observeThemeMode(): Flow<ThemeMode>
    fun observeSearchSuggestionsEnabledMode(): Flow<Boolean>
    fun observeShareRemoveGpsData(): Flow<Boolean>
    fun observeShowLibrary(): Flow<Boolean>
    suspend fun observeThemeModeState(): StateFlow<ThemeMode>
    fun observeMapProvider(): Flow<MapProvider>
    fun observeLoggingEnabled(): Flow<Boolean>
    fun observeBiometricsRequiredForAppAccess(): Flow<Boolean>
    fun observeBiometricsRequiredForHiddenPhotosAccess(): Flow<Boolean>
    fun observeBiometricsRequiredForTrashAccess(): Flow<Boolean>
    fun observeMemoriesEnabled(): Flow<Boolean>
    fun observeAnimateVideoThumbnails(): Flow<Boolean>
    fun observeMaxAnimatedVideoThumbnails(): Flow<Int>

    suspend fun setImageDiskCacheMaxLimit(sizeInMb: Int)
    suspend fun setImageMemCacheMaxLimit(sizeInMb: Int)
    suspend fun setVideoDiskCacheMaxLimit(sizeInMb: Int)
    suspend fun setFeedSyncFrequency(frequency: Int)
    suspend fun setFeedFeedDaysToRefresh(days: Int)
    suspend fun setFullSyncNetworkRequirements(networkType: NetworkType)
    suspend fun setFullSyncRequiresCharging(requiresCharging: Boolean)
    suspend fun setShouldPerformPeriodicFullSync(perform: Boolean)
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setSearchSuggestionsEnabled(enabled: Boolean)
    suspend fun setShareRemoveGpsData(enabled: Boolean)
    suspend fun setShowLibrary(show: Boolean)
    suspend fun setMapProvider(provider: MapProvider)
    suspend fun setLoggingEnabled(enabled: Boolean)
    suspend fun setBiometricsRequiredForAppAccess(required: Boolean)
    suspend fun setBiometricsRequiredForHiddenPhotosAccess(required: Boolean)
    suspend fun setBiometricsRequiredForTrashAccess(required: Boolean)
    suspend fun setMemoriesEnabled(enabled: Boolean)
    suspend fun setAnimateVideoThumbnails(animate: Boolean)
    suspend fun setMaxAnimatedVideoThumbnails(max: Int)
    suspend fun setShowBannerAskingForLocalMediaPermissionsOnFeed(show: Boolean)
    suspend fun setShowBannerAskingForLocalMediaPermissionsOnHeatmap(show: Boolean)
}