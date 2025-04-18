package org.rapid.userservice.service.rqrs.payment;

import lombok.Builder;
import org.rapid.userservice.entity.Payment;

public record SavePaymentRs(Payment payment) {
}
