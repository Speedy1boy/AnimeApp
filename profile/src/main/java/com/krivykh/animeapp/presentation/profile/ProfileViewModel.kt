package com.krivykh.animeapp.presentation.profile

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.data.local.ProfileDataStore
import com.krivykh.animeapp.core.CoreProvider
import com.krivykh.animeapp.domain.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import androidx.core.net.toUri

class ProfileViewModel : ViewModel() {
    private val dataStore: ProfileDataStore = CoreProvider.profileDataStore
    private val fullTimeRegex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
    private val partialTimeRegex = Regex("^([01]?\\d?|2[0-3]?)(:([0-5]\\d?)?)?$")

    val profile: StateFlow<UserProfile> = dataStore.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserProfile()
        )

    fun isTimeValid(time: String): Boolean {
        return time.trim().matches(fullTimeRegex)
    }

    fun shouldShowTimeError(time: String, isFocused: Boolean): Boolean {
        val trimmed = time.trim()
        if (trimmed.matches(fullTimeRegex)) return false
        return if (isFocused) {
            !trimmed.matches(partialTimeRegex)
        } else {
            true
        }
    }

    fun formatTime(hour: Int, minute: Int): String {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
    }

    fun saveProfile(
        context: Context,
        fullName: String,
        jobTitle: String,
        avatarUri: String,
        resumeUrl: String,
        classTime: String
    ) {
        val cleanTime = classTime.trim()
        if (!isTimeValid(cleanTime)) return

        viewModelScope.launch {
            dataStore.saveProfile(
                UserProfile(
                    fullName = fullName,
                    jobTitle = jobTitle,
                    avatarUri = avatarUri,
                    resumeUrl = resumeUrl,
                    favoriteClassTime = cleanTime
                )
            )
            scheduleNotification(context, fullName, cleanTime)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(context: Context, userName: String, time: String) {
        val parts = time.split(":").map { it.toInt() }
        val hour = parts[0]
        val minute = parts[1]

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_USER_NAME, userName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NotificationReceiver.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun downloadResume(context: Context, url: String) {
        if (url.isBlank()) {
            Toast.makeText(context, "Ссылка на резюме пуста", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val secureUrl = if (url.startsWith("http://")) url.replace("http://", "https://") else url

            val request = DownloadManager.Request(secureUrl.toUri()).apply {
                setTitle("Резюме")
                setDescription("Скачивание файла")
                setMimeType("application/pdf")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "resume.pdf")
            }

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(context, "Скачивание началось", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка: проверьте ссылку или подключение", Toast.LENGTH_LONG).show()
        }
    }
}