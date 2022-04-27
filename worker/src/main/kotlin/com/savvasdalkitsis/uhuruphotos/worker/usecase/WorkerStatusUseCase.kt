package com.savvasdalkitsis.uhuruphotos.worker.usecase

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.savvasdalkitsis.uhuruphotos.infrastructure.coroutines.onMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkerStatusUseCase @Inject constructor(
    private val workManager: WorkManager,
) {

    fun monitorUniqueJobStatus(jobName: String): Flow<WorkInfo.State> =
        monitorUniqueJob(jobName).map { it.state }

    fun monitorUniqueJob(jobName: String): Flow<WorkInfo> {
        var observer: ((MutableList<WorkInfo>) -> Unit)?
        var liveData: LiveData<MutableList<WorkInfo>>?
        return channelFlow {
            observer = {
                val workInfo = it.getOrNull(0)
                workInfo?.let {
                    CoroutineScope(Dispatchers.Default).launch {
                        send(it)
                    }
                }
            }
            liveData = workManager.getWorkInfosForUniqueWorkLiveData(jobName)
            onMain {
                liveData!!.observeForever(observer!!)
            }
            awaitClose {
                onMain {
                    liveData!!.removeObserver(observer!!)
                }
            }
        }.cancellable()
    }
}