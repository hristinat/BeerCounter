package com.example.beercounter.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.beercounter.MainActivity
import com.example.beercounter.R

// Notification ID.
private const val NOTIFICATION_ID = 0
private const  val REQUEST_CODE = 0
private const val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity

    val intent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    // Build the notification

//    val eggImage = BitmapFactory.decodeResource(
//        applicationContext.resources,
//        R.drawable.egg_icon
//    )

//    val bigPictureStyle = NotificationCompat.BigPictureStyle()
//        .bigPicture(eggImage)
//        .bigLargeIcon(null)

//    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)

//    val snoozePendingIntent = PendingIntent.getBroadcast(
//        applicationContext,
//        REQUEST_CODE,
//        snoozeIntent,
//        FLAGS
//    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.beer_notification_channel_id)
    ).setSmallIcon(R.drawable.ic_baseline_sports_bar_24)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}