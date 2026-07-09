package com.stripe.android.common.nfcscan.scanner

import app.cash.turbine.Turbine

internal class FakeNfcCardValidator(
    private val result: NfcCardValidator.Result = NfcCardValidator.Result.Validated,
) : NfcCardValidator {
    val validateCalls = Turbine<ScannedCardData>()

    override fun validate(scannedCardData: ScannedCardData): NfcCardValidator.Result {
        validateCalls.add(scannedCardData)
        return result
    }

    fun ensureAllEventsConsumed() {
        validateCalls.ensureAllEventsConsumed()
    }
}
