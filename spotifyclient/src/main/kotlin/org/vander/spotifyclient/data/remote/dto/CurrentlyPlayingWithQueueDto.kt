package org.vander.spotifyclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentlyPlayingWithQueueDto(
    @SerialName("currently_playing") val currentlyPlaying: SpotifyTrackDto,
    val queue: List<SpotifyTrackDto>
)

@Serializable
data class SpotifyTrackDto(
    val album: SpotifyAlbumDto,
    val artists: List<SpotifyArtistDto>,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("external_ids") val externalIds: SpotifyExternalIdsDto,
    @SerialName("external_urls") val externalUrls: SpotifyExternalUrlDto,
    val href: String,
    val id: String,
    @SerialName("is_playable") val isPlayable: Boolean = true,
    @SerialName("linked_from") val linkedFrom: Map<String, String> = emptyMap(), // vide ici
    val restrictions: SpotifyRestrictionsDto? = null,
    val name: String,
    val popularity: Int,
    @SerialName("preview_url") val previewUrl: String?,
    @SerialName("track_number") val trackNumber: Int,
    val type: String,
    val uri: String,
    @SerialName("is_local") val isLocal: Boolean
)

@Serializable
data class SpotifyAlbumDto(
    @SerialName("album_type") val albumType: String,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>,
    @SerialName("external_urls") val externalUrls: SpotifyExternalUrlDto,
    val href: String,
    val id: String,
    val images: List<SpotifyImageDto>,
    val name: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    val restrictions: SpotifyRestrictionsDto? = null,
    val type: String,
    val uri: String,
    val artists: List<SpotifyArtistDto>
)

@Serializable
data class SpotifyArtistDto(
    @SerialName("external_urls") val externalUrls: SpotifyExternalUrlDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

@Serializable
data class SpotifyImageDto(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
)

@Serializable
data class SpotifyExternalUrlDto(
    val spotify: String
)

@Serializable
data class SpotifyExternalIdsDto(
    val isrc: String? = null,
    val ean: String? = null,
    val upc: String? = null
)

@Serializable
data class SpotifyRestrictionsDto(
    val reason: String
)
