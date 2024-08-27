package paymentservice.publisher

import paymentservice.dto.PaymentDto

interface PaymentEventPublisher {

    fun sendEventsToTopic(paymentDto: PaymentDto)
}