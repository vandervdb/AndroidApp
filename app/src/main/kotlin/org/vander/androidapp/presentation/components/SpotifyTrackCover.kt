package org.vander.androidapp.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import org.vander.androidapp.data.util.SPOTIFY_COVER_UI

@Composable
fun SpotifyTrackCover(
    imageUri: String,
    modifier: Modifier = Modifier
) {
    val url = "${SPOTIFY_COVER_UI}${imageUri}"
    Log.d("SpotifyTrackCover", "url: $url")
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = rememberVectorPainter(image = Icons.Default.Audiotrack),
            error = rememberVectorPainter(image = Icons.Default.Error),
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
