package org.vander.androidapp.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
        modifier = modifier.clip(RoundedCornerShape(topEnd = 4.dp , topStart = 4.dp, bottomEnd = 4.dp, bottomStart = 4.dp)),
        contentScale = ContentScale.Crop
    )
}
