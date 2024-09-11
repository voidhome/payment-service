package paymentservice.scheduler

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import paymentservice.domain.OutboxRecordType
import paymentservice.service.OutboxService

@Component
@ConditionalOnProperty(prefix = "scheduler", value = arrayOf("outbox.enable"), havingValue = "true")
class OutboxScheduler(
    private val outboxService: OutboxService
) {

    @Scheduled(cron = "\${scheduler.outbox.cron}")
    fun publishRetryOutboxRecords() = runBlocking {
        try {
            log.info("Запуск планировщика публикации Outbox records")
            val records = outboxService.getOutboxRecordsForProcessing(OutboxRecordType.PAYMENT)
            if (records.isEmpty()) {
                log.info("Нет записей для публикации")
                return@runBlocking
            }
            outboxService.publishRetryOutboxRecords(records)
            log.info("Планировщик успешно завершил работу. Опубликованы ${records.size} записей")
        } catch (ex: Exception) {
            log.error("Ошибка при запуске планировщика: ${ex.message}", ex)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(OutboxScheduler::class.java)
    }
}