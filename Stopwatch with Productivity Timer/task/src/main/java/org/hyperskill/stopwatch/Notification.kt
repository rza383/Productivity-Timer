package org.hyperskill.stopwatch
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class NotificationService(private val context: Context) {
    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon_foreground)
        .setContentTitle("Time exceeded")
        .setContentText("Time limit exceeded!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOnlyAlertOnce(true)
        .setAutoCancel(true)

    fun showNotification(){
        val notification = notificationBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_INSISTENT
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(393939, notification)
    }

    companion object{
        const val CHANNEL_ID = "org.hyperskill"
    }
}