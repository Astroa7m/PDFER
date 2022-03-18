package com.example.pdfer.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.pdfer.data.api.PdfDownloader
import com.example.pdfer.util.Constants.FILE_URI_KEY
import com.example.pdfer.util.Constants.FOLDER_NAME
import com.example.pdfer.util.Constants.PDF_FILE_KEY
import com.example.pdfer.util.Constants.PDF_URL_DOWNLOAD
import com.example.pdfer.util.Constants.WORKER_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Worker that utilizes our HTTP network call and observes the download state
 * as well as saving it into local app file and handle notification
 */


@Suppress("BlockingMethodInNonBlockingContext")
class PdfDownloaderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val filename = inputData.getString(PDF_FILE_KEY) ?: "pdf-file.pdf"

                //starting foreground service
                handleNotification(context, filename)

                //making new dir to save pdfs in
                val pdfFolderPath = "${context.applicationContext.filesDir}/$FOLDER_NAME"
                File(pdfFolderPath).apply { if (exists().not()) mkdir() }
                val pdfFile = File(pdfFolderPath, filename)

                //getting the file url from the text input or using the test url instead
                val pdfUrlToDownload = inputData.getString(PDF_URL_DOWNLOAD)


                //making network call and writing into stream of bytes into a file
                //also passing the url dynamically
                val result =
                    PdfDownloader.instance.downloadPdf(pdfUrlToDownload!!).body()?.let { body ->
                        body.byteStream().use { inputStream ->
                            FileOutputStream(pdfFile).use { outputStream ->
                                val totalBytes = body.contentLength()
                                val data = ByteArray(8_192)
                                var progressBytes = 0L

                                while (true) {
                                    val bytes = inputStream.read(data)

                                    if (bytes == -1) {
                                        break
                                    }

                                    outputStream.write(data, 0, bytes)
                                    progressBytes += bytes
                                    //we could use this in order to observe the progress from our livedata info of the worker
                                    //setProgress(workDataOf("progressKey" to ((100* progressBytes)/totalBytes).toInt()))
                                    updateNotificationProgress(
                                        context,
                                        ((100 * progressBytes) / totalBytes).toInt(),
                                        filename,
                                        id
                                    )
                                }

                            }
                        }
                        val output = workDataOf(FILE_URI_KEY to pdfFile.toURI().toString())
                        Result.success(output)
                    } ?: Result.failure(workDataOf(WORKER_ERROR to "Unknown error"))
                result

            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(workDataOf(WORKER_ERROR to (e.localizedMessage ?: "Unknown error")))
            }
        }
    }


    //to add compatibility prior to Android 12
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val filename = inputData.getString(PDF_FILE_KEY) ?: "pdf-file.pdf"
        return ForegroundInfo(
            filename.hashCode(),
            getNotification(context, filename, id).build()
        )
    }
}