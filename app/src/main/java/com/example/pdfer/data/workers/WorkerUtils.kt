package com.example.pdfer.data.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import com.example.CHANNEL_ID
import com.example.pdfer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.yield
import java.net.URLDecoder
import java.util.*

/**
 * Util for our worker class to send or cancel notifications and get file name
 */


suspend fun CoroutineWorker.handleNotification(
    context: Context,
    fileName: String
) {
    //checking for coroutine cancellation
    yield()
    setForeground(
        ForegroundInfo(
            fileName.hashCode(),
            getNotification(
                context,
                fileName,
                id
            ).build()
        )
    )
}

fun getNotification(
    context: Context,
    fileName: String,
    workerId: UUID
): NotificationCompat.Builder {

    val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(workerId)

    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Downloading")
        .setContentText("Started Downloading $fileName file")
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .setAutoCancel(false)
        .setOnlyAlertOnce(true)
        .addAction(R.drawable.ic_cancel, "Cancel", cancelIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
}

fun CoroutineScope.updateNotificationProgress(
    context: Context,
    progress: Int,
    fileName: String,
    workerId: UUID
) {
    with(NotificationManagerCompat.from(context)) {
        //checking for coroutine cancellation if the user cancelled the download when it's in progress
        if (isActive) {
            notify(
                fileName.hashCode(),
                getNotification(context, fileName, workerId).setProgress(100, progress, false)
                    .build()
            )
        } else {
            with(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
                cancel(
                    fileName.hashCode()
                )
            }
        }
    }
}

//getting file name from the url after last forward slash
fun String.getFileName() = URLDecoder.decode(this, "UTF-8").substringAfterLast('/', "just_pdf.pdf")
