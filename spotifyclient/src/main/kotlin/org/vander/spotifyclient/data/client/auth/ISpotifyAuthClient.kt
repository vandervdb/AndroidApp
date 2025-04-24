package org.vander.spotifyclient.data.client.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher

interface ISpotifyAuthClient {
    fun authorize(contextActivity: Activity, launcher: ActivityResultLauncher<Intent>)
    fun handleSpotifyAuthResult(result: ActivityResult): ConnectionState
}