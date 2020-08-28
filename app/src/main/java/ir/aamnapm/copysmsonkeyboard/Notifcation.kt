package ir.aamnapm.copysmsonkeyboard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


class Notifcation(var context: Context) {

    lateinit var manager: NotificationManager


    @RequiresApi(Build.VERSION_CODES.M)
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "ForegroundServiceChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        } else
            manager = context.getSystemService(NotificationManager::class.java)
    }

    fun updateNotification(message: String, pendingIntent: PendingIntent) {
        manager.notify(
            1, NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Read Bank Sms")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build()
        )
    }

    fun createNotification(message: String?, pendingIntent: PendingIntent): Notification? {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Read Bank Sms")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }

}