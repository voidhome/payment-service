package paymentservice.publisher

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import paymentservice.utils.Serializer

@Component
class EventPublisherImpl(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val serializer: Serializer
) : EventPublisher {

    override suspend fun publish(topic: String?, data: Any) {
        val msg = ProducerRecord<String, ByteArray>(topic, serializer.serializeToBytes(data))

        GlobalScope.async {
            try {
                val result = kafkaTemplate.send(msg).get()
                log.info("Результат успешно опубликован: $result")
            } catch (ex: Exception) {
                log.error("Ошибка при публикации события: ${ex.message}")
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(EventPublisherImpl::class.java)
    }
}