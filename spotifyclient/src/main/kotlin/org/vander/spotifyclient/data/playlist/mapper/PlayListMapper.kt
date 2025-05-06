package org.vander.spotifyclient.data.playlist.mapper

import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistDto
import org.vander.spotifyclient.data.playlist.dto.SpotifyPlaylistsResponseDto
import org.vander.spotifyclient.domain.data.SpotifyPlaylist
import org.vander.spotifyclient.domain.data.SpotifyPlaylistsResponse

fun SpotifyPlaylistsResponseDto.toDomain(): SpotifyPlaylistsResponse {
    return SpotifyPlaylistsResponse(
        items = items.map { it.toDomain() }
    )
}

fun SpotifyPlaylistDto.toDomain(): SpotifyPlaylist {
    return SpotifyPlaylist(
        id = id,
        name = name,
        imageUrl = images.firstOrNull()?.url.orEmpty()
    )
}
