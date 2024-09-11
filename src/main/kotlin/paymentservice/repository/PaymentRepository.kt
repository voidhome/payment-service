package paymentservice.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import paymentservice.domain.Payment
import java.util.*

@Repository
interface PaymentRepository : CoroutineCrudRepository<Payment, UUID>