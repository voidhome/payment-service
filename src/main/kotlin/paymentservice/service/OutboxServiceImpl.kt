package paymentservice.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import paymentservice.config.KafkaTopicsConfig
import paymentservice.domain.OutboxRecord
import paymentservice.domain.OutboxRecordStatus
import paymentservice.domain.OutboxRecordType
import paymentservice.domain.Payment
import paymentservice.mapper.OutboxMapper
import paymentservice.publisher.EventPublisher
import paymentservice.repository.OutboxRepository
import java.time.Instant

@Service
@Transactional
class OutboxServiceImpl(
    private val outboxRepository: OutboxRepository,
    private val outboxMapper: OutboxMapper,
    private val eventPublisher: EventPublisher,
    private val topicsConfig: KafkaTopicsConfig,
) : OutboxService {

    override suspend fun createOutboxRecord(payment: Payment): OutboxRecord {
        val outboxRecord = outboxRepository.save(outboxMapper.toOutboxRecord(payment))
        log.info("OutboxRecord успешно создан с ID: ${outboxRecord.id}")
        return outboxRecord
    }

    override suspend fun getOutboxRecordsForProcessing(type: OutboxRecordType): List<OutboxRecord> {
        log.info("Запрос OutboxRecords типа: $type")
        val outboxRecords = outboxRepository.findOutboxRecordForProcessing(OutboxRecordStatus.SENT, type)
        log.info("Найдено ${outboxRecords.size} OutboxRecords для типа: $type")
        return outboxRecords
    }

    override suspend fun publishOutboxRecord(record: OutboxRecord) {
        try {
            eventPublisher.publish(topicsConfig.paymentCreated.name, record)
            outboxRepository.save(record.copy(status = OutboxRecordStatus.SENT, updatedAt = Instant.now()))
            log.info("Outbox record успешно опубликован: $record")
        } catch (ex: Exception) {
            log.error("Произошла ошибка при публикации Outbox record: ${ex.message}")
            throw RuntimeException("Произошла ошибка: ", ex)
        }
    }

    override suspend fun publishRetryOutboxRecords(outboxRecords: List<OutboxRecord>) {
        try {
            for (outboxRecord in outboxRecords) {
                outboxRecord.updatedAt = Instant.now()
                eventPublisher.publish(topicsConfig.retryTopic.name, outboxRecord)
            }
            outboxRepository.saveAll(outboxRecords)
            log.info("Outbox records успешно опубликованы")
        } catch (ex: Exception) {
            log.error("Произошла ошибка при публикации Outbox records: ${ex.message}")
            throw RuntimeException("Произошла ошибка: ", ex)
        }
    }

    override suspend fun markOutboxRecordAsCompleted(record: OutboxRecord) {
        try {
            outboxRepository.save(record.copy(status = OutboxRecordStatus.COMPLETED, updatedAt = Instant.now()))
            log.info("Статус Outbox record успешно обновлён на COMPLETED: $record")
        } catch (ex: Exception) {
            log.error("Произошла ошибка при обновлении статуса Outbox record: ${ex.message}")
            throw RuntimeException("Произошла ошибка: ", ex)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(OutboxServiceImpl::class.java)
    }
}