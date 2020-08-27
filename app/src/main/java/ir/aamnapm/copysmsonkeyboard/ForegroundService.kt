package ir.aamnapm.copysmsonkeyboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val input = intent!!.getStringExtra("inputExtra")

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Read Bank Sms")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val mSMSreceiver = SMSListener()
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(mSMSreceiver, mIntentFilter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "ForegroundServiceChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val  CHANNEL_ID = "ForegroundServiceChannel"
    }
}