package org.rapid.userservice.controller;

import jakarta.validation.Valid;
import org.rapid.userservice.controller.rqrs.payment.GetPaymentsHttpRq;
import org.rapid.userservice.controller.rqrs.payment.GetPaymentsHttpRs;
import org.rapid.userservice.controller.rqrs.payment.SavePaymentHttpRq;
import org.rapid.userservice.controller.rqrs.payment.SavePaymentHttpRs;
import org.rapid.userservice.service.PaymentServiceIf;
import org.rapid.userservice.service.rqrs.payment.GetPaymentsRq;
import org.rapid.userservice.service.rqrs.payment.GetPaymentsRs;
import org.rapid.userservice.service.rqrs.payment.SavePaymentRq;
import org.rapid.userservice.service.rqrs.payment.SavePaymentRs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/payment")
public class PaymentController {

    private final PaymentServiceIf paymentService;

    @Autowired
    public PaymentController(PaymentServiceIf paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("")
    ResponseEntity<GetPaymentsHttpRs> getPayment(@Valid GetPaymentsHttpRq rq){
        GetPaymentsRs rs = paymentService.getPayment(new GetPaymentsRq(rq.username()));
        return ResponseEntity.ok(new GetPaymentsHttpRs(rs.payments()));
    }
    @PostMapping("save")
    ResponseEntity<SavePaymentHttpRs> savePayment(@Valid @RequestBody SavePaymentHttpRq rq){
        SavePaymentRs rs = paymentService.savePayment(SavePaymentRq.builder()
                        .timestamp(rq.timestamp())
                        .paymentAmount(rq.paymentAmount())
                        .currencyCode(rq.currencyCode())
                        .role(rq.role())
                        .username(rq.username())
                .build());
        return ResponseEntity.ok(new SavePaymentHttpRs(rs.payment()));
    }
}
