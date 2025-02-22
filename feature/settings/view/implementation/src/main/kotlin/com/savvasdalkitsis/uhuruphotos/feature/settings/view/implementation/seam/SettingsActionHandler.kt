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

import androidx.work.ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE
import androidx.work.WorkInfo.State.RUNNING
import com.savvasdalkitsis.uhuruphotos.feature.avatar.domain.api.usecase.AvatarUseCase
import com.savvasdalkitsis.uhuruphotos.feature.feed.domain.api.worker.FeedWorkScheduler
import com.savvasdalkitsis.uhuruphotos.feature.search.domain.api.usecase.SearchUseCase
import com.savvasdalkitsis.uhuruphotos.feature.settings.domain.api.usecase.CacheSettingsUseCase
import com.savvasdalkitsis.uhuruphotos.feature.settings.domain.api.usecase.SettingsUseCase
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsAction.*
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsEffect.ShowMessage
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.AvatarUpdate
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisableFullSyncButton
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisablePrecacheThumbnailsButton
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayAnimateVideoThumbnailsEnabled
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayBiometrics
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayDiskCacheMaxLimit
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayFeedDaystoRefresh
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayFeedSyncFrequency
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayFullSyncNetworkRequirements
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayFullSyncProgress
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayFullSyncRequiresCharging
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayImageDiskCacheCurrentUse
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayImageMemCacheCurrentUse
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayLoggingEnabled
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayMapProviders
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayMaxAnimatedVideoThumbnails
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayMemCacheMaxLimit
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayNoMapProvidersOptions
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayPrecacheThumbnailsProgress
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplaySearchSuggestionsEnabled
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayShareGpsDataEnabled
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayShowLibrary
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayShowMemories
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayThemeMode
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayVideoDiskCacheCurrentUse
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.DisplayVideoDiskCacheMaxLimit
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.EnableFullSyncButton
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.EnablePrecacheThumbnailsButton
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.HideFullFeedSyncDialog
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.HideFullSyncProgress
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.HidePrecacheThumbnailsDialog
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.HidePrecacheThumbnailsProgress
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.SetMemoryCacheUpperLimit
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.SetDiskCacheUpperLimit
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.ShowFullFeedSyncDialog
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.seam.SettingsMutation.ShowPrecacheThumbnailsDialog
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.ui.state.BiometricsSetting
import com.savvasdalkitsis.uhuruphotos.feature.settings.view.implementation.ui.state.SettingsState
import com.savvasdalkitsis.uhuruphotos.foundation.biometrics.api.model.Biometrics.Enrolled
import com.savvasdalkitsis.uhuruphotos.foundation.biometrics.api.model.Biometrics.NoHardware
import com.savvasdalkitsis.uhuruphotos.foundation.biometrics.api.model.Biometrics.NotEnrolled
import com.savvasdalkitsis.uhuruphotos.foundation.biometrics.api.usecase.BiometricsUseCase
import com.savvasdalkitsis.uhuruphotos.foundation.log.api.usecase.FeedbackUseCase
import com.savvasdalkitsis.uhuruphotos.foundation.seam.api.ActionHandler
import com.savvasdalkitsis.uhuruphotos.foundation.strings.api.R.string
import com.savvasdalkitsis.uhuruphotos.foundation.system.api.usecase.SystemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

