package paymentservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import paymentservice.dto.PaymentDto
import paymentservice.publisher.PaymentEventPublisher

@RestController
@RequestMapping("/producer")
class EventRestController(private val publisher: PaymentEventPublisher) {

    @PostMapping("/publish")
    fun sendEvents(@RequestBody paymentDto: PaymentDto) {
        publisher.sendEventsToTopic(paymentDto)
    }
}