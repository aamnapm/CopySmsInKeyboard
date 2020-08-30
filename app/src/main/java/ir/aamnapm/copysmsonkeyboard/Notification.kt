package ir.aamnapm.copysmsonkeyboard


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat


class Notification(private var context: Context) {

    private lateinit var manager: NotificationManager

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.enableLights(true)
            serviceChannel.lightColor = Color.RED

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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(createRemoteView(message))
                .setContentIntent(pendingIntent)
                .build()
        )
    }

    fun createNotification(message: String?, pendingIntent: PendingIntent): Notification? {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContent(createRemoteView(message))
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createRemoteView(message: String?): RemoteViews {
        val contentView = RemoteViews(context.packageName, R.layout.notification)
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher)
        contentView.setTextViewText(R.id.title, context.getString(R.string.sms_reader))
        contentView.setTextViewText(R.id.text, message)
        return contentView
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }

}