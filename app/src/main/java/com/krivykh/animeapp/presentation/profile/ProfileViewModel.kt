package com.krivykh.animeapp.presentation.profile

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivykh.animeapp.data.local.ProfileDataStore
import com.krivykh.animeapp.di.AppModule
import com.krivykh.animeapp.domain.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class ProfileViewModel : ViewModel() {

    private val dataStore: ProfileDataStore = AppModule.profileDataStore

    val profile: StateFlow<UserProfile> = dataStore.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserProfile()
        )

    fun saveProfile(fullName: String, jobTitle: String, avatarUri: String, resumeUrl: String) {
        viewModelScope.launch {
            dataStore.saveProfile(
                UserProfile(
                    fullName = fullName,
                    jobTitle = jobTitle,
                    avatarUri = avatarUri,
                    resumeUrl = resumeUrl
                )
            )
        }
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
                setDescription("Скачивание файла...")
                setMimeType("application/pdf")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "resume.pdf")
            }

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(context, "Скачивание началось...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка: проверьте ссылку или подключение", Toast.LENGTH_LONG).show()
        }
    }
}