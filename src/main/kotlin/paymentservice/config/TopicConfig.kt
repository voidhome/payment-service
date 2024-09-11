package paymentservice.config

data class TopicConfig(
    var name: String = "",
    var partition: Int = 1,
    var replication: Short = 1,
)