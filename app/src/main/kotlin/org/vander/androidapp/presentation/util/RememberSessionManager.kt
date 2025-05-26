package org.vander.androidapp.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.EntryPointAccessors
import org.vander.androidapp.di.SpotifySessionEntryPoint
import org.vander.spotifyclient.domain.SpotifySessionManager

@Composable
fun rememberSpotifySessionManager(): SpotifySessionManager {
    val context = LocalContext.current
    return remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            SpotifySessionEntryPoint::class.java
        ).spotifySessionManager()
    }
}