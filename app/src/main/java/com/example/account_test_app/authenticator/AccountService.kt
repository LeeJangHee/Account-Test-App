package com.example.account_test_app.authenticator

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.account_test_app.Constants
import com.example.account_test_app.Constants.NOTIFICATION_ID
import com.example.account_test_app.R

class AccountService : Service() {
    private val TAG = this.javaClass.simpleName
    private var binder: TestAuthenticator? = null

    override fun onCreate() {
        super.onCreate()
        binder = TestAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder?.iBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")

        val serviceName = Constants.ACCOUNT_TYPE
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroup(
            NotificationChannelGroup("TasService", "TAS")
        )
        val serviceChannel = NotificationChannel(
            serviceName,
            serviceName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(serviceChannel)

        val notification: Notification = Notification.Builder(this, serviceName)
            .setContentTitle(serviceName)
            .setContentText(serviceName)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("ACCOUNT_TEST Service")
            .build()

        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }
}