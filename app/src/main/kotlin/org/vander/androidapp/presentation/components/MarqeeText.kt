package org.vander.androidapp.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    durationMillis: Int = 10000
) {
    var textWidth by remember { mutableStateOf(0) }
    var containerWidth by remember { mutableStateOf(0) }
    val offsetX = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .clipToBounds()
            .onGloballyPositioned { coordinates ->
                containerWidth = coordinates.size.width
            }
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            softWrap = false,
            onTextLayout = { layoutResult ->
                textWidth = layoutResult.size.width
            },
            modifier = Modifier.offset { IntOffset(-offsetX.value.roundToInt(), 0) }
        )
    }
    LaunchedEffect(textWidth, containerWidth, text) {
        if (textWidth > 0 && containerWidth > 0 && textWidth > containerWidth) {
            while (true) {
                offsetX.snapTo(0f)
                offsetX.animateTo(
                    targetValue = (textWidth - containerWidth).toFloat(),
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
                delay(1000L)
            }
        } else {
            offsetX.snapTo(0f)
        }
    }
}