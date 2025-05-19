package org.vander.spotifyclient.domain.data

data class UIQueueItem(
    val trackName: String,
    val artistName: String,
    val trackId: String,
) {
    companion object {
        fun empty(): UIQueueItem {
            return UIQueueItem(
                trackName = "",
                artistName = "",
                trackId = ""
            )
        }
    }
}