package com.example.pdfer.ui.main_screen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.pdfer.data.workers.PdfDownloaderWorker
import com.example.pdfer.data.workers.getFileName
import com.example.pdfer.util.Constants.DOWNLOAD_PDF_WORK
import com.example.pdfer.util.Constants.PDF_FILE_KEY
import com.example.pdfer.util.Constants.PDF_URL_DOWNLOAD
import com.example.pdfer.util.Constants.TEST_URL
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    var urlText by mutableStateOf("")
        private set

    fun onUrlTextChange(value: String) {
        urlText = value
    }

    private var workManager = WorkManager.getInstance(application)
    var downloadPdfWorkerId: UUID? = null
        private set

    private var outputFile: File? = null

    val workInfoLiveData: LiveData<MutableList<WorkInfo>>
        get() = workManager.getWorkInfosByTagLiveData(DOWNLOAD_PDF_WORK)

    fun doWork(isTest: Boolean = false) = viewModelScope.launch {

        val url = if (isTest) TEST_URL else urlText.ifEmpty { return@launch }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val pdfDownloaderWork = OneTimeWorkRequest.Builder(PdfDownloaderWorker::class.java)
            .addTag(DOWNLOAD_PDF_WORK)
            .setInputData(
                Data.Builder()
                    .putString(PDF_FILE_KEY, urlText.getFileName())
                    .putString(
                        PDF_URL_DOWNLOAD,
                        url
                    )
                    .build()
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .build()
        downloadPdfWorkerId = pdfDownloaderWork.id

        workManager.enqueueUniqueWork(
            urlText.getFileName(),
            ExistingWorkPolicy.KEEP,
            pdfDownloaderWork
        )

    }

    fun setOutputFile(fileUri: String) {
        outputFile = File(URI.create(fileUri))
    }

}