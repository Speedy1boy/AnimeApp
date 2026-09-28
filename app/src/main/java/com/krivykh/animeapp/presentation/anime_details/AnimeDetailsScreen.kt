package com.krivykh.animeapp.presentation.anime_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.krivykh.animeapp.domain.model.Anime

@Composable
fun AnimeDetailsScreen(
    viewModel: AnimeDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage ?: "Unknown Error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.anime != null -> {
                AnimeDetailsContent(anime = uiState.anime!!)
            }
        }
    }
}

@Composable
fun AnimeDetailsContent(anime: Anime) {
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        val (poster, title, status, synopsisLabel, synopsis, info) = createRefs()

        AsyncImage(
            model = anime.posterImageUrl,
            contentDescription = anime.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(140.dp)
                .height(200.dp)
                .constrainAs(poster) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = anime.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(poster.top)
                start.linkTo(poster.end, margin = 16.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = "Status: ${anime.status}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(title.bottom, margin = 8.dp)
                start.linkTo(title.start)
            }
        )

        Text(
            text = "Synopsis",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.constrainAs(synopsisLabel) {
                top.linkTo(poster.bottom, margin = 24.dp)
                start.linkTo(parent.start)
            }
        )

        Text(
            text = anime.synopsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(synopsis) {
                top.linkTo(synopsisLabel.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        Text(
            text = "Episodes: ${anime.episodeCount} | Rating: ${anime.averageRating}",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(info) {
                top.linkTo(synopsis.bottom, margin = 24.dp)
                start.linkTo(parent.start)
            }
        )
    }
}