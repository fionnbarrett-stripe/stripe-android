package com.stripe.android.common.nfcscan.scanner

import com.google.common.truth.Truth.assertThat
import com.stripe.android.core.strings.resolvableString
import com.stripe.android.paymentsheet.R
import org.junit.Test

internal class DefaultNfcCardValidatorTest {

    @Test
    fun `validate returns Validated for non-expired card`() {
        val validator = DefaultNfcCardValidator()

        assertThat(
            validator.validate(
                ScannedCardData(
                    cardNumber = "4242424242424242",
                    expirationMonth = 12,
                    expirationYear = 2030,
                ),
            ),
        ).isEqualTo(NfcCardValidator.Result.Validated)
    }

    @Test
    fun `validate returns expired error when card is expired`() {
        val validator = DefaultNfcCardValidator()

        val result = validator.validate(
            ScannedCardData(
                cardNumber = "4242424242424242",
                expirationMonth = 1,
                expirationYear = 2020,
            ),
        )

        assertThat(result).isEqualTo(
            NfcCardValidator.Result.Invalid(
                userMessage = R.string.stripe_nfc_expired_error.resolvableString,
            ),
        )
    }
}
