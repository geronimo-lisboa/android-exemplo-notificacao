package geronimo.don.standup

import android.Manifest
import android.app.AlarmManager
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
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import android.os.SystemClock
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient




class MainActivity : AppCompatActivity() {
    private var toastMessage = ""
    private lateinit var  mNotificationManager: NotificationManager

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_COARSE_LOCATION->{
                if(grantResults.size >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d("TESTE", "Permissáo concedida")
                }
                else{
                    Toast.makeText(this, "SEM PERMISSÁO DE LOCALIZAÇAO", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if(permissionCheck!=PackageManager.PERMISSION_GRANTED){
            Log.d("TESTE", "sem permissao")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
        }

        createNotificationChannel()
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        val alarmUp = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE)!=null
        alarmToggle.isChecked=alarmUp

        val notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        alarmToggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                toastMessage=getString(R.string.toast_on)
                val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
                val triggerTime = SystemClock.elapsedRealtime() + repeatInterval
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime, repeatInterval,notifyPendingIntent)
            }
            else{
                mNotificationManager.cancelAll()
                alarmManager.cancel(notifyPendingIntent)
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


    companion object {
        val NOTIFICATION_ID = 0
        val PRIMARY_CHANNEL_ID="primary_notification_channel"
        val REQUEST_COARSE_LOCATION = 999
    }
}
