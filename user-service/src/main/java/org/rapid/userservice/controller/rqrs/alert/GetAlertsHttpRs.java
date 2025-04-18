package org.rapid.userservice.controller.rqrs.alert;

import org.rapid.userservice.entity.Alert;

import java.util.List;

public record GetAlertsHttpRs(List<Alert> alerts) {
}
