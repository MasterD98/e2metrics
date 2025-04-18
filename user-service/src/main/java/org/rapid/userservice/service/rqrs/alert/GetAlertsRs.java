package org.rapid.userservice.service.rqrs.alert;

import org.rapid.userservice.entity.Alert;

import java.util.List;

public record GetAlertsRs(List<Alert> alerts) {
}
