package com.stripe.android.common.nfcscan.scanner

import com.stripe.android.core.strings.ResolvableString
import com.stripe.android.core.strings.resolvableString
import com.stripe.android.core.utils.DateUtils
import com.stripe.android.paymentsheet.R
import javax.inject.Inject

internal interface NfcCardValidator {
    fun validate(scannedCardData: ScannedCardData): Result

    sealed interface Result {
        data object Validated : Result

        data class Invalid(
            val userMessage: ResolvableString,
        ) : Result
    }
}

internal class DefaultNfcCardValidator @Inject constructor() : NfcCardValidator {
    override fun validate(scannedCardData: ScannedCardData): NfcCardValidator.Result {
        return scannedCardData.run {
            if (!DateUtils.isExpiryDataValid(expiryYear = expirationYear, expiryMonth = expirationMonth)) {
                NfcCardValidator.Result.Invalid(
                    userMessage = R.string.stripe_nfc_expired_error.resolvableString,
                )
            } else {
                NfcCardValidator.Result.Validated
            }
        }
    }
}
