package geronimo.don.standup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.app.PendingIntent
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity() {
    private var toastMessage = ""
    private lateinit var  mNotificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        alarmToggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                toastMessage=getString(R.string.toast_on)
                deliverNotification(this)
            }
            else{
                mNotificationManager.cancelAll()
                toastMessage=getString(R.string.toast_off)
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel(){
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "Stand Up Notification",
                NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description="Notifies me every 15 min to stand up and walk"
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun deliverNotification(context: Context){
        val contentIntent = Intent(context,MainActivity::class.java)
        val contentPendingIntent =
            PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setContentTitle("Stand up alert")
            .setContentText("LEVANTA E ANDA")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        mNotificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        val NOTIFICATION_ID = 0
        val PRIMARY_CHANNEL_ID="primary_notification_channel"
    }
}
