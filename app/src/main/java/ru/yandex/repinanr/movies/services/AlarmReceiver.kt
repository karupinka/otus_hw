package ru.yandex.repinanr.movies.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.getSystemService
import com.example.android.eggtimernotifications.util.sendNotification
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.presentation.dialog.DateDialog.Companion.ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val id: Int = intent?.getIntExtra(ID, 0) ?: 0

            generateNotification(
                it.getString(R.string.notification_title),
                it.getString(R.string.notification_message),
                id,
                it
            )
        }
    }

    fun generateNotification(title: String, message: String, movieId: Int, context: Context) {
        val notificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager

        val notificationChannel = NotificationChannel(
            CHANEL_ID,
            CHANEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.description = CHANEL_NAME
        createNotificationChanel(notificationManager)

        val remoteViews = getRemoteView(title, message, movieId, context)

        notificationManager.sendNotification(
            remoteViews = remoteViews,
            context = context,
            movieId = movieId,
            channelId = CHANEL_ID,
            notificationId = NOTIFICATION_ID
        )
    }

    private fun createNotificationChanel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANEL_ID,
            CHANEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(notificationChannel)
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String, movieId: Int, context: Context): RemoteViews {
        val remoteViews =
            RemoteViews(context.getPackageName(), R.layout.notification)

        remoteViews.setTextViewText(R.id.notificationTitle, title)
        remoteViews.setTextViewText(R.id.notificationText, message)

        return remoteViews
    }

    companion object {
        private const val CHANEL_ID = "1"
        private const val CHANEL_NAME = "alarm"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}