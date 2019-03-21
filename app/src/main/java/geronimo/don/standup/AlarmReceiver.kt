package geronimo.don.standup

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var mNotificationManager: NotificationManager
    override fun onReceive(context: Context, intent: Intent) {
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context){
        val contentIntent = Intent(context,MainActivity::class.java)
        val contentPendingIntent =
            PendingIntent.getActivity(context, MainActivity.NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, MainActivity.PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setContentTitle("Stand up alert")
            .setContentText("LEVANTA E ANDA")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        mNotificationManager.notify(MainActivity.NOTIFICATION_ID, builder.build())
    }

    companion object {
        val NOTIFICATION_ID = 0
        val PRIMARY_CHANNEL_ID="primary_notification_channel"
    }}
