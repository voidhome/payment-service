package paymentservice.dto

import paymentservice.domain.Payment

data class PaymentValidationDto(
    val isValid: Boolean,
    val payment: Payment
) : BaseDto(payment.id, payment.version)