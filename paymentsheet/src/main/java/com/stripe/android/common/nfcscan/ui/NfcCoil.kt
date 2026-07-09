package com.stripe.android.common.nfcscan.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stripe.android.core.strings.ResolvableString
import com.stripe.android.paymentsheet.R
import com.stripe.android.uicore.strings.resolve
import com.stripe.payments.model.R as PaymentsModelR

private val CoilCircleSize = 200.dp
private val TopTextOffset = (-55).dp
private val ShadowElevation = 8.dp

@Composable
internal fun NfcCoil(
    status: NfcScanningStatus,
    shouldRenderTextAboveCoil: Boolean,
    onSuccessShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val canShowText = status !is NfcScanningStatus.Scanned

    Box(modifier.width(IntrinsicSize.Min)) {
        if (shouldRenderTextAboveCoil) {
            NfcCoilInstructionText(
                canShow = canShowText,
                error = (status as? NfcScanningStatus.Idle)?.error,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offsetWithGap(TopTextOffset)
            )
        }

        val isDarkTheme = isSystemInDarkTheme()

        val shadow = if (isDarkTheme) {
            Modifier
        } else {
            Modifier.shadow(elevation = ShadowElevation, shape = CircleShape)
        }

        Box(
            modifier = Modifier
                .size(CoilCircleSize)
                .then(shadow)
                .background(color = MaterialTheme.colors.primary, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            NfcCoilAnimatedInterior(
                status = status,
                onSuccessShown = onSuccessShown,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (!shouldRenderTextAboveCoil) {
            NfcCoilInstructionText(
                canShow = canShowText,
                error = (status as? NfcScanningStatus.Idle)?.error,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offsetWithGap(30.dp)
            )
        }
    }
}

@Composable
private fun NfcCoilInstructionText(
    canShow: Boolean,
    error: ResolvableString?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = canShow,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .wrapContentSize(unbounded = true),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.stripe_nfc_scan_hold_card_behind_phone),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )

            error?.let { errorMessage ->
                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(PaymentsModelR.drawable.stripe_ic_error),
                        contentDescription = null,
                        tint = MaterialTheme.colors.error,
                        modifier = Modifier.size(12.dp),
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = errorMessage.resolve(),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

private fun Modifier.offsetWithGap(gap: Dp): Modifier {
    return layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, 0) {
            val spacingGap = gap.roundToPx()
            placeable.placeRelative(0, spacingGap)
        }
    }
}
