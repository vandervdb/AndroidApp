package org.vander.spotifyclient.utils

import io.ktor.http.URLBuilder
import org.vander.spotifyclient.BuildConfig.CLIENT_ID
import kotlin.random.Random

internal fun generateRandomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}

fun getAuthorizationUrl(): String {
    val state = generateRandomString(16)

    return URLBuilder(HTTPS_ACCOUNTS_SPOTIFY_COM_AUTHORIZE).apply {
        parameters.append("response_type", "code")
        parameters.append("client_id", CLIENT_ID)
        parameters.append("redirect_uri", REDIRECT_URI)
        parameters.append("scope", USER_READ_PRIVATE)
        parameters.append("state", state)
    }.buildString()
}