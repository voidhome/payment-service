package paymentservice.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaAdminConfig(
    private val configProperties: KafkaConfigProperties
) {

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        return KafkaAdmin(adminProps())
    }

    private fun adminProps(): Map<String, Any> = hashMapOf(
        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to configProperties.bootstrapServers,
    )
}