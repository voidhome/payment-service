package paymentservice.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("outbox_table")
data class OutboxRecord(
    @Id @Column(ID) var id: UUID?,
    @Column(TYPE) var type: OutboxRecordType,
    @Column(AGGREGATE_ID) var aggregateId: UUID?,
    @Column(DATA) var data: ByteArray,
    @Column(STATUS) var status: OutboxRecordStatus,
    @Column(VERSION) var version: Long,
    @CreatedDate @Column(CREATED_AT) var createdAt: Instant?,
    @LastModifiedDate @Column(UPDATED_AT) var updatedAt: Instant?
) {

    override fun toString(): String {
        return "OutboxRecord(id=$id, type=$type, status=$status, aggregateId=$aggregateId, " +
                "data=${String(data)}, version=$version, createdAt = $createdAt, updatedAt = $updatedAt)"
    }

    companion object {
        const val ID = "id"
        const val TYPE = "type"
        const val STATUS = "status"
        const val AGGREGATE_ID = "aggregate_id"
        const val DATA = "data"
        const val VERSION = "version"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
    }
}
