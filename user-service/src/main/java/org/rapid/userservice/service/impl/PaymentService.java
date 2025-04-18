package org.rapid.userservice.service.impl;

import org.rapid.userservice.entity.Payment;
import org.rapid.userservice.entity.User;
import org.rapid.userservice.repository.PaymentRepository;
import org.rapid.userservice.repository.UserRepository;
import org.rapid.userservice.service.PaymentServiceIf;
import org.rapid.userservice.service.rqrs.payment.GetPaymentsRq;
import org.rapid.userservice.service.rqrs.payment.GetPaymentsRs;
import org.rapid.userservice.service.rqrs.payment.SavePaymentRq;
import org.rapid.userservice.service.rqrs.payment.SavePaymentRs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements PaymentServiceIf {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GetPaymentsRs getPayment(GetPaymentsRq rq) {
        List<Payment> payments = paymentRepository.getAllByUserUsername(rq.username());
        if(payments.isEmpty()){
            throw new IllegalArgumentException("invalid username");
        }
        return new GetPaymentsRs(payments);
    }

    @Override
    public SavePaymentRs savePayment(SavePaymentRq rq) {
        Optional<User> user = userRepository.getUserByUsername(rq.username());
        if(user.isEmpty()){
            throw new IllegalArgumentException("Invalid username");
        }
        Payment payment = paymentRepository
                .save(
                        Payment.builder()
                                .user(user.get())
                                .paymentAmount(rq.paymentAmount())
                                .currencyCode(rq.currencyCode())
                                .role(rq.role())
                                .timestamp(rq.timestamp())
                                .build());
        return new SavePaymentRs(payment);
    }
}
