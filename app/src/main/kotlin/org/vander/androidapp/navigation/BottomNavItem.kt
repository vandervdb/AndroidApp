package org.vander.androidapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem (val route: String, val icon: ImageVector, val label: String){
    object Home: BottomNavItem("home", Icons.Default.Home, "Home")
    object Spotify: BottomNavItem("spotify", Icons.Default.LibraryMusic, "Spotify")
}