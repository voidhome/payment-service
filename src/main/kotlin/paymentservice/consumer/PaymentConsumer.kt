package paymentservice.consumer

import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import paymentservice.dto.PaymentValidationDto
import paymentservice.mapper.OutboxMapper
import paymentservice.service.OutboxService
import paymentservice.utils.Serializer

@Component
class PaymentConsumer(
    private val outboxService: OutboxService,
    private val outboxMapper: OutboxMapper,
    private val serializer: Serializer
) {

    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = arrayOf("\${topics.payment-processed.name}"),
    )
    fun process(consumerRecord: ConsumerRecord<String, ByteArray>) = runBlocking {
        try {
            val paymentValidationDto = serializer.deserialize(consumerRecord.value(), PaymentValidationDto::class.java)
            val outboxRecord = outboxMapper.toOutboxRecord(paymentValidationDto)
            outboxService.markOutboxRecordAsCompleted(outboxRecord)
            log.info("Запись успешно обработана: ${getConsumerRecordInfo(consumerRecord)}")
        } catch (ex: Exception) {
            log.error("Произошла ошибка при обработке записи: ${ex.message}")
        }
    }

    private fun getConsumerRecordInfo(consumerRecord: ConsumerRecord<String, ByteArray>): String {
        val topic = consumerRecord.topic()
        val offset = consumerRecord.offset()
        val key = consumerRecord.key()
        val partition = consumerRecord.partition()
        val timestamp = consumerRecord.timestamp()
        val value = String(consumerRecord.value())
        return "topic: $topic key: $key partition: $partition offset: $offset timestamp: $timestamp value: $value"
    }

    companion object {
        private val log = LoggerFactory.getLogger(PaymentConsumer::class.java)
    }
}