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
package com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam

import androidx.work.NetworkType
import com.savvasdalkitsis.uhuruphotos.foundation.map.api.model.MapProvider
import com.savvasdalkitsis.uhuruphotos.foundation.ui.api.theme.ThemeMode

internal sealed class SettingsAction {

    data class ChangeImageDiskCache(val sizeInMb: Float) : SettingsAction()
    data class ChangeVideoDiskCache(val sizeInMb: Float) : SettingsAction()
    data class ChangeImageMemCache(val sizeInMb: Float) : SettingsAction()
    data class ChangeFullSyncNetworkRequirements(val networkType: NetworkType) : SettingsAction()
    data class ChangeFullSyncChargingRequirements(val requiredCharging: Boolean) : SettingsAction()
    data class ChangeSearchSuggestionsEnabled(val enabled: Boolean) : SettingsAction()
    data class ChangeShareGpsDataEnabled(val enabled: Boolean) : SettingsAction()
    data class ChangeShowLibrary(val show: Boolean) : SettingsAction()
    data class ChangeAnimateVideoThumbnails(val animate: Boolean) : SettingsAction()
    data class ChangeMaxAnimatedVideoThumbnails(val max: Int) : SettingsAction()

    data class FeedSyncFrequencyChanged(val frequency: Float, val upperLimit: Float) : SettingsAction()
    data class ChangeThemeMode(val themeMode: ThemeMode) : SettingsAction()
    data class ChangeMapProvider(val mapProvider: MapProvider) : SettingsAction()
    data class ChangeLoggingEnabled(val enabled: Boolean) : SettingsAction()
    data class FeedRefreshChanged(val days: Int) : SettingsAction()
    data class ChangeBiometricsAppAccessRequirement(val required: Boolean) : SettingsAction()
    data class ChangeBiometricsHiddenPhotosAccessRequirement(val required: Boolean) : SettingsAction()
    data class ChangeBiometricsTrashAccessRequirement(val required: Boolean) : SettingsAction()
    data class ChangeMemoriesEnabled(val enabled: Boolean) : SettingsAction()

    object LoadSettings : SettingsAction()
    object NavigateBack : SettingsAction()
    object ClearImageDiskCache : SettingsAction()
    object ClearImageMemCache : SettingsAction()
    object ClearVideoDiskCache : SettingsAction()
    object AskForFullFeedSync : SettingsAction()
    object CancelFullFeedSync : SettingsAction()
    object DismissFullFeedSyncDialog : SettingsAction()
    object PerformFullFeedSync : SettingsAction()
    object ClearLogFileClicked : SettingsAction()
    object SendFeedbackClicked : SettingsAction()
    object ClearRecentSearches : SettingsAction()
    object EnrollToBiometrics : SettingsAction()
    object AskForPrecacheThumbnails : SettingsAction()
    object CancelPrecacheThumbnails : SettingsAction()
    object DismissPrecacheThumbnailsDialog : SettingsAction()
    object PrecacheThumbnails : SettingsAction()
}
