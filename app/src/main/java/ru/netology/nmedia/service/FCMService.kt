package ru.netology.nmedia.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random


class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {

        message.data[action]?.let {
            try {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        )
                    )

                    Action.CREATE -> addPost(
                        gson.fromJson(
                            message.data[content],
                            Create::class.java
                        )
                    )
                }
            } catch (e: IllegalArgumentException) {
                showUnknownActionNotification()
            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }


    private fun addPost(content: Create) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_add_post,
                    content.userName,
                    content.postContent
                )
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content.postContent)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }

    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }

    private fun notify(notification: Notification) {
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this)
                .notify(Random.nextInt(100_000), notification)
        }
    }
    private fun showUnknownActionNotification() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Неизвестный тип уведомлений")
            .setContentText("Получено сообщение с неизвестным типом действия.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }
}

enum class Action {
    LIKE, CREATE
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)
data class Create(
    val userName: String,
    val postContent: String
)


