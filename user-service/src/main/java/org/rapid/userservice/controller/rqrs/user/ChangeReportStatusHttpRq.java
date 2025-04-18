package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;

public record ChangeReportStatusHttpRq (@NotNull String username, @NotNull boolean status){
}