internal class SettingsActionHandler @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val feedWorkScheduler: FeedWorkScheduler,
    private val avatarUseCase: AvatarUseCase,
    private val cacheUseCase: CacheSettingsUseCase,
    private val feedbackUseCase: FeedbackUseCase,
    private val searchUseCase: SearchUseCase,
    private val biometricsUseCase: BiometricsUseCase,
    private val systemUseCase: SystemUseCase,
) : ActionHandler<SettingsState, SettingsEffect, SettingsAction, SettingsMutation> {

    override fun handleAction(
        state: SettingsState,
        action: SettingsAction,
        effect: suspend (SettingsEffect) -> Unit
    ): Flow<SettingsMutation> = when (action) {
        LoadSettings -> merge(
            settingsUseCase.observeImageDiskCacheMaxLimit()
                .map(::DisplayDiskCacheMaxLimit),
            settingsUseCase.observeImageMemCacheMaxLimit()
                .map(::DisplayMemCacheMaxLimit),
            settingsUseCase.observeVideoDiskCacheMaxLimit()
                .map(::DisplayVideoDiskCacheMaxLimit),
            settingsUseCase.observeFeedSyncFrequency()
                .map(::DisplayFeedSyncFrequency),
            settingsUseCase.observeFeedDaysToRefresh()
                .map(::DisplayFeedDaystoRefresh),
            settingsUseCase.observeFullSyncNetworkRequirements()
                .map(::DisplayFullSyncNetworkRequirements),
            settingsUseCase.observeFullSyncRequiresCharging()
                .map(::DisplayFullSyncRequiresCharging),
            settingsUseCase.observeThemeMode()
                .map(::DisplayThemeMode),
            settingsUseCase.observeSearchSuggestionsEnabledMode()
                .map(::DisplaySearchSuggestionsEnabled),
            settingsUseCase.observeShareRemoveGpsData()
                .map(::DisplayShareGpsDataEnabled),
            settingsUseCase.observeShowLibrary()
                .map(::DisplayShowLibrary),
            settingsUseCase.observeMemoriesEnabled()
                .map(::DisplayShowMemories),
            settingsUseCase.observeAnimateVideoThumbnails()
                .map(::DisplayAnimateVideoThumbnailsEnabled),
            settingsUseCase.observeMaxAnimatedVideoThumbnails()
                .map(::DisplayMaxAnimatedVideoThumbnails),
            settingsUseCase.observeMapProvider().map { current ->
                val available = settingsUseCase.getAvailableMapProviders()
                when {
                    available.size > 1 -> DisplayMapProviders(current, available)
                    else -> DisplayNoMapProvidersOptions
                }
            },
            settingsUseCase.observeLoggingEnabled()
                .map(::DisplayLoggingEnabled),
            combine(
                settingsUseCase.observeBiometricsRequiredForAppAccess(),
                settingsUseCase.observeBiometricsRequiredForHiddenPhotosAccess(),
                settingsUseCase.observeBiometricsRequiredForTrashAccess(),
            ) { app, hiddenPhotos, trash ->
                DisplayBiometrics(enrollment(app, hiddenPhotos, trash))
            },
            cacheUseCase.observeImageDiskCacheCurrentUse()
                .map(::DisplayImageDiskCacheCurrentUse),
            cacheUseCase.observeImageMemCacheCurrentUse()
                .map(::DisplayImageMemCacheCurrentUse),
            cacheUseCase.observeVideoDiskCacheCurrentUse()
                .map(::DisplayVideoDiskCacheCurrentUse),
            avatarUseCase.getAvatarState()
                .map(::AvatarUpdate),
            flowOf(systemUseCase.getAvailableSystemMemoryInMb())
                .map(::SetMemoryCacheUpperLimit),
            flowOf(systemUseCase.getAvailableStorageInMb())
                .map(::SetDiskCacheUpperLimit),
            combine(
                feedWorkScheduler.observeFeedRefreshJob(),
                feedWorkScheduler.observePrecacheThumbnailsJob(),
            ) { feedRefresh, precacheThumbnails ->
                feedRefresh to precacheThumbnails
            }.flatMapMerge { (feedRefresh, precacheThumbnails) ->
                flow {
                    when  {
                        feedRefresh?.status == RUNNING -> {
                            emit(DisableFullSyncButton)
                            emit(DisablePrecacheThumbnailsButton)
                            emit(DisplayFullSyncProgress(feedRefresh.progress))
                            emit(HidePrecacheThumbnailsProgress)
                        }
                        precacheThumbnails?.status == RUNNING -> {
                            emit(DisableFullSyncButton)
                            emit(DisablePrecacheThumbnailsButton)
                            emit(DisplayPrecacheThumbnailsProgress(precacheThumbnails.progress))
                            emit(HideFullSyncProgress)
                        }
                        else -> {
                            emit(EnableFullSyncButton)
                            emit(EnablePrecacheThumbnailsButton)
                        }
                    }
                }
            }
        )
        NavigateBack -> flow {
            effect(SettingsEffect.NavigateBack)
        }
        is ChangeImageDiskCache -> flow {
            settingsUseCase.setImageDiskCacheMaxLimit(action.sizeInMb.toInt())
        }
        ClearImageDiskCache -> flow {
            cacheUseCase.clearImageDiskCache()
        }
        is ChangeImageMemCache -> flow {
            settingsUseCase.setImageMemCacheMaxLimit(action.sizeInMb.toInt())
        }
        ClearImageMemCache -> flow {
            cacheUseCase.clearImageMemCache()
        }
        is ChangeVideoDiskCache -> flow {
            settingsUseCase.setVideoDiskCacheMaxLimit(action.sizeInMb.toInt())
        }
        ClearVideoDiskCache -> flow {
            cacheUseCase.clearVideoDiskCache()
        }
        is FeedSyncFrequencyChanged -> flow {
            settingsUseCase.setFeedSyncFrequency(action.frequency.toInt())
            settingsUseCase.setShouldPerformPeriodicFullSync(action.frequency != action.upperLimit)
            feedWorkScheduler.scheduleFeedRefreshPeriodic(CANCEL_AND_REENQUEUE)
            effect(ShowMessage(string.feed_sync_freq_changed))
        }
        AskForFullFeedSync -> flowOf(ShowFullFeedSyncDialog)
        DismissFullFeedSyncDialog -> flowOf(HideFullFeedSyncDialog)
        PerformFullFeedSync -> flow {
            feedWorkScheduler.scheduleFeedRefreshNow(shallow = false)
            emit(HideFullFeedSyncDialog)
        }
        CancelFullFeedSync -> flow {
            feedWorkScheduler.cancelFullFeedSync()
        }
        is ChangeFullSyncNetworkRequirements -> flow {
            settingsUseCase.setFullSyncNetworkRequirements(action.networkType)
            feedWorkScheduler.scheduleFeedRefreshPeriodic(CANCEL_AND_REENQUEUE)
            effect(ShowMessage(string.feed_sync_network_changed))
        }
        is ChangeFullSyncChargingRequirements -> flow {
            settingsUseCase.setFullSyncRequiresCharging(action.requiredCharging)
            feedWorkScheduler.scheduleFeedRefreshPeriodic(CANCEL_AND_REENQUEUE)
            effect(ShowMessage(string.feed_sync_charging_changed))
        }
        is ChangeThemeMode -> flow {
            settingsUseCase.setThemeMode(action.themeMode)
        }
        is ChangeSearchSuggestionsEnabled -> flow {
            settingsUseCase.setSearchSuggestionsEnabled(action.enabled)
        }
        is ChangeShareGpsDataEnabled -> flow {
            settingsUseCase.setShareRemoveGpsData(action.enabled)
        }
        is ChangeShowLibrary -> flow {
            settingsUseCase.setShowLibrary(action.show)
        }
        ClearLogFileClicked -> flow {
            feedbackUseCase.clearLogs()
            effect(ShowMessage(string.logs_cleared))
        }
        SendFeedbackClicked -> flow {
            feedbackUseCase.sendFeedback()
        }
        ClearRecentSearches -> flow {
            searchUseCase.clearRecentSearchSuggestions()
            effect(ShowMessage(string.recent_searches_cleared))
        }
        is ChangeMapProvider -> flow {
            settingsUseCase.setMapProvider(action.mapProvider)
        }
        is ChangeLoggingEnabled -> flow {
            settingsUseCase.setLoggingEnabled(action.enabled)
        }
        is FeedRefreshChanged -> flow {
            settingsUseCase.setFeedFeedDaysToRefresh(action.days)
        }
        is ChangeBiometricsAppAccessRequirement -> authenticateIfNeeded(action.required) {
            settingsUseCase.setBiometricsRequiredForAppAccess(it)
        }
        is ChangeBiometricsHiddenPhotosAccessRequirement -> authenticateIfNeeded(action.required) {
            settingsUseCase.setBiometricsRequiredForHiddenPhotosAccess(it)
        }
        is ChangeBiometricsTrashAccessRequirement -> authenticateIfNeeded(action.required) {
            settingsUseCase.setBiometricsRequiredForTrashAccess(it)
        }
        EnrollToBiometrics -> flow {
            effect(SettingsEffect.EnrollToBiometrics)
        }
        is ChangeMemoriesEnabled -> flow {
            settingsUseCase.setMemoriesEnabled(action.enabled)
        }
        is ChangeAnimateVideoThumbnails -> flow {
            settingsUseCase.setAnimateVideoThumbnails(action.animate)
        }
        is ChangeMaxAnimatedVideoThumbnails -> flow {
            settingsUseCase.setMaxAnimatedVideoThumbnails(action.max)
        }
        AskForPrecacheThumbnails -> flowOf(ShowPrecacheThumbnailsDialog)
        DismissPrecacheThumbnailsDialog -> flowOf(HidePrecacheThumbnailsDialog)
        PrecacheThumbnails -> flow {
            feedWorkScheduler.schedulePrecacheThumbnailsNow()
            emit(HidePrecacheThumbnailsDialog)
        }
        CancelPrecacheThumbnails -> flow {
            feedWorkScheduler.cancelPrecacheThumbnails()
        }
    }

    private fun enrollment(
        app: Boolean,
        hiddenPhotos: Boolean,
        trash: Boolean,
    ) = when (biometricsUseCase.getBiometrics()) {
        Enrolled -> BiometricsSetting.Enrolled(app, hiddenPhotos, trash)
        NotEnrolled -> BiometricsSetting.NotEnrolled
        NoHardware -> null
    }

    private fun authenticateIfNeeded(
        required: Boolean,
        change: suspend (Boolean) -> Unit,
    ) = flow<SettingsMutation> {
        val proceed = when {
            required -> Result.success(Unit)
            else -> biometricsUseCase.authenticate(
                string.authenticate,
                string.authenticate_to_change,
                string.authenticate_to_change_description,
                true,
            )
        }
        if (proceed.isSuccess) {
            change(required)
        }
    }

}
