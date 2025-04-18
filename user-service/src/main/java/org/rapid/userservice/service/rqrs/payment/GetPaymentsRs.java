package org.rapid.userservice.service.rqrs.payment;

import org.rapid.userservice.entity.Payment;

import java.util.List;

public record GetPaymentsRs(List<Payment> payments) {
}
