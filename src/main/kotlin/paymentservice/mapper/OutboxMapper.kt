package paymentservice.mapper

import org.springframework.stereotype.Component
import paymentservice.domain.OutboxRecord
import paymentservice.domain.OutboxRecordStatus
import paymentservice.domain.OutboxRecordType
import paymentservice.domain.Payment
import paymentservice.dto.PaymentValidationDto
import paymentservice.repository.OutboxRepository
import paymentservice.utils.Serializer

@Component
class OutboxMapper(
    private val outboxRepository: OutboxRepository,
    private val serializer: Serializer
) {

    suspend fun toOutboxRecord(payment: Payment): OutboxRecord = OutboxRecord(
        id = null,
        type = OutboxRecordType.PAYMENT,
        aggregateId = payment.id,
        data = serializer.serializeToBytes(payment),
        status = OutboxRecordStatus.NEW,
        version = payment.version,
        createdAt = payment.createdAt,
        updatedAt = payment.updatedAt
    )

    suspend fun toOutboxRecord(dto: PaymentValidationDto): OutboxRecord {
        val existingRecord = dto.payment.id?.let { outboxRepository.findByAggregateId(it) }

        return OutboxRecord(
            id = existingRecord?.id,
            type = OutboxRecordType.PAYMENT,
            aggregateId = dto.payment.id,
            data = serializer.serializeToBytes(dto.payment),
            status = OutboxRecordStatus.NEW,
            version = dto.payment.version,
            createdAt = dto.payment.createdAt,
            updatedAt = dto.payment.updatedAt
        )
    }
}