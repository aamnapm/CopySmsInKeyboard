package ir.aamnapm.copysmsonkeyboard

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi

class ForegroundService : Service() {

    lateinit var context: Context
    lateinit var mSMSReceiver: SMSListener
    lateinit var broadcastReceiver: BroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        context = this

        var notifcation = Notification(context)

        notifcation.createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        var notification = notifcation.createNotification("", pendingIntent)
        startForeground(1, notification)

        onReceiveMessage(notifcation, pendingIntent)

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        reciveSmsOnService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(mSMSReceiver)
        context.unregisterReceiver(broadcastReceiver)
    }


    private fun reciveSmsOnService() {
        mSMSReceiver = SMSListener()
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(mSMSReceiver, mIntentFilter)
    }


    private fun onReceiveMessage(
        notification: Notification,
        pendingIntent: PendingIntent
    ) {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                notification.updateNotification(
                    getString(R.string.secret) + intent.getStringExtra("DATA"),
                    pendingIntent
                )
                var handler = Handler()
                handler.postDelayed({
                    notification.updateNotification(
                        "",
                        pendingIntent
                    )
                }, 120000)
            }
        }
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction("SMS")
        context.registerReceiver(broadcastReceiver, mIntentFilter)
    }
}