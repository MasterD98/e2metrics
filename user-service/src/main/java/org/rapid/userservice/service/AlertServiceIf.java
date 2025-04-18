package org.rapid.userservice.service;

import org.rapid.userservice.service.rqrs.alert.*;

public interface AlertServiceIf {
    GetAlertLimitsRs getAlertLimits(GetAlertLimitsRq rq);
    SaveAlertLimitsRs saveAlertLimits(SaveAlertLimitsRq rq);
    GetAlertsRs getAlerts(GetAlertsRq rq);
    SaveAlertRs saveAlert(SaveAlertRq rq);
    UpdateAlertStatusRs updateAlertStatus(UpdateAlertStatusRq rq);
}
