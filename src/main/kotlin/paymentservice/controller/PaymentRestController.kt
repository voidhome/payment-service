package paymentservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import paymentservice.dto.CreatePaymentDto
import paymentservice.service.PaymentService

@RestController
@RequestMapping("/api/v1/payments")
class PaymentRestController(
    private val paymentService: PaymentService,
) {

    @PostMapping
    suspend fun createPayment(@RequestBody createPaymentDto: CreatePaymentDto) =
        ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(createPaymentDto))
}

