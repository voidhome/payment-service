package paymentservice.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("payments")
data class Payment(
    @Id @Column(ID) val id: UUID?,
    @Column(AMOUNT) val amount: Double,
    @Column(CURRENCY) val currency: String,
    @Column(PAYMENT_METHOD) val paymentMethod: String,
    @Column(DESCRIPTION) val description: String?,
    @Column(STATUS) val status: PaymentStatus,
    @Column(VERSION) var version: Long,
    @CreatedDate @Column(CREATED_AT) var createdAt: Instant?,
    @LastModifiedDate @Column(UPDATED_AT) var updatedAt: Instant?
) {

    override fun toString(): String {
        return "Payment(id='$id', version='$version', amount='$amount', currency=$currency, paymentMethod=$paymentMethod, " +
                "status=$status, description='$description', createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    companion object {
        const val ID = "id"
        const val VERSION = "version"
        const val AMOUNT = "amount"
        const val CURRENCY = "currency"
        const val PAYMENT_METHOD = "payment_method"
        const val STATUS = "status"
        const val DESCRIPTION = "description"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
    }
}