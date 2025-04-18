package org.rapid.userservice.service;

import org.rapid.userservice.service.rqrs.payment.GetPaymentsRq;
import org.rapid.userservice.service.rqrs.payment.GetPaymentsRs;
import org.rapid.userservice.service.rqrs.payment.SavePaymentRq;
import org.rapid.userservice.service.rqrs.payment.SavePaymentRs;

public interface PaymentServiceIf {
    GetPaymentsRs getPayment(GetPaymentsRq rq);
    SavePaymentRs savePayment(SavePaymentRq rq);
}
