package org.vander.spotifyclient.domain.data

data class SpotifyAlbum(
    val albumType: String,
    val totalTracks: Int,
    val availableMarkets: List<String>,
    val externalUrls: String,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val releaseDate: String,
    val type: String,
    val uri: String,
    val artists: List<SpotifyArtist>
)
