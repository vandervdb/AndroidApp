package org.vander.spotifyclient.data.client.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationClient.createLoginActivityIntent
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import org.vander.spotify.data.utils.CLIENT_ID
import org.vander.spotify.data.utils.REDIRECT_URI
import org.vander.spotifyclient.data.client.auth.ConnectionState.Connected
import org.vander.spotifyclient.data.client.auth.ConnectionState.Failed

class SpotifyClient() : ISpotifyAuthClient {
    companion object {
        private const val TAG = "SpotifyClient"
    }

    private var spotifyAppRemote: SpotifyAppRemote? = null


    /**
     * Initiates the authorization flow using AppAuth.
     *
     * This function builds an AuthorizationRequest with the specified client ID, redirect URI, and scopes.
     * It then creates an intent to launch the login activity and triggers the authorization process
     * via the provided ActivityResultLauncher.
     *
     * @param contextActivity The Activity context used to create the login intent.
     * @param launcher An ActivityResultLauncher responsible for launching the login activity and
     *                 receiving the result of the authorization process.
     *
     * @throws IllegalStateException if 'connected' is null. This indicates that the authorization
     *                               service configuration is not yet available or an error occurred
     *                               while obtaining it.
     *
     * @see AuthorizationRequest
     * @see ActivityResultLauncher
     * @see AuthorizationResponse.Type.CODE
     * @see createLoginActivityIntent
     *
     */
    override fun authorize(contextActivity: Activity, launcher: ActivityResultLauncher<Intent>) {
        val request = AuthorizationRequest.Builder(
            CLIENT_ID,
            AuthorizationResponse.Type.CODE,
            REDIRECT_URI
        ).apply {
            setScopes(arrayOf("streaming"))
        }.build()

        val intent = createLoginActivityIntent(contextActivity, request)
        launcher.launch(intent)
    }


    /**
     * Handles the result of a Spotify authentication activity.
     *
     * This function processes the `ActivityResult` received after a Spotify login or authorization attempt.
     * It checks the result code and data to determine the outcome of the authentication.
     *
     * @param result The `ActivityResult` object containing the result code and data from the Spotify authentication activity.
     * @return A `ConnectionState` object representing the state of the connection to Spotify.
     *         - `Connected`: If the authentication was successful and an authorization code was received.
     *           It contains the authorization code.
     *         - `Failed`: If the authentication failed, either due to an error or missing data.
     *           It contains an `Exception` describing the error.
     *
     * @throws Exception if :
     *          - No data is received in the result.
     *          - the response type is not CODE.
     *          - The result code is not OK
     *
     * The possible scenario are :
     * - result code is `Activity.RESULT_OK` and `result.data` contains the authorization response with type CODE : Return `Connected` with authorization code
     * - result code is `Activity.RESULT_OK` and `result.data` contains the authorization response with an error : Return `Failed` with the error message.
     * - result code is `Activity.RESULT_OK` and `result.data` is null : Return `Failed` with the message "Aucune donnée reçue".
     * - result code is not `Activity.RESULT_OK` : Return `Failed` with the result code.
     *
     * Logs are written to the console with the tag "TAG" to provide debugging information at every step.
     */
    override fun handleSpotifyAuthResult(result: ActivityResult): ConnectionState {
        Log.d(TAG, "handleSpotifyAuthResult: $result")

        return if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data == null) {
                Log.e(TAG, "Aucune donnée reçue dans le résultat d'activité")
                Failed(Exception("Aucune donnée reçue"))
            }
            Log.d(TAG, "Résultat OK: $data")
            val response = AuthorizationClient.getResponse(result.resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.CODE -> {
                    Log.d(TAG, "Code d'autorisation reçu: ${response.code}")
                    Connected(response.code)
                }

                else -> {
                    Log.e(TAG, "Erreur dans la réponse: ${response.error}")
                    Failed(Exception(response.error))
                }
            }
        } else {
            Log.e(TAG, "Erreur lors de la connexion à Spotify, code résultat: ${result.resultCode}")
            Failed(Exception(result.resultCode.toString()))
        }
    }


}

sealed class ConnectionState {
    data class Connected(val accessToken: String) : ConnectionState()
    data class Disconnected(val reason: String) : ConnectionState()
    object Connecting : ConnectionState()
    data class Failed(val exception: Exception) : ConnectionState()
}