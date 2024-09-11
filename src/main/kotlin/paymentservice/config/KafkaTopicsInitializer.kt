package paymentservice.config

import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.admin.NewTopic
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.stereotype.Component

@Component
class KafkaTopicsInitializer(
    private val kafkaTopicsConfig: KafkaTopicsConfig,
    private val kafkaAdmin: KafkaAdmin
) {

    @PostConstruct
    fun init() {
        kotlin.runCatching {
            kafkaTopicsConfig.getTopics()
                .map { NewTopic(it.name, it.partition, it.replication) }
                .forEach {
                    kafkaAdmin.createOrModifyTopics(it)
                    log.info("Топик создан или изменён: $it")
                }
        }
            .onSuccess { log.info("Топики Kafka успешно созданы") }
            .onFailure { log.error("Ошибка при создании топиков Kafka: ${it.message}") }
            .getOrThrow()
    }

    companion object {
        val log = LoggerFactory.getLogger(KafkaTopicsInitializer::class.java)
    }
}