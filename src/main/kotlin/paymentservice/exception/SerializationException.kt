package paymentservice.exception

data class SerializationException(val ex: Throwable) : RuntimeException(ex)