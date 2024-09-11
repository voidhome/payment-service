package paymentservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import paymentservice.domain.Payment
import paymentservice.dto.CreatePaymentDto
import paymentservice.mapper.PaymentMapper
import paymentservice.repository.PaymentRepository

@Service
class PaymentServiceImpl(
    private val outboxService: OutboxService,
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper
) : PaymentService {

    override suspend fun createPayment(paymentDto: CreatePaymentDto): Payment {
        try {
            val payment = paymentRepository.save(paymentMapper.toPayment(paymentDto))
            val record = outboxService.createOutboxRecord(payment)
            outboxService.publishOutboxRecord(record)
            log.info("Платеж успешно сохранён: $payment")
            return payment
        } catch (ex: Exception) {
            log.error("Ошибка при создании платежа: ${ex.message}", ex)
            throw RuntimeException("Не удалось создать платеж", ex)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(PaymentServiceImpl::class.java)
    }
}