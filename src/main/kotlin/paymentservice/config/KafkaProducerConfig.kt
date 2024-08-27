package paymentservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import paymentservice.dto.PaymentDto


@Configuration
class KafkaProducerConfig {

    @Bean
    fun producerConfig(): MutableMap<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer::class.java)
        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, PaymentDto> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, PaymentDto> {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun createTopic(): NewTopic {
        return TopicBuilder
            .name("payment-created-events-topic")
            .partitions(3)
            .replicas(1)
            .build()
    }
}