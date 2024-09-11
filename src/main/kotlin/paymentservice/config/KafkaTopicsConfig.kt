package paymentservice.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "topics")
class KafkaTopicsConfig {

    var paymentCreated: TopicConfig = TopicConfig()
    var paymentProcessed: TopicConfig = TopicConfig()
    var retryTopic: TopicConfig = TopicConfig()

    fun getTopics() = listOf(
        paymentCreated,
        paymentProcessed,
        retryTopic
    )

    @PostConstruct
    fun logConfigProperties() {
        log.info("Настроенные топики kafka: ${getTopics()}")
    }

    companion object {
        private val log = LoggerFactory.getLogger(KafkaTopicsConfig::class.java)
    }
}