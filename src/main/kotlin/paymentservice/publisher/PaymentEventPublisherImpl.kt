package paymentservice.publisher

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import paymentservice.dto.PaymentDto

@Component
class PaymentEventPublisherImpl(private val kafkaTemplate: KafkaTemplate<String, PaymentDto>) :
    PaymentEventPublisher {

    private val logger = LoggerFactory.getLogger(PaymentEventPublisherImpl::class.java)

    override fun sendEventsToTopic(paymentDto: PaymentDto) {
        try {
            val future = kafkaTemplate.send("payment-created-events-topic", paymentDto)
            future.whenComplete { result, ex ->
                if (ex == null) {
                    logger.info("Сообщение успешно отправлено: [$paymentDto] с оффсетом: [${result.recordMetadata.offset()}]")
                } else {
                    logger.error("Не удалось отправить сообщение: [$paymentDto]. Причина: ${ex.message}")
                }
            }
        } catch (ex: Exception) {
            logger.error("Ошибка при отправке сообщения: ${ex.message}")
        }
    }
}