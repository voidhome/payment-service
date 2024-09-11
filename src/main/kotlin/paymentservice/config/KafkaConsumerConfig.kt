package paymentservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
class KafkaConsumerConfig(
    private val configProperties: KafkaConfigProperties
) {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, ByteArray> = DefaultKafkaConsumerFactory(consumerProps())

    @Bean
    fun kafkaListenerContainerFactory(containerFactory: ConsumerFactory<String, ByteArray>): ConcurrentKafkaListenerContainerFactory<String, ByteArray> =
        ConcurrentKafkaListenerContainerFactory<String, ByteArray>().apply {
            this.consumerFactory = consumerFactory()
            setConcurrency(Runtime.getRuntime().availableProcessors())
        }

    private fun consumerProps(): Map<String, Any> = hashMapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to configProperties.bootstrapServers,
        ConsumerConfig.GROUP_ID_CONFIG to configProperties.groupId,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ByteArrayDeserializer::class.java,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to configProperties.enableAutoCommitConfig,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to configProperties.autoOffsetResetConfig,
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG to configProperties.maxPollRecordsConfig
    )
}