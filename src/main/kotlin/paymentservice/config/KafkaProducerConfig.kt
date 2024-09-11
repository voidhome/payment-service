package paymentservice.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfig(
    private val configProperties: KafkaConfigProperties,
) {

    @Bean
    fun producerFactory(): ProducerFactory<String, ByteArray> {
        return DefaultKafkaProducerFactory(producerProps())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, ByteArray> {
        return KafkaTemplate(producerFactory())
    }

    private fun producerProps(): Map<String, Any> = hashMapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to configProperties.bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to ByteArraySerializer::class.java,
        ProducerConfig.ACKS_CONFIG to configProperties.acksConfig,
        ProducerConfig.RETRIES_CONFIG to configProperties.retriesConfig,
        ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG to configProperties.deliveryTimeoutMsConfig,
        ProducerConfig.MAX_REQUEST_SIZE_CONFIG to configProperties.maxRequestSizeConfig,
        ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG to configProperties.requestTimeoutMsConfig,
    )
}