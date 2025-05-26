package org.vander.spotifyclient.domain.state

import org.vander.spotifyclient.domain.data.UIQueueItem

data class UIQueueState(
    val items: List<UIQueueItem>
) {
    companion object {
        fun empty(): UIQueueState {
            return UIQueueState(List(0) { UIQueueItem.empty() })
        }
    }
}