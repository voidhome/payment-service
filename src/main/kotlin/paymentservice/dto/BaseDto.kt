package paymentservice.dto

import java.util.*

sealed class BaseDto(
    val aggregateId: UUID?,
    open val version: Long
)