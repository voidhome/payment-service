package paymentservice.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import paymentservice.domain.OutboxRecord
import paymentservice.domain.OutboxRecordStatus
import paymentservice.domain.OutboxRecordType
import java.util.*

@Repository
interface OutboxRepository : CoroutineCrudRepository<OutboxRecord, UUID> {

    suspend fun findByAggregateId(aggregateId: UUID): OutboxRecord?

    @Query(FIND_OUTBOX_RECORDS_FOR_PROCESSING)
    suspend fun findOutboxRecordForProcessing(
        status: OutboxRecordStatus, type: OutboxRecordType
    ): List<OutboxRecord>

    companion object {
        const val FIND_OUTBOX_RECORDS_FOR_PROCESSING = """
                SELECT * FROM public.outbox_table ot
                WHERE ot.type = :type AND ot.status = :status
                ORDER BY ot.updated_at ASC
                FOR UPDATE SKIP LOCKED
            """
    }
}