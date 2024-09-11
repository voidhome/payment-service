package paymentservice.service

import paymentservice.domain.OutboxRecord
import paymentservice.domain.OutboxRecordType
import paymentservice.domain.Payment

interface OutboxService {

    suspend fun createOutboxRecord(payment: Payment): OutboxRecord

    suspend fun getOutboxRecordsForProcessing(type: OutboxRecordType): List<OutboxRecord>

    suspend fun publishOutboxRecord(record: OutboxRecord)

    suspend fun publishRetryOutboxRecords(outboxRecords: List<OutboxRecord>)

    suspend fun markOutboxRecordAsCompleted(record: OutboxRecord)
}