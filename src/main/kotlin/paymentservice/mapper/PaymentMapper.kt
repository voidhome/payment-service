package paymentservice.mapper

import org.springframework.stereotype.Component
import paymentservice.domain.Payment
import paymentservice.domain.PaymentStatus
import paymentservice.dto.CreatePaymentDto
import java.time.Instant

@Component
class PaymentMapper {

    fun toPayment(paymentDto: CreatePaymentDto): Payment =
        Payment(
            id = null,
            amount = paymentDto.amount,
            currency = paymentDto.currency,
            paymentMethod = paymentDto.paymentMethod,
            description = "",
            status = PaymentStatus.NEW,
            version = 0,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
}