package org.vander.spotifyclient.utils

import android.util.Log
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.vander.spotifyclient.data.remote.dto.SpotifyErrorResponseDto

suspend inline fun <reified T> HttpResponse.parseSpotifyResult(
    tag: String = "SpotifyApi"
): Result<T> {
    val rawBody = this.bodyAsText()
    val json = Json { ignoreUnknownKeys = true }

    return try {
        val root = json.parseToJsonElement(rawBody).jsonObject

        if ("error" in root) {
            val errorDto = json.decodeFromString<SpotifyErrorResponseDto>(rawBody)
            Log.e(tag, "Spotify error ${errorDto.error.status} : ${errorDto.error.message}")
            Result.failure(Exception("Spotify error ${errorDto.error.status}: ${errorDto.error.message}"))
        } else {
            val result = json.decodeFromString<T>(rawBody)
            Result.success(result)
        }
    } catch (e: Exception) {
        Log.e(tag, "JSON parsing error", e)
        Result.failure(e)
    }
}