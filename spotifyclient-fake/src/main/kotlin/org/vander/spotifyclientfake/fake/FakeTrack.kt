package org.vander.spotifyclientfake.fake


import com.spotify.protocol.types.Album
import com.spotify.protocol.types.Artist
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.Track

@Suppress("UNCHECKED_CAST")
fun createFakeTrack(): Track {
    val imageUri = ImageUri("https://via.placeholder.com/300")

    val fakeArtist = Artist::class.java.getDeclaredConstructor().apply { isAccessible = true }.newInstance()
    val fakeAlbum = Album::class.java.getDeclaredConstructor().apply { isAccessible = true }.newInstance()

    // On accède aux champs privés via la réflexion
    setField(fakeArtist, "name", "Fake Artist")
    setField(fakeArtist, "uri", "spotify:artist:123")
    setField(fakeArtist, "imageUri", null)

    setField(fakeAlbum, "name", "Fake Album")
    setField(fakeAlbum, "uri", "spotify:album:123")
    setField(fakeAlbum, "imageUri", null)

    val fakeTrack = Track::class.java.getDeclaredConstructor().apply { isAccessible = true }.newInstance()

    setField(fakeTrack, "artist", fakeArtist)
    setField(fakeTrack, "artists", listOf(fakeArtist))
    setField(fakeTrack, "album", fakeAlbum)
    setField(fakeTrack, "duration", 180_000L)
    setField(fakeTrack, "name", "Fake Track Name")
    setField(fakeTrack, "uri", "spotify:track:123")
    setField(fakeTrack, "imageUri", imageUri)
    setField(fakeTrack, "isEpisode", false)
    setField(fakeTrack, "isPodcast", false)

    return fakeTrack
}

private fun setField(instance: Any, fieldName: String, value: Any?) {
    val field = instance.javaClass.getDeclaredField(fieldName)
    field.isAccessible = true
    field.set(instance, value)
}
