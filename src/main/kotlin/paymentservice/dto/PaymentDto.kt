package paymentservice.dto

data class PaymentDto(
    val amount: Double, val currency: String, val paymentMethod: String
)