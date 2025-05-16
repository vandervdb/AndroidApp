package org.vander.androidapp.presentation.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.vander.androidapp.R
import org.vander.androidapp.presentation.viewmodel.SpotifyViewModel
import org.vander.androidapp.ui.theme.SpotifyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyScreenWrapper(
    navController: NavController,
    spotifyViewModel: SpotifyViewModel,
    launcher: ActivityResultLauncher<Intent>,
    activity: Activity
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.spotify_full_logo_black),
                        contentDescription = "Spotify Logo",
                        modifier = Modifier
                            .height(32.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SpotifyGreen,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        SpotifyScreen(
            navController = navController,
            viewModel = spotifyViewModel,
            launcher = launcher,
            activity = activity,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
