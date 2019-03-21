package geronimo.don.standup

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var mNotificationManager: NotificationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location?->
//            val lat = location?.latitude
//            val lon = location?.longitude
//            Log.d("TESTE", "$lat : $lon")
//        }

        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        deliverNotification(context)
    }

    @SuppressLint("MissingPermission")
    private fun deliverNotification(context: Context){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location?->
            val lat = location?.latitude
            val lon = location?.longitude
            Log.d("TESTE", "$lat : $lon")
            //...
            val gmmIntentUri = Uri.parse("geo:$lat,$lon")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            //val contentIntent = Intent(context,MainActivity::class.java)
            val contentPendingIntent =
                PendingIntent.getActivity(context, MainActivity.NOTIFICATION_ID, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = NotificationCompat.Builder(context, MainActivity.PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("Stand up alert")
                .setContentText("Localização: $lat : $lon")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
            mNotificationManager.notify(MainActivity.NOTIFICATION_ID, builder.build())

        }
    }

    companion object {
        val NOTIFICATION_ID = 0
        val PRIMARY_CHANNEL_ID="primary_notification_channel"
    }}
