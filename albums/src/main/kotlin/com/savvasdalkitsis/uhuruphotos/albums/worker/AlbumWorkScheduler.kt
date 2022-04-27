package com.savvasdalkitsis.uhuruphotos.albums.worker

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import com.savvasdalkitsis.uhuruphotos.albums.api.worker.AlbumWorkScheduler
import com.savvasdalkitsis.uhuruphotos.albums.api.worker.RefreshJobState
import com.savvasdalkitsis.uhuruphotos.settings.usecase.SettingsUseCase
import com.savvasdalkitsis.uhuruphotos.worker.WorkScheduler
import com.savvasdalkitsis.uhuruphotos.worker.usecase.WorkerStatusUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AlbumWorkScheduler @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val workerStatusUseCase: WorkerStatusUseCase,
    private val settingsUseCase: SettingsUseCase,
) : AlbumWorkScheduler {

    override fun scheduleAlbumsRefreshNow(shallow: Boolean) =
        workScheduler.scheduleNow<AlbumDownloadWorker>(AlbumDownloadWorker.WORK_NAME) {
            putBoolean(AlbumDownloadWorker.KEY_SHALLOW, shallow)
        }

    override fun scheduleAlbumsRefreshPeriodic(
        existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy
    ) {
        if (settingsUseCase.getShouldPerformPeriodicFullSync()) {
            workScheduler.schedulePeriodic<AlbumDownloadWorker>(
                AlbumDownloadWorker.WORK_NAME,
                repeatInterval = settingsUseCase.getFeedSyncFrequency().toLong(),
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                initialDelayDuration = 1,
                initialDelayTimeUnit = TimeUnit.HOURS,
                existingPeriodicWorkPolicy = existingPeriodicWorkPolicy,
                networkRequirement = settingsUseCase.getFullSyncNetworkRequirements(),
                requiresCharging = settingsUseCase.getFullSyncRequiresCharging(),
            )
        } else {
            workScheduler.workManager.cancelUniqueWork(AlbumDownloadWorker.WORK_NAME)
        }
    }

    override fun observeAlbumRefreshJob(): Flow<RefreshJobState> =
        workerStatusUseCase.monitorUniqueJob(AlbumDownloadWorker.WORK_NAME).map {
            RefreshJobState(
                status = it.state,
                progress = it.progress.getInt(AlbumDownloadWorker.Progress, 0)
            )
        }
}