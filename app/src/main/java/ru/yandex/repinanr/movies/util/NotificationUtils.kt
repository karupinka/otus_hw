/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.presentation.common.MoviesActivity

private val REQUEST_CODE = 0

fun NotificationManager.sendNotification(
    remoteViews: RemoteViews,
    context: Context,
    movieId: Int,
    channelId: String,
    notificationId: Int
) {

    val intent = Intent(context, MoviesActivity::class.java).apply {
        flags = FLAG_ACTIVITY_CLEAR_TOP
        putExtra("id", movieId)
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        intent,
        PendingIntent.FLAG_ONE_SHOT
    )

    val builder = NotificationCompat.Builder(
        context,
        channelId
    )
        .setContent(remoteViews)
        .setSmallIcon(R.drawable.icons_alarm)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(notificationId, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
