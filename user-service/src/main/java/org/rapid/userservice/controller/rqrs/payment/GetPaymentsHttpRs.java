package org.rapid.userservice.controller.rqrs.payment;

import org.rapid.userservice.entity.Payment;

import java.util.List;

public record GetPaymentsHttpRs(List<Payment> payments) {

}
