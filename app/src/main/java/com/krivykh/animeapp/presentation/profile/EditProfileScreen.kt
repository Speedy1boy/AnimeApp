package com.krivykh.animeapp.presentation.profile

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }

    var showPhotoDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(profile) {
        fullName = profile.fullName
        jobTitle = profile.jobTitle
        resumeUrl = profile.resumeUrl
        avatarUri = profile.avatarUri
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempCameraUri != null) {
            avatarUri = tempCameraUri.toString()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            avatarUri = uri.toString()
        }
    }

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Нужен доступ к галерее", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val uri = createTempPictureUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Нужен доступ к камере", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { showPhotoDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri.isNotBlank()) {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = "Аватар",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                label = { Text("Должность") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("Ссылка на резюме (.pdf)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveProfile(fullName, jobTitle, avatarUri, resumeUrl)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }

        if (showPhotoDialog) {
            AlertDialog(
                onDismissRequest = { showPhotoDialog = false },
                title = { Text("Выберите фото") },
                text = { Text("Откуда загрузить аватар?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showPhotoDialog = false
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    ) {
                        Text("Камера")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showPhotoDialog = false
                            galleryPermissionLauncher.launch(storagePermission)
                        }
                    ) {
                        Text("Галерея")
                    }
                }
            )
        }
    }
}

fun createTempPictureUri(context: Context): Uri {
    val file = File(context.cacheDir, "temp_avatar.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}