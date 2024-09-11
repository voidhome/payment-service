package paymentservice.service

import paymentservice.domain.Payment
import paymentservice.dto.CreatePaymentDto

interface PaymentService {

    suspend fun createPayment(paymentDto: CreatePaymentDto): Payment
}