package ru.yandex.repinanr.movies.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.yandex.repinanr.movies.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        var id = 0

        Log.d(TAG, "From: ${message?.from}")

        message.data?.let {
            Log.d(TAG, "Message data: " + message.data)
            id = it.get(MOVIE_ID).toString().toInt()
        }

        message.notification?.let {
            generateNotification(
                it.title ?: throw RuntimeException("Empty title"),
                it.body ?: throw RuntimeException("Empty message"),
                id
            )
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String, movieId: Int): RemoteViews {
        val remoteViews =
            RemoteViews(getPackageName(), R.layout.notification)

        remoteViews.setTextViewText(R.id.notificationTitle, title)
        remoteViews.setTextViewText(R.id.notificationText, message)

        return remoteViews
    }

    fun generateNotification(title: String, message: String, movieId: Int) {
        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager

        val notificationChannel = NotificationChannel(
            CHANEL_ID,
            CHANEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = CHANEL_NAME
        createNotificationChanel(notificationManager)

        val remoteViews = getRemoteView(title, message, movieId)

        notificationManager.sendNotification(
            remoteViews = remoteViews,
            context = this,
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

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val CHANEL_ID = "2"
        private const val CHANEL_NAME = "firebase"
        private const val NOTIFICATION_ID = 2
        private const val MOVIE_ID = "id"
        private const val TAG = "MyFirebaseMessagingService"
    }
}