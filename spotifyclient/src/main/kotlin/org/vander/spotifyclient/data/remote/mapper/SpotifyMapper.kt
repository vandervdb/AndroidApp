package org.vander.spotifyclient.data.remote.mapper

import org.vander.spotifyclient.data.remote.dto.CurrentlyPlayingWithQueueDto
import org.vander.spotifyclient.data.remote.dto.SpotifyAlbumDto
import org.vander.spotifyclient.data.remote.dto.SpotifyArtistDto
import org.vander.spotifyclient.data.remote.dto.SpotifyImageDto
import org.vander.spotifyclient.data.remote.dto.SpotifyTrackDto
import org.vander.spotifyclient.domain.data.CurrentlyPlayingAndQueue
import org.vander.spotifyclient.domain.data.SpotifyAlbum
import org.vander.spotifyclient.domain.data.SpotifyArtist
import org.vander.spotifyclient.domain.data.SpotifyImage
import org.vander.spotifyclient.domain.data.SpotifyQueue
import org.vander.spotifyclient.domain.data.SpotifyTrack


fun CurrentlyPlayingWithQueueDto.toDomain(): CurrentlyPlayingAndQueue {
    return CurrentlyPlayingAndQueue(
        currentlyPlaying = currentlyPlaying?.toDomain(),
        queue = SpotifyQueue(
            tracks = queue.map { it?.toDomain() ?: SpotifyTrack.empty() }
        )
    )
}

fun SpotifyTrackDto.toDomain(): SpotifyTrack {
    return SpotifyTrack(
        album = album.toDomain(),
        artists = artists.map { it.toDomain() },
        availableMarkets = availableMarkets,
        discNumber = discNumber,
        durationMs = durationMs,
        explicit = explicit,
        externalIds = externalIds.isrc ?: "",
        externalUrls = externalUrls.spotify,
        href = href,
        id = id,
        isPlayable = isPlayable,
        name = name,
        trackNumber = trackNumber,
        type = type,
        uri = uri
    )
}

fun SpotifyAlbumDto.toDomain(): SpotifyAlbum {
    return SpotifyAlbum(
        albumType = albumType,
        totalTracks = totalTracks,
        availableMarkets = availableMarkets,
        externalUrls = externalUrls.spotify,
        href = href,
        id = id,
        images = images.map { it.toDomain() },
        name = name,
        releaseDate = releaseDate,
        type = type,
        uri = uri,
        artists = artists.map { it.toDomain() }
    )
}

fun SpotifyArtistDto.toDomain(): SpotifyArtist {
    return SpotifyArtist(
        externalUrls = externalUrls.spotify,
        href = href,
        id = id,
        name = name,
        type = type,
        uri = uri
    )
}

fun SpotifyImageDto.toDomain(): SpotifyImage {
    return SpotifyImage(
        url = url,
        height = height,
        width = width
    )
}